package com.studybuddy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val chatId: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val messageType: MessageType = MessageType.TEXT,
    val attachmentUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

enum class MessageType {
    TEXT,
    IMAGE,
    PDF,
    SYSTEM
}

data class Chat(
    val id: String,
    val participants: List<String>,
    val participantNames: List<String>,
    val lastMessage: String,
    val lastMessageTime: Long,
    val unreadCount: Int = 0
)

// Mock messages for demo
object MockMessages {
    fun getMessages(chatId: String) = listOf(
        Message(
            id = 1,
            chatId = chatId,
            senderId = "other",
            senderName = "Study Buddy",
            content = "Hey! Ready to study together?",
            timestamp = System.currentTimeMillis() - 3600000
        ),
        Message(
            id = 2,
            chatId = chatId,
            senderId = "me",
            senderName = "You",
            content = "Yes! Let's start with Physics today.",
            timestamp = System.currentTimeMillis() - 3500000
        ),
        Message(
            id = 3,
            chatId = chatId,
            senderId = "other",
            senderName = "Study Buddy",
            content = "Perfect! I was thinking we could cover Newton's Laws.",
            timestamp = System.currentTimeMillis() - 3400000
        ),
        Message(
            id = 4,
            chatId = chatId,
            senderId = "me",
            senderName = "You",
            content = "Sounds great! I have some notes I can share.",
            timestamp = System.currentTimeMillis() - 3300000
        ),
        Message(
            id = 5,
            chatId = chatId,
            senderId = "other",
            senderName = "Study Buddy",
            content = "Awesome! Let's use the Pomodoro technique - 25 mins study, 5 mins break?",
            timestamp = System.currentTimeMillis() - 3200000
        )
    )
}
