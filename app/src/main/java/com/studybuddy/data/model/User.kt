package com.studybuddy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.studybuddy.data.local.Converters

@Entity(tableName = "users")
@TypeConverters(Converters::class)
data class User(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val classLevel: String = "",
    val subjects: List<String> = emptyList(),
    val examType: String = "",
    val dailyStudyHours: Int = 0,
    val totalStudyMinutes: Long = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val badges: List<String> = emptyList(),
    val profileImageUrl: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val lastStudyDate: Long = 0
)

data class UserState(
    val isLoggedIn: Boolean = false,
    val isProfileComplete: Boolean = false,
    val currentUser: User? = null
)
