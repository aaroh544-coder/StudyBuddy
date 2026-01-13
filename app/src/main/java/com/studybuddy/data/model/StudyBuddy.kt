package com.studybuddy.data.model

data class StudyBuddy(
    val id: String,
    val name: String,
    val classLevel: String,
    val subjects: List<String>,
    val examType: String,
    val dailyStudyHours: Int,
    val profileImageUrl: String = "",
    val connectionStatus: ConnectionStatus = ConnectionStatus.NONE
)

enum class ConnectionStatus {
    NONE,
    PENDING_SENT,
    PENDING_RECEIVED,
    CONNECTED
}

// Mock data for demo purposes
object MockStudyBuddies {
    val buddies = listOf(
        StudyBuddy(
            id = "1",
            name = "Priya Sharma",
            classLevel = "Class 12",
            subjects = listOf("Physics", "Chemistry", "Mathematics"),
            examType = "JEE Main",
            dailyStudyHours = 6
        ),
        StudyBuddy(
            id = "2",
            name = "Rahul Verma",
            classLevel = "Class 12",
            subjects = listOf("Physics", "Chemistry", "Biology"),
            examType = "NEET",
            dailyStudyHours = 7
        ),
        StudyBuddy(
            id = "3",
            name = "Ananya Gupta",
            classLevel = "Class 11",
            subjects = listOf("Mathematics", "Physics"),
            examType = "JEE Advanced",
            dailyStudyHours = 5
        ),
        StudyBuddy(
            id = "4",
            name = "Arjun Patel",
            classLevel = "Class 10",
            subjects = listOf("Mathematics", "English", "Science"),
            examType = "Board Exams",
            dailyStudyHours = 4
        ),
        StudyBuddy(
            id = "5",
            name = "Sneha Reddy",
            classLevel = "Undergraduate",
            subjects = listOf("Computer Science", "Mathematics"),
            examType = "GATE",
            dailyStudyHours = 5
        ),
        StudyBuddy(
            id = "6",
            name = "Vikram Singh",
            classLevel = "Class 12",
            subjects = listOf("Physics", "Chemistry", "Mathematics"),
            examType = "JEE Main",
            dailyStudyHours = 8
        ),
        StudyBuddy(
            id = "7",
            name = "Kavya Nair",
            classLevel = "Class 11",
            subjects = listOf("Biology", "Chemistry"),
            examType = "NEET",
            dailyStudyHours = 6
        ),
        StudyBuddy(
            id = "8",
            name = "Aditya Kumar",
            classLevel = "Class 9",
            subjects = listOf("Mathematics", "Science", "English"),
            examType = "Board Exams",
            dailyStudyHours = 3
        )
    )
}
