package com.shoaib.notes_app_kmp.util

import platform.LocalAuthentication.*
import platform.Foundation.NSLocalizedString
import com.shoaib.notes_app_kmp.util.AppLogger

/**
 * iOS implementation of BiometricHelper using LocalAuthentication framework.
 */
actual class BiometricHelper {
    
    private val context = LAContext()
    
    actual fun isBiometricAvailable(): Boolean {
        var error: platform.Foundation.NSError? = null
        val canEvaluate = context.canEvaluatePolicy(
            LAPolicyDeviceOwnerAuthenticationWithBiometrics,
            error = error
        )
        
        if (error != null) {
            AppLogger.d("BiometricHelper", "Biometric check error: ${error.localizedDescription}")
            return false
        }
        
        return canEvaluate
    }
    
    actual fun getBiometricType(): String? {
        if (!isBiometricAvailable()) return null
        
        return when (context.biometryType) {
            LABiometryTypeFaceID -> "Face ID"
            LABiometryTypeTouchID -> "Touch ID"
            LABiometryTypeNone -> null
            else -> "Biometric"
        }
    }
    
    actual fun authenticate(
        title: String,
        subtitle: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onCancel: () -> Unit
    ) {
        if (!isBiometricAvailable()) {
            AppLogger.d("BiometricHelper", "Biometric authentication not available")
            onError("Biometric authentication not available")
            return
        }
        
        val reason = subtitle.ifEmpty { "Please authenticate to access your notes" }
        
        context.evaluatePolicy(
            policy = LAPolicyDeviceOwnerAuthenticationWithBiometrics,
            localizedReason = reason
        ) { success, error ->
            if (success) {
                AppLogger.d("BiometricHelper", "Biometric authentication succeeded")
                onSuccess()
            } else {
                val nsError = error as? platform.Foundation.NSError
                if (nsError != null) {
                    val errorCode = nsError.code.toLong()
                    AppLogger.d("BiometricHelper", "Biometric authentication error: $errorCode")
                    
                    when (errorCode) {
                        kLAErrorUserCancel.toLong(),
                        kLAErrorSystemCancel.toLong() -> {
                            onCancel()
                        }
                        else -> {
                            val errorMessage = nsError.localizedDescription ?: "Authentication failed"
                            onError(errorMessage)
                        }
                    }
                } else {
                    onError("Authentication failed")
                }
            }
        }
    }
}

actual fun createBiometricHelper(): BiometricHelper = BiometricHelper()

