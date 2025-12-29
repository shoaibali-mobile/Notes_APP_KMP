package com.shoaib.notes_app_kmp.data.local

import com.shoaib.notes_app_kmp.util.AppLogger
import com.shoaib.notes_app_kmp.util.KeychainHelper
import platform.Security.SecRandomCopyBytes
import platform.Security.errSecSuccess

/**
 * iOS implementation of database encryption key management using Keychain.
 * 
 * HOW IT WORKS:
 * 1. Uses iOS Keychain to store the encryption key securely (hardware-backed)
 * 2. Generates a random 32-byte key on first use using SecRandomCopyBytes
 * 3. Stores it in Keychain with kSecAttrAccessibleWhenUnlockedThisDeviceOnly
 * 4. Retrieves it when needed to encrypt/decrypt the database
 * 
 * SECURITY:
 * - Keychain is hardware-backed (uses Secure Enclave on supported devices)
 * - Key is encrypted by iOS system
 * - Only accessible when device is unlocked
 * - Bound to this device only (cannot be transferred)
 * 
 * COMPARISON WITH ANDROID:
 * - Android: Uses Android Keystore + EncryptedSharedPreferences
 * - iOS: Uses Keychain (simpler, built-in secure storage)
 */
actual fun getDatabaseEncryptionKey(): ByteArray {
    val tag = "SQLCipher-Encryption-iOS"
    
    AppLogger.d(tag, "🔐 Starting database encryption key retrieval (iOS)...")
    
    val keyAlias = "db_encryption_key"
    
    // Try to load existing key from Keychain
    val existingKey = KeychainHelper.load(keyAlias)
    
    if (existingKey == null) {
        AppLogger.d(tag, "🆕 No encryption key found in Keychain. Generating new 32-byte key...")
        
        // Generate a new 32-byte (256-bit) key using iOS SecureRandom
        // SecRandomCopyBytes is cryptographically secure random number generator
        val newKey = ByteArray(32)
        val status = SecRandomCopyBytes(null, 32, newKey)
        
        if (status != errSecSuccess) {
            AppLogger.d(tag, "❌ Failed to generate secure random key: $status")
            throw IllegalStateException("Failed to generate secure random key: $status")
        }
        
        AppLogger.d(tag, "✅ Generated new 32-byte (256-bit) database encryption key")
        
        // Save to Keychain
        val saved = KeychainHelper.save(keyAlias, newKey)
        if (!saved) {
            AppLogger.d(tag, "❌ Failed to save encryption key to Keychain")
            throw IllegalStateException("Failed to save encryption key to Keychain")
        }
        
        AppLogger.d(tag, "💾 Stored encryption key securely in iOS Keychain")
        AppLogger.d(tag, "✅ Database encryption key setup complete (new key generated)")
        
        return newKey
    } else {
        AppLogger.d(tag, "🔍 Found existing encryption key in Keychain")
        AppLogger.d(tag, "🔓 Successfully retrieved database key from Keychain")
        AppLogger.d(tag, "✅ Database encryption key retrieved successfully (existing key)")
        
        return existingKey
    }
}
