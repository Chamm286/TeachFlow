package com.example.teachflow.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.teachflow.data.model.Message

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    chatViewModel: ChatViewModel,
    chatRoomId: String,
    currentUserId: String,
    currentUserName: String,
    currentUserRole: String
) {
    val messages by chatViewModel.messages.collectAsState()
    var textState by remember { mutableStateOf("") }

    // Tự động load tin nhắn khi vào phòng
    LaunchedEffect(chatRoomId) {
        chatViewModel.loadMessages(chatRoomId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thảo luận: $chatRoomId") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            // Thanh nhập liệu tích hợp đẩy bàn phím
            Surface(tonalElevation = 3.dp, modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(8.dp).navigationBarsPadding().imePadding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = textState,
                        onValueChange = { textState = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Nhập tin nhắn...") },
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FloatingActionButton(
                        onClick = {
                            if (textState.isNotBlank()) {
                                chatViewModel.sendMessage(chatRoomId, textState, currentUserId, currentUserName, currentUserRole)
                                textState = ""
                            }
                        },
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send")
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 8.dp),
            reverseLayout = true // Tin nhắn mới nhất nằm dưới
        ) {
            items(messages) { message ->
                MessageItem(message = message, isMine = message.senderId == currentUserId)
            }
        }
    }
}

@Composable
fun MessageItem(message: Message, isMine: Boolean) {
    val alignment = if (isMine) Alignment.CenterEnd else Alignment.CenterStart
    val color = if (isMine) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer

    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), contentAlignment = alignment) {
        Column(horizontalAlignment = if (isMine) Alignment.End else Alignment.Start) {
            if (!isMine) {
                Text(text = message.senderName, style = MaterialTheme.typography.labelSmall)
            }
            Surface(
                color = color,
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 1.dp
            ) {
                Text(text = message.content, modifier = Modifier.padding(12.dp))
            }
        }
    }
}