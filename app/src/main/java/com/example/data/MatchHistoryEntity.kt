package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match_history")
data class MatchHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val opponentName: String,
    val userSquadOvr: Int,
    val opponentSquadOvr: Int,
    val userScore: Int,
    val opponentScore: Int,
    val won: Boolean,
    val penaltiesScored: Int,
    val coinsEarned: Int,
    val dateMillis: Long = System.currentTimeMillis()
)
