package com.example.teachflow.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teachflow.data.model.Message
import com.example.teachflow.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() { // "Xài" ở đây

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    fun loadMessages(chatRoomId: String) {
        viewModelScope.launch {
            // Gọi hàm getMessages từ Repository để hứng Flow tin nhắn
            repository.getMessages(chatRoomId).collect { list ->
                _messages.value = list
            }
        }
    }

    fun sendMessage(chatRoomId: String, content: String, senderId: String, senderName: String, role: String) {
        val msg = Message(
            senderId = senderId,
            senderName = senderName,
            content = content,
            senderRole = role
        )
        viewModelScope.launch {
            // Gọi hàm gửi tin nhắn của Repository
            repository.sendMessage(chatRoomId, msg)
        }
    }
}