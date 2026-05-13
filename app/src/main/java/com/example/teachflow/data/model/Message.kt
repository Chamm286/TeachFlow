package com.example.teachflow.data.model

data class Message(
    val senderId: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
)