package com.example.teachflow.navigation

sealed class Screen(val route: String) {
    // Onboarding & Auth
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    
    // Main Dashboard (chung)
    object MainDashboard : Screen("main_dashboard")
    
    // Common
    object Settings : Screen("settings")
    object About : Screen("about")
    
    // Student
    object StudentDashboard : Screen("student_dashboard")
    object StudentGrades : Screen("student_grades")
    object StudentProfile : Screen("student_profile")
    
    // Teacher
    object TeacherDashboard : Screen("teacher_dashboard")
    object TeacherClasses : Screen("teacher_classes")
    object TeacherGradeInput : Screen("teacher_grade_input")
    object TeacherStatistics : Screen("teacher_statistics")
}
