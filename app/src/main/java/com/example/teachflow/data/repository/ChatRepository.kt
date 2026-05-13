package com.example.teachflow.data.repository

import com.example.teachflow.data.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ChatRepository(private val firestore: FirebaseFirestore) {

    // 1. Hàm lắng nghe tin nhắn Realtime
    fun getMessages(roomId: String): Flow<List<Message>> = callbackFlow {
        val listener = firestore.collection("chats")
            .document(roomId)
            .collection("messages")
            // Sắp xếp giảm dần để tin nhắn mới nhất nằm trên cùng danh sách (phù hợp với reverseLayout)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents?.mapNotNull { it.toObject(Message::class.java) } ?: emptyList()
                trySend(messages)
            }

        // Hủy lắng nghe khi thoát màn hình chat
        awaitClose { listener.remove() }
    }

    // 2. Hàm gửi tin nhắn
    fun sendMessage(roomId: String, message: Message) {
        firestore.collection("chats")
            .document(roomId)
            .collection("messages")
            .add(message)
    }
}