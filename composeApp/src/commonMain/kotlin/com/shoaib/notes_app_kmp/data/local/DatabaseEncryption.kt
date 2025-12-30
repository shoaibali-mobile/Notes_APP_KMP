package com.shoaib.notes_app_kmp.data.local

expect fun getDatabaseEncryptionKey(): ByteArray


/**
 * Expect declaration for getting the database encryption key.
 *
 * This function returns a 32-byte (256-bit) encryption key that will be used
 * by SQLCipher to encrypt/decrypt the database.
 *
 * Why expect/actual?
 * - Android: Uses EncryptedSharedPreferences with hardware-backed Keystore
 * - iOS: Will use Keychain (to be implemented later)
 *
 * Security Note:
 * The key must be stored securely. If lost, the encrypted database becomes
 * unreadable. The key should never be hardcoded or stored in plain text.
 */