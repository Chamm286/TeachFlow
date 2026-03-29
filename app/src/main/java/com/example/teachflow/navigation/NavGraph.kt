package com.example.teachflow.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.teachflow.ui.splash.SplashScreen
import com.example.teachflow.ui.onboarding.OnboardingScreen
import com.example.teachflow.ui.welcome.WelcomeScreen
import com.example.teachflow.ui.auth.login.LoginScreen
import com.example.teachflow.ui.auth.register.RegisterScreen
import com.example.teachflow.ui.auth.forgotpassword.ForgotPasswordScreen
import com.example.teachflow.ui.main.MainDashboard
import com.example.teachflow.ui.student.dashboard.StudentDashboard
import com.example.teachflow.ui.teacher.dashboard.TeacherDashboard
import com.example.teachflow.ui.settings.SettingsScreen
import com.example.teachflow.ui.about.AboutScreen

@Composable
fun NavGraph(
    startDestination: String = Screen.Splash.route
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash -> Onboarding
        composable(Screen.Splash.route) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Onboarding -> MainDashboard
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Screen.MainDashboard.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // Main Dashboard
        composable(Screen.MainDashboard.route) {
            MainDashboard(navController = navController)
        }

        // Welcome
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // Login
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { role ->
                    if (role == "teacher") {
                        navController.navigate(Screen.TeacherDashboard.route) {
                            popUpTo(Screen.MainDashboard.route) { inclusive = false }
                        }
                    } else {
                        navController.navigate(Screen.StudentDashboard.route) {
                            popUpTo(Screen.MainDashboard.route) { inclusive = false }
                        }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }

        // Register
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigateUp()
                }
            )
        }

        // ForgotPassword
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController)
        }

        // Student Dashboard
        composable(Screen.StudentDashboard.route) {
            StudentDashboard(navController = navController)
        }

        // Teacher Dashboard
        composable(Screen.TeacherDashboard.route) {
            TeacherDashboard(navController = navController)
        }

        // Settings
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }

        // About
        composable(Screen.About.route) {
            AboutScreen(navController = navController)
        }
    }
}
