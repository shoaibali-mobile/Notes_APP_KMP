package com.shoaib.notes_app_kmp.util

/**
 * Helper for biometric authentication (fingerprint/face ID).
 * Uses expect/actual pattern to support both Android and iOS platforms.
 */
expect class BiometricHelper {
    /**
     * Check if biometric authentication is available on the device.
     * @return true if biometrics are available, false otherwise
     */
    fun isBiometricAvailable(): Boolean
    
    /**
     * Get the type of biometric available (e.g., "Fingerprint", "Face ID", "Touch ID").
     * @return String describing the biometric type, or null if not available
     */
    fun getBiometricType(): String?
    
    /**
     * Authenticate using biometrics.
     * @param title Title for the biometric prompt
     * @param subtitle Subtitle for the biometric prompt
     * @param onSuccess Callback when authentication succeeds
     * @param onError Callback when authentication fails (error message)
     * @param onCancel Callback when user cancels authentication
     */
    fun authenticate(
        title: String = "Biometric Authentication",
        subtitle: String = "Please authenticate to access your notes",
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onCancel: () -> Unit
    )
}

expect fun createBiometricHelper(): BiometricHelper

