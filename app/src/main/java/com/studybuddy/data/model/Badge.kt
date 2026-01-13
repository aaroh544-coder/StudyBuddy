package com.studybuddy.data.model

data class Badge(
    val id: String,
    val name: String,
    val description: String,
    val iconName: String,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null
)

object BadgeDefinitions {
    val allBadges = listOf(
        Badge(
            id = "first_session",
            name = "First Step",
            description = "Complete your first study session",
            iconName = "star"
        ),
        Badge(
            id = "streak_3",
            name = "Getting Started",
            description = "Maintain a 3-day streak",
            iconName = "local_fire_department"
        ),
        Badge(
            id = "streak_7",
            name = "Week Warrior",
            description = "Maintain a 7-day streak",
            iconName = "whatshot"
        ),
        Badge(
            id = "streak_30",
            name = "Monthly Master",
            description = "Maintain a 30-day streak",
            iconName = "military_tech"
        ),
        Badge(
            id = "hours_10",
            name = "Dedicated Learner",
            description = "Study for 10 total hours",
            iconName = "school"
        ),
        Badge(
            id = "hours_50",
            name = "Knowledge Seeker",
            description = "Study for 50 total hours",
            iconName = "psychology"
        ),
        Badge(
            id = "hours_100",
            name = "Study Champion",
            description = "Study for 100 total hours",
            iconName = "emoji_events"
        ),
        Badge(
            id = "early_bird",
            name = "Early Bird",
            description = "Start a session before 6 AM",
            iconName = "wb_sunny"
        ),
        Badge(
            id = "night_owl",
            name = "Night Owl",
            description = "Study after 10 PM",
            iconName = "nightlight"
        ),
        Badge(
            id = "pomodoro_10",
            name = "Pomodoro Pro",
            description = "Complete 10 Pomodoro sessions",
            iconName = "timer"
        ),
        Badge(
            id = "buddy_first",
            name = "Social Learner",
            description = "Connect with your first study buddy",
            iconName = "people"
        ),
        Badge(
            id = "buddy_5",
            name = "Study Network",
            description = "Connect with 5 study buddies",
            iconName = "groups"
        )
    )
}
