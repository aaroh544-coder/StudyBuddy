package com.studybuddy.data.local

import androidx.room.*
import com.studybuddy.data.model.Message
import com.studybuddy.data.model.StudySession
import com.studybuddy.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: String): Flow<User?>
    
    @Query("SELECT * FROM users LIMIT 1")
    fun getCurrentUser(): Flow<User?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
    
    @Update
    suspend fun updateUser(user: User)
    
    @Delete
    suspend fun deleteUser(user: User)
    
    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}

@Dao
interface StudySessionDao {
    @Query("SELECT * FROM study_sessions WHERE userId = :userId ORDER BY date DESC")
    fun getSessionsByUser(userId: String): Flow<List<StudySession>>
    
    @Query("SELECT * FROM study_sessions WHERE userId = :userId AND date >= :startDate AND date <= :endDate")
    fun getSessionsInRange(userId: String, startDate: Long, endDate: Long): Flow<List<StudySession>>
    
    @Query("SELECT SUM(durationMinutes) FROM study_sessions WHERE userId = :userId")
    fun getTotalStudyMinutes(userId: String): Flow<Int?>
    
    @Query("SELECT SUM(durationMinutes) FROM study_sessions WHERE userId = :userId AND date >= :startDate")
    fun getStudyMinutesSince(userId: String, startDate: Long): Flow<Int?>
    
    @Insert
    suspend fun insertSession(session: StudySession)
    
    @Query("DELETE FROM study_sessions WHERE userId = :userId")
    suspend fun deleteAllSessions(userId: String)
}

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getMessagesByChat(chatId: String): Flow<List<Message>>
    
    @Insert
    suspend fun insertMessage(message: Message)
    
    @Query("UPDATE messages SET isRead = 1 WHERE chatId = :chatId")
    suspend fun markAsRead(chatId: String)
    
    @Query("DELETE FROM messages WHERE chatId = :chatId")
    suspend fun deleteChat(chatId: String)
}

@Database(
    entities = [User::class, StudySession::class, Message::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun messageDao(): MessageDao
    
    companion object {
        const val DATABASE_NAME = "study_buddy_db"
    }
}
