package com.shoaib.notes_app_kmp

actual class PlatformContext

actual fun getPlatformContext(): PlatformContext {
    return PlatformContext()
}

actual fun isPlatformContextInitialized(): Boolean {
    return true  // iOS doesn't need initialization check
}

