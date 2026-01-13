package com.studybuddy.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object ProfileSetup : Screen("profile_setup")
    object Home : Screen("home")
    object FindBuddy : Screen("find_buddy")
    object Timer : Screen("timer")
    object Progress : Screen("progress")
    object Profile : Screen("profile")
    object StudyRoom : Screen("study_room/{buddyId}/{buddyName}") {
        fun createRoute(buddyId: String, buddyName: String) = "study_room/$buddyId/$buddyName"
    }
    object EditProfile : Screen("edit_profile")
}
