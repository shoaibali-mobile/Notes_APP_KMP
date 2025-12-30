package com.shoaib.notes_app_kmp.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.shoaib.notes_app_kmp.appContext
import com.shoaib.notes_app_kmp.currentActivity
import com.shoaib.notes_app_kmp.util.logD

/**
 * Android implementation of BiometricHelper using BiometricPrompt.
 * Works with FragmentActivity (used in Compose apps).
 * Supports Face ID, Fingerprint, and Device Credential fallback.
 */
actual class BiometricHelper {
    
    // BiometricPrompt error codes from androidx.biometric library
    private companion object {
        const val ERROR_USER_CANCELED = 10
        const val ERROR_NEGATIVE_BUTTON = 13
    }
    
    private val context: Context = appContext
    
    actual fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(context)
        // Check for any biometric authentication (strong or weak)
        val canStrongAuth = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        val canWeakAuth = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
        val canDeviceCredential = biometricManager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL)
        
        return canStrongAuth == BiometricManager.BIOMETRIC_SUCCESS ||
               canWeakAuth == BiometricManager.BIOMETRIC_SUCCESS ||
               canDeviceCredential == BiometricManager.BIOMETRIC_SUCCESS
    }
    
    actual fun getBiometricType(): String? {
        val biometricManager = BiometricManager.from(context)
        val packageManager = context.packageManager
        
        // Priority: Check BIOMETRIC_STRONG first (face unlock on modern devices)
        val canStrongAuth = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        if (canStrongAuth == BiometricManager.BIOMETRIC_SUCCESS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (packageManager.hasSystemFeature(PackageManager.FEATURE_FACE)) {
                    return "Face ID"
                }
            }
            return "Face"
        }
        
        // Check for BIOMETRIC_WEAK (fingerprint)
        val canWeakAuth = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
        if (canWeakAuth == BiometricManager.BIOMETRIC_SUCCESS) {
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
                return "Fingerprint"
            }
            return "Fingerprint"
        }
        
        // Check for device credential as fallback
        val canDeviceCredential = biometricManager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL)
        if (canDeviceCredential == BiometricManager.BIOMETRIC_SUCCESS) {
            return "Device Credential"
        }
        
        return null
    }
    
    actual fun authenticate(
        title: String,
        subtitle: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onCancel: () -> Unit
    ) {
        // Get the current Activity from stored reference
        // BiometricPrompt requires FragmentActivity
        val activity = currentActivity
        if (activity == null) {
            logD("BiometricHelper", "No FragmentActivity available, cannot show biometric prompt")
            logD("BiometricHelper", "Make sure MainActivity is set as currentActivity")
            onError("Biometric authentication not available")
            return
        }
        
        logD("BiometricHelper", "Using Activity: ${activity.javaClass.simpleName}")
        
        val biometricManager = BiometricManager.from(context)
        
        // Determine which authenticators to allow based on available biometrics
        // Priority: Face → Fingerprint → Device Credential
        val biometricType = getBiometricType()
        val allowedAuthenticators = when {
            biometricType == "Face ID" || biometricType == "Face" -> {
                // Face available: try face first, fallback to device credential
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
            }
            biometricType == "Fingerprint" -> {
                // Fingerprint available: try fingerprint first, fallback to device credential
                BiometricManager.Authenticators.BIOMETRIC_WEAK or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
            }
            else -> {
                // No biometric available: only device credential (PIN/Pattern/Password)
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
            }
        }
        
        // Check if any authentication method is available
        val canAuthenticate = biometricManager.canAuthenticate(allowedAuthenticators)
        if (canAuthenticate != BiometricManager.BIOMETRIC_SUCCESS) {
            logD("BiometricHelper", "No authentication method available on this device")
            onError("Biometric authentication not available")
            return
        }
        
        val executor = ContextCompat.getMainExecutor(context)
        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    logD("BiometricHelper", "Biometric authentication succeeded")
                    onSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    logD(
                        "BiometricHelper",
                        "Biometric authentication error: $errorCode - $errString"
                    )
                    // Handle user cancellation errors
                    when (errorCode) {
                        ERROR_USER_CANCELED,
                        ERROR_NEGATIVE_BUTTON -> {
                            onCancel()
                        }
                        else -> {
                            onError(errString.toString())
                        }
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    logD("BiometricHelper", "Biometric authentication failed")
                    onError("Authentication failed. Please try again.")
                }
            }
        )
        
        val promptInfoBuilder = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
        
        // Only set negative button text if device credential is not allowed
        // (When device credential is allowed, Android shows "Use PIN" button automatically)
        if (!allowedAuthenticators.hasFlag(BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            promptInfoBuilder.setNegativeButtonText("Cancel")
        }
        
        promptInfoBuilder.setAllowedAuthenticators(allowedAuthenticators)
        val promptInfo = promptInfoBuilder.build()
        
        logD("BiometricHelper", "Showing biometric prompt: $title (Type: $biometricType)")
        biometricPrompt.authenticate(promptInfo)
    }
    
    /**
     * Helper extension function to check if flags contain a specific authenticator.
     */
    private fun Int.hasFlag(flag: Int): Boolean {
        return (this and flag) == flag
    }
}

actual fun createBiometricHelper(): BiometricHelper = BiometricHelper()

