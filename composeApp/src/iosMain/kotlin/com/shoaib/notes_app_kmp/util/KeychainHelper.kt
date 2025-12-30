package com.shoaib.notes_app_kmp.util

import platform.Foundation.*
import platform.Security.*
import platform.darwin.OSStatus

/**
 * iOS Keychain helper for secure key storage.
 * 
 * Keychain is iOS's secure storage (equivalent to Android Keystore).
 * Keys stored here are hardware-backed and encrypted by the system.
 * 
 * HOW IT WORKS:
 * - Uses iOS Security framework (Keychain Services)
 * - Keys are stored with kSecClassGenericPassword
 * - Accessible only when device is unlocked (kSecAttrAccessibleWhenUnlockedThisDeviceOnly)
 * - Hardware-backed on devices with Secure Enclave
 */
object KeychainHelper {
    private const val SERVICE = "com.shoaib.notes_app_kmp"
    
    /**
     * Save data to Keychain securely.
     * 
     * @param key The key identifier
     * @param data The data to store (as ByteArray)
     * @return true if successful, false otherwise
     */
    fun save(key: String, data: ByteArray): Boolean {
        val query = mapOf(
            kSecClass to kSecClassGenericPassword,
            kSecAttrService to SERVICE,
            kSecAttrAccount to key,
            kSecValueData to NSData.dataWithBytes(data, data.size),
            kSecAttrAccessible to kSecAttrAccessibleWhenUnlockedThisDeviceOnly
        )
        
        // Delete existing item first (if exists)
        SecItemDelete(query)
        
        // Add new item
        val status = SecItemAdd(query, null)
        return status == errSecSuccess
    }
    
    /**
     * Load data from Keychain.
     * 
     * @param key The key identifier
     * @return The stored data as ByteArray, or null if not found
     */
    fun load(key: String): ByteArray? {
        val query = mapOf(
            kSecClass to kSecClassGenericPassword,
            kSecAttrService to SERVICE,
            kSecAttrAccount to key,
            kSecReturnData to true,
            kSecMatchLimit to kSecMatchLimitOne
        )
        
        val result = mutableMapOf<Any?, Any?>()
        val status = SecItemCopyMatching(query, result)
        
        if (status == errSecSuccess) {
            val data = result[kSecValueData] as? NSData
            return data?.let {
                val bytes = ByteArray(it.length.toInt())
                it.getBytes(bytes, it.length)
                bytes
            }
        }
        
        return null
    }
    
    /**
     * Check if key exists in Keychain.
     * 
     * @param key The key identifier
     * @return true if key exists, false otherwise
     */
    fun exists(key: String): Boolean {
        return load(key) != null
    }
}
