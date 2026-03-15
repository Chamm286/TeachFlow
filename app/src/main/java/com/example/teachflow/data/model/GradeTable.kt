package com.example.teachflow.data.model

data class GradeTable(
    val classInfo: Class,
    val gradeColumns: List<GradeColumn>,
    val students: List<StudentWithScores>
)

data class StudentWithScores(
    val student: Student,
    val scores: Map<String, Float?>,
    val averageScore: Float? = null
)