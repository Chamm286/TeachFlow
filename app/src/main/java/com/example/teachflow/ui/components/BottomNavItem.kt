package com.example.teachflow.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Dashboard : BottomNavItem(
        route = "dashboard",
        title = "Tổng quan",
        icon = Icons.Default.Home,
        selectedIcon = Icons.Default.Home
    )
    
    object Classes : BottomNavItem(
        route = "classes",
        title = "Lớp học",
        icon = Icons.Default.School,
        selectedIcon = Icons.Default.School
    )
    
    object Grades : BottomNavItem(
        route = "grades",
        title = "Điểm số",
        icon = Icons.Default.Grade,
        selectedIcon = Icons.Default.Grade
    )
    
    object Profile : BottomNavItem(
        route = "profile",
        title = "Cá nhân",
        icon = Icons.Default.Person,
        selectedIcon = Icons.Default.Person
    )
    
    object Settings : BottomNavItem(
        route = "settings",
        title = "Cài đặt",
        icon = Icons.Default.Settings,
        selectedIcon = Icons.Default.Settings
    )
}
