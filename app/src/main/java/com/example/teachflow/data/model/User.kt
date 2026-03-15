package com.example.teachflow.data.model

data class User(
    val id: String = "",
    val email: String = "",
    val fullName: String = "",
    val role: String = "student", // "teacher" hoặc "student"
    val avatar: String = "",
    val phone: String = "",
    val studentCode: String = "",
    val className: String = "",
    val createdAt: Long = System.currentTimeMillis()
)