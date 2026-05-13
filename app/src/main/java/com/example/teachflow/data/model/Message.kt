package com.example.teachflow.data.model

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val senderRole: String = "" // "teacher" hoặc "student"
)