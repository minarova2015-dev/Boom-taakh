package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_challenges")
data class DailyChallengeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val target: Int,
    val currentProgress: Int,
    val rewardCoins: Int,
    val isClaimed: Boolean = false
)
