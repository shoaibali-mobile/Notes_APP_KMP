package com.shoaib.notes_app_kmp.data.model

import kotlinx.serialization.Serializable

/**
 * Data class representing the bottom bar configuration from Remote Config.
 */
@Serializable
data class BottomBarConfig(
    val home: Boolean = false,
    val settings: Boolean = false,
    val profile: Boolean = false
)

