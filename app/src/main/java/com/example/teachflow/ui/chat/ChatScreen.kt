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
    myUserId: String,       // ID của tài khoản đang đăng nhập
    partnerUserId: String,  // ID của người mình đang muốn chat cùng
    partnerName: String     // Tên người kia để hiển thị lên thanh tiêu đề
) {
    val messages by chatViewModel.messages.collectAsState()
    var textState by remember { mutableStateOf("") }

    // Tự động tạo roomId và tải tin nhắn
    val roomId = remember { chatViewModel.getRoomId(myUserId, partnerUserId) }

    LaunchedEffect(roomId) {
        chatViewModel.loadMessages(roomId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(partnerName) }, // Hiện tên người chat cùng
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 3.dp, modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .navigationBarsPadding()
                        .imePadding(),
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
                                chatViewModel.sendMessage(roomId, textState, myUserId)
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
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp),
            reverseLayout = true // Lật ngược danh sách để tin nhắn mới trồi lên từ dưới
        ) {
            items(messages) { message ->
                MessageBubble(message = message, isMine = message.senderId == myUserId)
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message, isMine: Boolean) {
    val alignment = if (isMine) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor = if (isMine) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (isMine) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Surface(
            color = bubbleColor,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isMine) 16.dp else 4.dp,
                bottomEnd = if (isMine) 4.dp else 16.dp
            ),
            shadowElevation = 1.dp
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                color = textColor
            )
        }
    }
}