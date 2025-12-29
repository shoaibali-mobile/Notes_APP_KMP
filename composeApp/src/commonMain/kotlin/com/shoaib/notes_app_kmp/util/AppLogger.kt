package com.shoaib.notes_app_kmp.util

/**
 * Global Logger object for application-wide logging.
 * 
 * This object provides a convenient way to log messages throughout the app.
 * It uses the platform-specific logD implementation (Android Log.d, iOS NSLog).
 * 
 * Usage:
 * ```
 * AppLogger.d("MyTag", "This is a debug message")
 * AppLogger.d("Short message") // Uses default tag
 * ```
 */
object AppLogger {
    private const val DEFAULT_TAG = "NotesApp"
    
    /**
     * Log a debug message.
     * 
     * @param tag The tag to identify the source of the log message
     * @param message The message to log
     */
    fun d(tag: String = DEFAULT_TAG, message: String) {
        logD(tag, message)
    }
    
    /**
     * Log a debug message with default tag.
     * Convenience method for common logging.
     * 
     * @param message The message to log
     */
    fun d(message: String) {
        logD(DEFAULT_TAG, message)
    }
}
