package com.example.teachflow.data.model

data class Class(
    val id: String = "",
    val className: String = "",
    val subject: String = "",
    val academicYear: String = "",
    val teacherId: String = "",
    val room: String = "",
    val schedule: String = "",
    val studentCount: Int = 0
)