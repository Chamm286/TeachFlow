package com.example.teachflow.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.teachflow.data.repository.ChatRepository
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
import com.example.teachflow.ui.chat.ChatScreen
import com.example.teachflow.ui.chat.ChatViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

        // Class Detail
        composable(
            route = Screen.ClassDetail.route,
            arguments = listOf(
                androidx.navigation.navArgument("title") { type = androidx.navigation.NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            com.example.teachflow.ui.course.CourseDetailScreen(
                navController = navController,
                courseTitle = title
            )
        }

        // Chat
        composable(
            route = Screen.Chat.route,
            arguments = listOf(
                navArgument("partnerUserId") { type = NavType.StringType },
                navArgument("partnerName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // 1. Lấy dữ liệu từ đường dẫn
            val partnerUserId = backStackEntry.arguments?.getString("partnerUserId") ?: ""
            val partnerName = backStackEntry.arguments?.getString("partnerName") ?: "Người dùng"

            // 2. Khởi tạo Repository làm việc với Firebase
            val firestore = FirebaseFirestore.getInstance()
            val chatRepository = ChatRepository(firestore)

            // 3. Khởi tạo ViewModel chuyên nghiệp có chứa Repository
            val chatViewModel: ChatViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ChatViewModel(chatRepository) as T
                    }
                }
            )

            // 4. Lấy ID của chính mình (người đang cầm máy)
            val myUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

            // 5. Gọi giao diện Chat
            ChatScreen(
                navController = navController,
                chatViewModel = chatViewModel,
                myUserId = myUserId,
                partnerUserId = partnerUserId,
                partnerName = partnerName
            )
        }
    }
}
