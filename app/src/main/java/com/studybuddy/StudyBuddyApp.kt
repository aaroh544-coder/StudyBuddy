package com.studybuddy

import android.app.Application
import androidx.room.Room
import com.studybuddy.data.local.AppDatabase
import com.studybuddy.data.local.UserPreferences

class StudyBuddyApp : Application() {
    
    lateinit var database: AppDatabase
        private set
    
    lateinit var userPreferences: UserPreferences
        private set
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // Initialize Room Database
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
        
        // Initialize DataStore Preferences
        userPreferences = UserPreferences(applicationContext)
    }
    
    companion object {
        lateinit var instance: StudyBuddyApp
            private set
    }
}
