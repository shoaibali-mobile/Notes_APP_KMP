package com.shoaib.notes_app_kmp.util

import platform.Foundation.NSLog

actual fun logD(tag: String, message: String) {
    NSLog("$tag: $message")
}

