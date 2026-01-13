package com.studybuddy.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.studybuddy.ui.components.BottomNavBar
import com.studybuddy.ui.screens.*

@Composable
fun StudyBuddyNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Splash.route
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // Routes where bottom nav should be shown
    val bottomNavRoutes = listOf(
        Screen.Home.route,
        Screen.FindBuddy.route,
        Screen.Timer.route,
        Screen.Progress.route,
        Screen.Profile.route
    )
    
    val showBottomNav = currentRoute in bottomNavRoutes
    
    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                BottomNavBar(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues),
            enterTransition = {
                fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                    initialOffsetX = { 300 },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                    initialOffsetX = { -300 },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                    targetOffsetX = { 300 },
                    animationSpec = tween(300)
                )
            }
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.ProfileSetup.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Screen.ProfileSetup.route) {
                ProfileSetupScreen(
                    onSetupComplete = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.ProfileSetup.route) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToTimer = {
                        navController.navigate(Screen.Timer.route)
                    },
                    onNavigateToFindBuddy = {
                        navController.navigate(Screen.FindBuddy.route)
                    },
                    onNavigateToProgress = {
                        navController.navigate(Screen.Progress.route)
                    }
                )
            }
            
            composable(Screen.FindBuddy.route) {
                FindBuddyScreen(
                    onBuddyClick = { buddyId, buddyName ->
                        navController.navigate(Screen.StudyRoom.createRoute(buddyId, buddyName))
                    }
                )
            }
            
            composable(Screen.Timer.route) {
                TimerScreen()
            }
            
            composable(Screen.Progress.route) {
                ProgressScreen()
            }
            
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onEditProfile = {
                        navController.navigate(Screen.EditProfile.route)
                    }
                )
            }
            
            composable(
                route = Screen.StudyRoom.route,
                arguments = listOf(
                    navArgument("buddyId") { type = NavType.StringType },
                    navArgument("buddyName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val buddyId = backStackEntry.arguments?.getString("buddyId") ?: ""
                val buddyName = backStackEntry.arguments?.getString("buddyName") ?: ""
                StudyRoomScreen(
                    buddyId = buddyId,
                    buddyName = buddyName,
                    onBack = { navController.popBackStack() }
                )
            }
            
            composable(Screen.EditProfile.route) {
                EditProfileScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
