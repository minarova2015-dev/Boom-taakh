package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE isLoggedIn = 1 LIMIT 1")
    fun getActiveUserProfile(): Flow<UserEntity?>

    @Query("SELECT * FROM user_profile WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getActiveUserProfileSync(): UserEntity?

    @Query("SELECT * FROM user_profile WHERE LOWER(username) = LOWER(:username) AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): UserEntity?

    @Query("SELECT * FROM user_profile WHERE LOWER(username) = LOWER(:username) LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Query("SELECT * FROM user_profile ORDER BY id DESC")
    fun getAllAccounts(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE user_profile SET isLoggedIn = 0")
    suspend fun logoutAll()

    @Query("UPDATE user_profile SET isLoggedIn = 1, lastSavedTimestamp = :now WHERE id = :id")
    suspend fun setActiveUser(id: Long, now: Long = System.currentTimeMillis())

    @Query("DELETE FROM user_profile")
    suspend fun deleteAll()
}

@Dao
interface MatchHistoryDao {
    @Query("SELECT * FROM match_history ORDER BY dateMillis DESC LIMIT 30")
    fun getAllMatches(): Flow<List<MatchHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: MatchHistoryEntity): Long
}

@Dao
interface DailyChallengeDao {
    @Query("SELECT * FROM daily_challenges")
    fun getAllChallenges(): Flow<List<DailyChallengeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenges(challenges: List<DailyChallengeEntity>)

    @Update
    suspend fun updateChallenge(challenge: DailyChallengeEntity)
}
