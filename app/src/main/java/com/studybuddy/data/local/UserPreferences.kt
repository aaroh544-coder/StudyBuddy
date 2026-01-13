package com.studybuddy.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val context: Context) {
    
    private object PreferencesKeys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val IS_PROFILE_COMPLETE = booleanPreferencesKey("is_profile_complete")
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val LAST_STUDY_DATE = longPreferencesKey("last_study_date")
        val CURRENT_STREAK = intPreferencesKey("current_streak")
        val TOTAL_POMODORO_SESSIONS = intPreferencesKey("total_pomodoro_sessions")
        val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
    }
    
    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] ?: false
        }
    
    val isProfileComplete: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.IS_PROFILE_COMPLETE] ?: false
        }
    
    val userId: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.USER_ID] ?: ""
        }
    
    val userName: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.USER_NAME] ?: ""
        }
    
    val currentStreak: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.CURRENT_STREAK] ?: 0
        }
    
    val totalPomodoroSessions: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.TOTAL_POMODORO_SESSIONS] ?: 0
        }
    
    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] = isLoggedIn
        }
    }
    
    suspend fun setProfileComplete(isComplete: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_PROFILE_COMPLETE] = isComplete
        }
    }
    
    suspend fun setUserId(userId: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = userId
        }
    }
    
    suspend fun setUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
        }
    }
    
    suspend fun setCurrentStreak(streak: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.CURRENT_STREAK] = streak
        }
    }
    
    suspend fun incrementPomodoroSessions() {
        context.dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.TOTAL_POMODORO_SESSIONS] ?: 0
            preferences[PreferencesKeys.TOTAL_POMODORO_SESSIONS] = current + 1
        }
    }
    
    suspend fun updateLastStudyDate(date: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_STUDY_DATE] = date
        }
    }
    
    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
