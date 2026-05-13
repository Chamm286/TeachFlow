package com.example.teachflow.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teachflow.data.model.Message
import com.example.teachflow.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    fun loadMessages(roomId: String) {
        viewModelScope.launch {
            repository.getMessages(roomId).collect { list ->
                _messages.value = list
            }
        }
    }

    fun sendMessage(roomId: String, content: String, senderId: String) {
        if (content.isBlank()) return

        val msg = Message(
            senderId = senderId,
            content = content,
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            repository.sendMessage(roomId, msg)
        }
    }

    // Thuật toán tạo ID Phòng Chat duy nhất cho 2 người (Rất Quan Trọng)
    fun getRoomId(userId1: String, userId2: String): String {
        return if (userId1 < userId2) {
            "${userId1}_$userId2"
        } else {
            "${userId2}_$userId1"
        }
    }
}