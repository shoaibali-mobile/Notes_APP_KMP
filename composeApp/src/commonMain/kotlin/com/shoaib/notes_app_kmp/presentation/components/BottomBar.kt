package com.shoaib.notes_app_kmp.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.shoaib.notes_app_kmp.data.model.BottomBarConfig
import com.shoaib.notes_app_kmp.presentation.navigation.Screen

/**
 * Bottom navigation bar that displays items based on Remote Config.
 * Only shows items where the boolean value is true.
 */
@Composable
fun BottomBar(
    config: BottomBarConfig,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    // Build list of navigation items based on config
    val items = mutableListOf<BottomNavItem>()
    
    if (config.home) {
        items.add(BottomNavItem(
            route = Screen.Home.route,
            label = "Home",
            icon = Icons.Default.Home
        ))
    }
    
    if (config.settings) {
        items.add(BottomNavItem(
            route = Screen.Settings.route,
            label = "Settings",
            icon = Icons.Default.Settings
        ))
    }
    
    if (config.profile) {
        items.add(BottomNavItem(
            route = Screen.Profile.route,
            label = "Profile",
            icon = Icons.Default.Person
        ))
    }

    // Only show bottom bar if there are items to display
    if (items.isNotEmpty()) {
        NavigationBar(modifier = modifier) {
            items.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label) },
                    selected = currentRoute == item.route,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}

/**
 * Data class representing a bottom navigation item.
 */
data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

