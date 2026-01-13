package com.studybuddy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_sessions")
data class StudySession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val date: Long = System.currentTimeMillis(),
    val durationMinutes: Int,
    val sessionType: SessionType = SessionType.POMODORO,
    val subject: String = ""
)

enum class SessionType {
    POMODORO,
    FREE_STUDY,
    GROUP_STUDY
}

data class DailyStudyStats(
    val date: Long,
    val totalMinutes: Int,
    val sessionCount: Int
)

data class WeeklyStudyStats(
    val weekStart: Long,
    val dailyStats: List<DailyStudyStats>,
    val totalMinutes: Int
)
