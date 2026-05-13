package com.example.teachflow.data.repository

import com.example.teachflow.data.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatRepository(private val firestore: FirebaseFirestore) {

    // Lắng nghe tin nhắn Realtime từ Firestore
    fun getMessages(chatRoomId: String): Flow<List<Message>> = callbackFlow {
        val snapshotListener = firestore.collection("chat_rooms")
            .document(chatRoomId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                val messages = snapshot?.toObjects(Message::class.java) ?: emptyList()
                trySend(messages)
            }
        awaitClose { snapshotListener.remove() }
    }

    suspend fun sendMessage(chatRoomId: String, message: Message) {
        firestore.collection("chat_rooms")
            .document(chatRoomId)
            .collection("messages")
            .add(message)
            .await()
    }
}