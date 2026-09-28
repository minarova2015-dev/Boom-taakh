package com.example.model

enum class GameMode(val title: String) {
    SOLO_AI("Solo vs Smart AI"),
    ONLINE_MULTIPLAYER("Online Room & Friends"),
    LOCAL_PASS_AND_PLAY("Pass & Play (2-Players)")
}

enum class AIDifficulty(
    val title: String,
    val description: String,
    val budgetMillions: Int,
    val bidAggressiveness: Int
) {
    ROOKIE("Rookie AI", "Casual bidder, conservative budget (€110M)", 110, 1),
    PRO("Pro AI", "Balanced & strategic bids (€140M)", 140, 2),
    WORLD_CLASS("World Class AI", "High-pressure bidding on top stars (€160M)", 160, 3),
    LEGEND("Legend AI", "Relentless auction master with deep pockets (€185M)", 185, 4)
}

enum class AuctionPhase {
    BIDDING_ACTIVE,
    SOLD_BOOM_TAAKH,
    BETWEEN_ROUNDS,
    FINAL_SHOWDOWN
}

data class Bidder(
    val id: String,
    val name: String,
    val isUser: Boolean,
    val avatarEmoji: String
)

data class ChatMessage(
    val senderName: String,
    val text: String,
    val isVoiceTranscript: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

data class ActiveAuctionState(
    val currentRound: Int = 1,
    val totalRounds: Int = 11,
    val targetSlotId: String = "slot_st",
    val targetPosition: Position = Position.ST,
    val mysteryPlayer: Player,
    val currentBidMillions: Int = 5,
    val highestBidder: Bidder,
    val secondsLeft: Int = 10,
    val phase: AuctionPhase = AuctionPhase.BIDDING_ACTIVE,
    val revealedHints: List<HintCard> = emptyList(),
    val activePowerUps: List<PowerUpCard> = emptyList(),
    val refereeBribeResult: RefereeBribeResult? = null,
    val userBudgetMillions: Int = 140,
    val opponentBudgetMillions: Int = 140,
    val userSquad: TeamSquad = TeamSquad("My Club", isUserTeam = true),
    val opponentSquad: TeamSquad = TeamSquad("Rival FC", isUserTeam = false),
    val refereeAnnouncementText: String = "",
    val isBidFreezeActive: Boolean = false,
    val gameMode: GameMode = GameMode.SOLO_AI,
    val aiDifficulty: AIDifficulty = AIDifficulty.PRO,
    val roomCode: String = "BT-8291"
)

data class MatchShowdownResult(
    val userSquadOvr: Int,
    val opponentSquadOvr: Int,
    val userGoals: Int,
    val opponentGoals: Int,
    val penaltyGoalsFromBribe: Int,
    val winnerName: String,
    val isUserWinner: Boolean,
    val coinsEarned: Int,
    val winsEarned: Int
)
