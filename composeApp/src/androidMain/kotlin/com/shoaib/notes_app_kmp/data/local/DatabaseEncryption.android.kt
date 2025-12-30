package com.shoaib.notes_app_kmp.data.local

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.shoaib.notes_app_kmp.appContext
import com.shoaib.notes_app_kmp.util.AppLogger
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * Android implementation of database encryption key management using Android Keystore.
 * 
 * This function manages the SQLCipher database encryption key securely:
 * 1. Uses Android Keystore to store a master key (hardware-backed if available)
 * 2. Generates a 32-byte encryption key for SQLCipher
 * 3. Encrypts the database key with the Keystore master key
 * 4. Stores the encrypted key in SharedPreferences
 */
actual fun getDatabaseEncryptionKey(): ByteArray {
    val tag = "SQLCipher-Encryption"
    
    AppLogger.d(tag, "🔐 Starting database encryption key retrieval...")
    
    // Step 1: Get or create the master key in Android Keystore
    val masterKeyAlias = "db_master_key"
    val keyStore = KeyStore.getInstance("AndroidKeyStore")
    keyStore.load(null)
    
    AppLogger.d(tag, "📦 AndroidKeyStore loaded successfully")
    
    // Step 2: Check if master key already exists
    if (!keyStore.containsAlias(masterKeyAlias)) {
        AppLogger.d(tag, "🔑 Master key not found. Creating new master key in AndroidKeyStore...")
        
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )

        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            masterKeyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256) // 256-bit AES key
            .build()

        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
        
        AppLogger.d(tag, "✅ Master key created successfully in AndroidKeyStore (256-bit AES)")
    } else {
        AppLogger.d(tag, "✅ Master key found in AndroidKeyStore")
    }

    // Step 3: Get the master key from Keystore
    val masterKey = keyStore.getKey(masterKeyAlias, null) as SecretKey
    AppLogger.d(tag, "🔓 Master key retrieved from AndroidKeyStore")

    // Step 4: Get or generate the database encryption key
    val prefs = appContext.getSharedPreferences("db_prefs", android.content.Context.MODE_PRIVATE)
    val keyAlias = "db_encryption_key_encrypted"

    // Try to retrieve the encrypted database key
    val encryptedKeyBase64 = prefs.getString(keyAlias, null)

    if (encryptedKeyBase64 == null) {
        AppLogger.d(tag, "🆕 No encrypted database key found. Generating new 32-byte key...")
        
        // Generate a new 32-byte (256-bit) key for SQLCipher
        val random = SecureRandom()
        val newKey = ByteArray(32)
        random.nextBytes(newKey)
        
        AppLogger.d(tag, "✅ Generated new 32-byte (256-bit) database encryption key")

        // Encrypt the database key using the master key from Keystore
        val cipher = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, masterKey)

        // Encrypt the key
        val encryptedKey = cipher.doFinal(newKey)
        
        AppLogger.d(tag, "🔒 Encrypted database key using master key from Keystore")

        // Store the encrypted key in SharedPreferences
        // It's safe because it's encrypted with the Keystore key
        val encryptedKeyString = android.util.Base64.encodeToString(
            encryptedKey,
            android.util.Base64.NO_WRAP
        )
        prefs.edit().putString(keyAlias, encryptedKeyString).apply()
        
        AppLogger.d(tag, "💾 Stored encrypted database key in SharedPreferences")

        // Also store the IV (Initialization Vector) needed for decryption
        val iv = cipher.iv
        val ivString = android.util.Base64.encodeToString(iv, android.util.Base64.NO_WRAP)
        prefs.edit().putString("${keyAlias}_iv", ivString).apply()
        
        AppLogger.d(tag, "💾 Stored IV (Initialization Vector) for decryption")
        AppLogger.d(tag, "✅ Database encryption key setup complete (new key generated)")

        return newKey

    } else {
        AppLogger.d(tag, "🔍 Found existing encrypted database key. Decrypting...")
        
        // Decrypt the stored key using the master key from Keystore
        val encryptedKey =
            android.util.Base64.decode(encryptedKeyBase64, android.util.Base64.NO_WRAP)
        val ivString = prefs.getString("${keyAlias}_iv", null)
            ?: throw IllegalStateException("IV not found for database key")
        val iv = android.util.Base64.decode(ivString, android.util.Base64.NO_WRAP)

        val cipher = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding")
        val algorithmParameterSpec = javax.crypto.spec.GCMParameterSpec(128, iv)
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, masterKey, algorithmParameterSpec)

        val decryptedKey = cipher.doFinal(encryptedKey)
        
        AppLogger.d(tag, "🔓 Successfully decrypted database key from storage")
        AppLogger.d(tag, "✅ Database encryption key retrieved successfully (existing key)")

        return decryptedKey
    }
}