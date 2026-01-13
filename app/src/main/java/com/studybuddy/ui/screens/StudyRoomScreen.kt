package com.studybuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studybuddy.data.model.Message
import com.studybuddy.data.model.MockMessages
import com.studybuddy.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyRoomScreen(
    buddyId: String,
    buddyName: String,
    onBack: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(MockMessages.getMessages(buddyId)) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(listOf(PrimaryLight, Primary))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = buddyName.first().toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = buddyName,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Online",
                            fontSize = 12.sp,
                            color = AccentGreen
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { /* Video call */ }) {
                    Icon(Icons.Filled.Videocam, contentDescription = "Video Call")
                }
                IconButton(onClick = { /* More options */ }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "More")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Surface
            )
        )
        
        HorizontalDivider(color = SurfaceVariant)
        
        // Messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    isMe = message.senderId == "me"
                )
            }
        }
        
        // Input Area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Attachment Button
            IconButton(
                onClick = { /* Open attachment picker */ }
            ) {
                Icon(
                    imageVector = Icons.Filled.AttachFile,
                    contentDescription = "Attach",
                    tint = TextSecondary
                )
            }
            
            // Text Input
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { 
                    Text(
                        "Type a message...",
                        color = TextHint
                    ) 
                },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(max = 120.dp),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = SurfaceVariant,
                    focusedBorderColor = Primary,
                    unfocusedContainerColor = SurfaceVariant,
                    focusedContainerColor = SurfaceVariant
                ),
                maxLines = 4
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Send Button
            FilledIconButton(
                onClick = {
                    if (messageText.isNotBlank()) {
                        val newMessage = Message(
                            id = System.currentTimeMillis(),
                            chatId = buddyId,
                            senderId = "me",
                            senderName = "You",
                            content = messageText,
                            timestamp = System.currentTimeMillis()
                        )
                        messages = messages + newMessage
                        messageText = ""
                        
                        coroutineScope.launch {
                            listState.animateScrollToItem(messages.size - 1)
                        }
                    }
                },
                modifier = Modifier.size(48.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Primary
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: Message,
    isMe: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMe) 16.dp else 4.dp,
                        bottomEnd = if (isMe) 4.dp else 16.dp
                    )
                )
                .background(
                    if (isMe) Primary else Surface
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.content,
                color = if (isMe) Color.White else TextPrimary,
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = formatTime(message.timestamp),
                color = if (isMe) Color.White.copy(alpha = 0.7f) else TextHint,
                fontSize = 10.sp,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

private fun formatTime(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    val minutes = diff / 60000
    val hours = diff / 3600000
    
    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "${minutes}m ago"
        hours < 24 -> "${hours}h ago"
        else -> "${hours / 24}d ago"
    }
}
