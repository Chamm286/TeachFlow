package com.example.teachflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

// Auth
import com.example.teachflow.ui.auth.login.LoginScreen
import com.example.teachflow.ui.auth.login.LoginViewModel
import com.example.teachflow.ui.auth.register.RegisterScreen
import com.example.teachflow.ui.auth.register.RegisterViewModel
import com.example.teachflow.ui.auth.forgotpassword.ForgotPasswordScreen
import com.example.teachflow.ui.auth.forgotpassword.ForgotPasswordViewModel

// Welcome & Onboarding
import com.example.teachflow.ui.welcome.WelcomeScreen
import com.example.teachflow.ui.onboarding.OnboardingScreen
import com.example.teachflow.ui.splash.SplashScreen

// Teacher
import com.example.teachflow.ui.teacher.dashboard.TeacherDashboard
import com.example.teachflow.ui.teacher.classes.ClassesScreen
import com.example.teachflow.ui.teacher.grade.GradeInputScreen
import com.example.teachflow.ui.teacher.statistics.StatisticsScreen

// Student
import com.example.teachflow.ui.student.dashboard.StudentDashboard
import com.example.teachflow.ui.student.grades.GradesScreen
import com.example.teachflow.ui.student.profile.ProfileScreen

// Theme
import com.example.teachflow.ui.theme.TeachFlowTheme

val Context.dataStore by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TeachFlowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TeachFlowApp()
                }
            }
        }
    }
}

@Composable
fun TeachFlowApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val dataStore = context.dataStore
    var isFirstLaunch by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isFirstLaunch = runBlocking {
            val preferences = dataStore.data.first()
            preferences[booleanPreferencesKey("isFirstLaunch")] ?: true
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isFirstLaunch) "splash" else "login"
    ) {
        // Splash Screen
        composable("splash") {
            SplashScreen(
                onTimeout = {
                    if (isFirstLaunch) {
                        navController.navigate("welcome") {
                            popUpTo("splash") { inclusive = true }
                        }
                    } else {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            )
        }

        // Welcome Screen (NEW)
        composable("welcome") {
            WelcomeScreen(
                onGetStarted = {
                    navController.navigate("onboarding") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }

        // Onboarding
        composable("onboarding") {
            OnboardingScreen(
                onComplete = {
                    runBlocking {
                        dataStore.edit { settings ->
                            settings[booleanPreferencesKey("isFirstLaunch")] = false
                        }
                    }
                    navController.navigate("login") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        // Auth - Login
        composable("login") {
            val viewModel: LoginViewModel = viewModel()
            LoginScreen(
                onLoginSuccess = { userId, role ->
                    when (role) {
                        "teacher" -> navController.navigate("teacher_dashboard/$userId")
                        else -> navController.navigate("student_dashboard/$userId")
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onNavigateToForgotPassword = {
                    navController.navigate("forgot_password")
                },
                viewModel = viewModel
            )
        }

        // Auth - Register
        composable("register") {
            val viewModel: RegisterViewModel = viewModel()
            RegisterScreen(
                onRegisterSuccess = { userId, role ->
                    when (role) {
                        "teacher" -> navController.navigate("teacher_dashboard/$userId")
                        else -> navController.navigate("student_dashboard/$userId")
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        // Auth - Forgot Password
        composable("forgot_password") {
            val viewModel: ForgotPasswordViewModel = viewModel()
            ForgotPasswordScreen(
                onBack = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        // Teacher - Dashboard
        composable(
            route = "teacher_dashboard/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            TeacherDashboard(
                teacherId = userId,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("teacher_dashboard") { inclusive = true }
                    }
                },
                onNavigateToClasses = {
                    navController.navigate("teacher_classes/$userId")
                },
                onNavigateToGrade = {
                    navController.navigate("teacher_grade/$userId")
                },
                onNavigateToStatistics = {
                    navController.navigate("teacher_statistics/$userId")
                }
            )
        }

        // Teacher - Classes
        composable(
            route = "teacher_classes/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ClassesScreen(
                teacherId = userId,
                onBack = { navController.popBackStack() },
                onClassClick = { classId, className ->
                    navController.navigate("teacher_grade_detail/$classId/$className")
                }
            )
        }

        // Teacher - Grade Input
        composable(
            route = "teacher_grade/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            GradeInputScreen(
                teacherId = userId,
                classId = "",
                className = "",
                onBack = { navController.popBackStack() }
            )
        }

        // Teacher - Grade Detail
        composable(
            route = "teacher_grade_detail/{classId}/{className}",
            arguments = listOf(
                navArgument("classId") { type = NavType.StringType },
                navArgument("className") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val classId = backStackEntry.arguments?.getString("classId") ?: ""
            val className = backStackEntry.arguments?.getString("className") ?: ""
            GradeInputScreen(
                teacherId = "",
                classId = classId,
                className = className,
                onBack = { navController.popBackStack() }
            )
        }

        // Teacher - Statistics
        composable(
            route = "teacher_statistics/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            StatisticsScreen(
                teacherId = userId,
                onBack = { navController.popBackStack() }
            )
        }

        // Student - Dashboard
        composable(
            route = "student_dashboard/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            StudentDashboard(
                studentId = userId,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("student_dashboard") { inclusive = true }
                    }
                },
                onNavigateToGrades = {
                    navController.navigate("student_grades/$userId")
                },
                onNavigateToProfile = {
                    navController.navigate("student_profile/$userId")
                }
            )
        }

        // Student - Grades
        composable(
            route = "student_grades/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            GradesScreen(
                studentId = userId,
                onBack = { navController.popBackStack() }
            )
        }

        // Student - Profile
        composable(
            route = "student_profile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ProfileScreen(
                studentId = userId,
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("student_profile") { inclusive = true }
                    }
                }
            )
        }
    }
}