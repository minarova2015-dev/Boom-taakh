package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val email: String = "",
    val password: String = "123456",
    val clubName: String,
    val avatarEmoji: String = "⚽",
    val careerCoins: Int = 500,
    val careerWins: Int = 0,
    val careerLosses: Int = 0,
    val highestSquadOvr: Int = 0,
    val bribesAttempted: Int = 0,
    val bribesSuccessful: Int = 0,
    val isLoggedIn: Boolean = true,
    val lastSavedTimestamp: Long = System.currentTimeMillis()
)
