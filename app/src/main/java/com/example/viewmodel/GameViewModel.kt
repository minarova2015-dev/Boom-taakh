package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundManager
import com.example.data.AppDatabase
import com.example.data.BoomTaakhRepository
import com.example.data.DailyChallengeEntity
import com.example.data.MatchHistoryEntity
import com.example.data.UserEntity
import com.example.model.AIDifficulty
import com.example.model.ActiveAuctionState
import com.example.model.AppLanguage
import com.example.model.AuctionPhase
import com.example.model.Bidder
import com.example.model.CardProgression
import com.example.model.ChatMessage
import com.example.model.GameMode
import com.example.model.HintCard
import com.example.model.HintType
import com.example.model.MatchShowdownResult
import com.example.model.Player
import com.example.model.PlayerDatabase
import com.example.model.Position
import com.example.model.PowerUpCard
import com.example.model.PowerUpType
import com.example.model.RefereeBribeResult
import com.example.model.RefereeManager
import com.example.model.SquadSlot
import com.example.model.TeamSquad
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    AUCTION_ROOM,
    MATCH_SHOWDOWN,
    MULTIPLAYER_LOBBY,
    LEADERBOARDS,
    DAILY_CHALLENGES,
    CARDS_PROGRESSION,
    LOGIN_PROFILE,
    AUTH_SCREEN
}

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = BoomTaakhRepository(
        database.userDao(),
        database.matchHistoryDao(),
        database.dailyChallengeDao()
    )
    val soundManager = SoundManager(application)

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.EN)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _userProfile = MutableStateFlow<UserEntity?>(null)
    val userProfile: StateFlow<UserEntity?> = _userProfile.asStateFlow()

    private val _allAccounts = MutableStateFlow<List<UserEntity>>(emptyList())
    val allAccounts: StateFlow<List<UserEntity>> = _allAccounts.asStateFlow()

    private val _dailyChallenges = MutableStateFlow<List<DailyChallengeEntity>>(emptyList())
    val dailyChallenges: StateFlow<List<DailyChallengeEntity>> = _dailyChallenges.asStateFlow()

    private val _matchHistory = MutableStateFlow<List<MatchHistoryEntity>>(emptyList())
    val matchHistory: StateFlow<List<MatchHistoryEntity>> = _matchHistory.asStateFlow()

    private val _auctionState = MutableStateFlow<ActiveAuctionState?>(null)
    val auctionState: StateFlow<ActiveAuctionState?> = _auctionState.asStateFlow()

    private val _showdownResult = MutableStateFlow<MatchShowdownResult?>(null)
    val showdownResult: StateFlow<MatchShowdownResult?> = _showdownResult.asStateFlow()

    // Voice Chat State
    private val _isVoiceChatMuted = MutableStateFlow(false)
    val isVoiceChatMuted: StateFlow<Boolean> = _isVoiceChatMuted.asStateFlow()

    private val _voiceChatActiveSpeaker = MutableStateFlow<String?>(null)
    val voiceChatActiveSpeaker: StateFlow<String?> = _voiceChatActiveSpeaker.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    // Dev unlock override for testing 15-win hints and 30-win power-ups
    private val _devUnlockAllCards = MutableStateFlow(false)
    val devUnlockAllCards: StateFlow<Boolean> = _devUnlockAllCards.asStateFlow()

    // Status message toast / banner for errors like "Not enough coins!" or "Already highest bidder!"
    private val _actionFeedback = MutableStateFlow<String?>(null)
    val actionFeedback: StateFlow<String?> = _actionFeedback.asStateFlow()

    private var timerJob: Job? = null
    private var aiBidJob: Job? = null

    // Order of 11 positions for a full squad match
    private val roundPositions = listOf(
        "slot_st" to Position.ST,
        "slot_lw" to Position.LW,
        "slot_rw" to Position.RW,
        "slot_cam" to Position.CAM,
        "slot_cm" to Position.CM,
        "slot_cdm" to Position.CDM,
        "slot_lb" to Position.LB,
        "slot_cb1" to Position.CB,
        "slot_cb2" to Position.CB,
        "slot_rb" to Position.RB,
        "slot_gk" to Position.GK
    )

    init {
        viewModelScope.launch {
            val user = repository.getOrSeedUser()
            _userProfile.value = user
            repository.userProfile.collect { updated ->
                if (updated != null) {
                    _userProfile.value = updated
                }
            }
        }
        viewModelScope.launch {
            repository.dailyChallenges.collect { list ->
                _dailyChallenges.value = list
            }
        }
        viewModelScope.launch {
            repository.matchHistory.collect { list ->
                _matchHistory.value = list
            }
        }
        viewModelScope.launch {
            repository.allAccounts.collect { list ->
                _allAccounts.value = list
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun login(username: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            when (val res = repository.login(username, password)) {
                is com.example.data.AuthResult.Success -> {
                    _userProfile.value = res.user
                    _currentScreen.value = AppScreen.HOME
                    soundManager.playWhistle()
                    onResult(true, "Welcome back, @${res.user.username}!")
                }
                is com.example.data.AuthResult.Error -> {
                    onResult(false, res.message)
                }
            }
        }
    }

    fun signUp(
        username: String,
        email: String,
        password: String,
        clubName: String,
        avatarEmoji: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            when (val res = repository.signUp(username, email, password, clubName, avatarEmoji)) {
                is com.example.data.AuthResult.Success -> {
                    _userProfile.value = res.user
                    _currentScreen.value = AppScreen.HOME
                    soundManager.playWhistle()
                    onResult(true, "Account created! Welcome to Boom Taakh, @${res.user.username}!")
                }
                is com.example.data.AuthResult.Error -> {
                    onResult(false, res.message)
                }
            }
        }
    }

    fun continueAsGuest() {
        viewModelScope.launch {
            val user = repository.getOrSeedUser()
            _userProfile.value = user
            _currentScreen.value = AppScreen.HOME
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _userProfile.value = null
            _currentScreen.value = AppScreen.AUTH_SCREEN
        }
    }

    fun switchAccount(userId: Long) {
        viewModelScope.launch {
            val user = repository.switchAccount(userId)
            if (user != null) {
                _userProfile.value = user
                _currentScreen.value = AppScreen.HOME
            }
        }
    }

    fun manualSaveGame(onSaved: (String) -> Unit) {
        viewModelScope.launch {
            val time = repository.manualSave()
            soundManager.playCoinBid()
            onSaved("All game progress, squad stats & coins successfully saved to SQLite database!")
        }
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }

    fun toggleDevUnlockCards() {
        _devUnlockAllCards.value = !_devUnlockAllCards.value
    }

    fun toggleVoiceMute() {
        _isVoiceChatMuted.value = !_isVoiceChatMuted.value
    }

    fun clearFeedback() {
        _actionFeedback.value = null
    }

    fun resetCareerStats() {
        viewModelScope.launch {
            val resetUser = repository.resetStats()
            _userProfile.value = resetUser
        }
    }

    fun startNewMatch(
        mode: GameMode = GameMode.SOLO_AI,
        difficulty: AIDifficulty = AIDifficulty.PRO,
        roomCode: String = "BT-${(1000..9999).random()}"
    ) {
        val user = _userProfile.value ?: UserEntity(username = "Player", clubName = "My FC", careerCoins = 500, careerWins = 0)
        val oppName = when (mode) {
            GameMode.SOLO_AI -> "${difficulty.title} Coach"
            GameMode.ONLINE_MULTIPLAYER -> "Challenger_${(10..99).random()}"
            GameMode.LOCAL_PASS_AND_PLAY -> "Player 2"
        }
        val oppBidder = Bidder("opp", oppName, isUser = false, avatarEmoji = "🤖")

        val firstTarget = roundPositions[0]
        val firstMystery = PlayerDatabase.getRandomPlayerForPosition(firstTarget.second)

        _auctionState.value = ActiveAuctionState(
            currentRound = 1,
            totalRounds = roundPositions.size,
            targetSlotId = firstTarget.first,
            targetPosition = firstTarget.second,
            mysteryPlayer = firstMystery,
            currentBidMillions = 5,
            highestBidder = oppBidder,
            secondsLeft = 10,
            phase = AuctionPhase.BIDDING_ACTIVE,
            userBudgetMillions = 140,
            opponentBudgetMillions = difficulty.budgetMillions,
            userSquad = TeamSquad(user.clubName, isUserTeam = true),
            opponentSquad = TeamSquad(oppName, isUserTeam = false),
            gameMode = mode,
            aiDifficulty = difficulty,
            roomCode = roomCode
        )

        _chatMessages.value = listOf(
            ChatMessage("Boom Taakh Ref", "Welcome to the Arena! AI Mode: ${difficulty.title} ⚽ Round 1/11 starting!", isVoiceTranscript = true)
        )

        _currentScreen.value = AppScreen.AUCTION_ROOM
        startCountdownTimer()
    }

    private fun startCountdownTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val current = _auctionState.value ?: break
                if (current.phase != AuctionPhase.BIDDING_ACTIVE) break

                val newSeconds = current.secondsLeft - 1
                if (newSeconds <= 0) {
                    finishBiddingRound()
                    break
                } else {
                    _auctionState.value = current.copy(secondsLeft = newSeconds)
                    if (newSeconds in 2..8 && current.highestBidder.isUser && !current.isBidFreezeActive) {
                        evaluateAiBid(current)
                    }
                }
            }
        }
    }

    private fun evaluateAiBid(state: ActiveAuctionState) {
        if (state.phase != AuctionPhase.BIDDING_ACTIVE || state.isBidFreezeActive) return

        val difficulty = state.aiDifficulty
        val bidChance = when (difficulty) {
            AIDifficulty.ROOKIE -> 40
            AIDifficulty.PRO -> 60
            AIDifficulty.WORLD_CLASS -> 75
            AIDifficulty.LEGEND -> 85
        }

        val roll = (1..100).random()
        if (roll <= bidChance && state.opponentBudgetMillions > state.currentBidMillions + 2) {
            aiBidJob?.cancel()
            aiBidJob = viewModelScope.launch {
                val delayTime = when (difficulty) {
                    AIDifficulty.LEGEND -> (300..700).random().toLong()
                    AIDifficulty.WORLD_CLASS -> (500..1000).random().toLong()
                    AIDifficulty.PRO -> (700..1500).random().toLong()
                    AIDifficulty.ROOKIE -> (1200..2200).random().toLong()
                }
                delay(delayTime)
                val current = _auctionState.value ?: return@launch
                if (current.phase != AuctionPhase.BIDDING_ACTIVE || current.isBidFreezeActive) return@launch

                val raise = when (difficulty) {
                    AIDifficulty.LEGEND -> listOf(3, 5, 8).random()
                    AIDifficulty.WORLD_CLASS -> listOf(3, 5).random()
                    AIDifficulty.PRO -> listOf(2, 3).random()
                    AIDifficulty.ROOKIE -> 2
                }

                val newBid = current.currentBidMillions + raise
                if (newBid <= current.opponentBudgetMillions) {
                    val oppBidder = Bidder("opp", current.opponentSquad.teamName, isUser = false, avatarEmoji = "🤖")
                    _auctionState.value = current.copy(
                        currentBidMillions = newBid,
                        highestBidder = oppBidder,
                        secondsLeft = maxOf(current.secondsLeft, 5)
                    )
                    soundManager.playCoinBid()
                    simulateOpponentVoiceBanter()
                }
            }
        }
    }

    private fun simulateOpponentVoiceBanter() {
        val quotes = listOf(
            "I need this player for my squad!",
            "You cannot outbid me!",
            "Boom Taakh is mine this round!",
            "Check that rating, he's a star!",
            "Ref, watch the clock!"
        )
        val quote = quotes.random()
        _voiceChatActiveSpeaker.value = _auctionState.value?.opponentSquad?.teamName ?: "Rival"
        _chatMessages.value = _chatMessages.value + ChatMessage(
            senderName = _voiceChatActiveSpeaker.value ?: "Opponent",
            text = quote,
            isVoiceTranscript = true
        )
        viewModelScope.launch {
            delay(2500)
            if (_voiceChatActiveSpeaker.value == (_auctionState.value?.opponentSquad?.teamName ?: "Rival")) {
                _voiceChatActiveSpeaker.value = null
            }
        }
    }

    fun placeUserBid(incrementMillions: Int) {
        val current = _auctionState.value ?: return
        if (current.phase != AuctionPhase.BIDDING_ACTIVE) return

        // Rule: If player already bidded and holds highest bid, they CANNOT bid more against themselves!
        if (current.highestBidder.isUser) {
            _actionFeedback.value = "You are already the highest bidder! Wait for rival counter-bid."
            return
        }

        val newBid = current.currentBidMillions + incrementMillions
        if (newBid > current.userBudgetMillions) {
            _actionFeedback.value = "Not enough transfer budget (€${current.userBudgetMillions}M available)!"
            return
        }

        val user = _userProfile.value ?: UserEntity(username = "Player", clubName = "My FC")
        val userBidder = Bidder("user", user.username, isUser = true, avatarEmoji = user.avatarEmoji)

        _auctionState.value = current.copy(
            currentBidMillions = newBid,
            highestBidder = userBidder,
            secondsLeft = maxOf(current.secondsLeft, 5)
        )
        soundManager.playCoinBid()
        startCountdownTimer()
    }

    fun passRound() {
        val current = _auctionState.value ?: return
        if (current.phase != AuctionPhase.BIDDING_ACTIVE) return
        _auctionState.value = current.copy(secondsLeft = 1)
    }

    fun slideMoneyToReferee(amountMillions: Int) {
        val current = _auctionState.value ?: return
        if (current.phase != AuctionPhase.BIDDING_ACTIVE) return
        if (current.userBudgetMillions < amountMillions) {
            _actionFeedback.value = "Not enough match budget for €${amountMillions}M bribe!"
            return
        }

        soundManager.playCoinBid()

        val hasCharm = current.activePowerUps.any { it.type == PowerUpType.BRIBE_CHARM }
        val result = RefereeManager.attemptBribe(amountMillions, hasBribeCharm = hasCharm)

        var newSquad = current.userSquad
        if (result.penaltyAwarded) {
            soundManager.playPenaltyAwarded()
            newSquad = newSquad.copy(
                penaltyAdvantageGoals = newSquad.penaltyAdvantageGoals + 1,
                powerUpBonusOvr = newSquad.powerUpBonusOvr + result.bonusSquadOvr
            )
        }

        _auctionState.value = current.copy(
            userBudgetMillions = current.userBudgetMillions - amountMillions,
            refereeBribeResult = result,
            userSquad = newSquad
        )

        viewModelScope.launch {
            val user = _userProfile.value ?: return@launch
            val updated = user.copy(
                bribesAttempted = user.bribesAttempted + 1,
                bribesSuccessful = if (result.penaltyAwarded) user.bribesSuccessful + 1 else user.bribesSuccessful
            )
            repository.saveUser(updated)
            _userProfile.value = updated
        }
    }

    fun useHintCard(type: HintType) {
        val current = _auctionState.value ?: return
        val user = _userProfile.value ?: return
        val userWins = user.careerWins
        val isUnlocked = CardProgression.areHintsUnlocked(userWins) || _devUnlockAllCards.value

        if (!isUnlocked) {
            _actionFeedback.value = "Hint cards unlock after 15 wins! (Current: $userWins/15)"
            return
        }

        if (current.revealedHints.any { it.type == type }) return

        // Coins are used to use hints!
        if (user.careerCoins < type.coinCost) {
            _actionFeedback.value = "Not enough coins! Need 🪙 ${type.coinCost} (You have 🪙 ${user.careerCoins})"
            return
        }

        val text = CardProgression.generateHintForPlayer(current.mysteryPlayer, type)
        val newCard = HintCard(id = "hint_${type.name}", type = type, costCoins = type.coinCost, isRevealed = true, hintText = text)

        _auctionState.value = current.copy(
            revealedHints = current.revealedHints + newCard
        )
        soundManager.playWhistle()

        // Deduct coins from user balance
        val updatedUser = user.copy(careerCoins = user.careerCoins - type.coinCost)
        _userProfile.value = updatedUser
        viewModelScope.launch {
            repository.saveUser(updatedUser)
        }
    }

    fun usePowerUpCard(type: PowerUpType) {
        val current = _auctionState.value ?: return
        val user = _userProfile.value ?: return
        val userWins = user.careerWins
        val isUnlocked = CardProgression.arePowerUpsUnlocked(userWins) || _devUnlockAllCards.value

        if (!isUnlocked) {
            _actionFeedback.value = "Power-up cards unlock after 30 wins! (Current: $userWins/30)"
            return
        }

        if (current.activePowerUps.any { it.type == type }) return

        // Coins are used to use power-ups!
        if (user.careerCoins < type.coinCost) {
            _actionFeedback.value = "Not enough coins! Need 🪙 ${type.coinCost} (You have 🪙 ${user.careerCoins})"
            return
        }

        val newCard = PowerUpCard(id = "power_${type.name}", type = type, isUsed = true)
        var updatedSquad = current.userSquad
        var bidFreeze = current.isBidFreezeActive
        var updatedUserBudget = current.userBudgetMillions

        when (type) {
            PowerUpType.GOLDEN_BOOST, PowerUpType.LEGEND_PASS,
            PowerUpType.DEFENSE_WALL, PowerUpType.ATTACK_BLITZ,
            PowerUpType.MIDFIELD_MAESTRO, PowerUpType.SCOUT_RADAR -> {
                updatedSquad = updatedSquad.copy(powerUpBonusOvr = updatedSquad.powerUpBonusOvr + type.ovrBoost)
            }
            PowerUpType.BID_FREEZE -> {
                bidFreeze = true
                viewModelScope.launch {
                    delay(4000)
                    _auctionState.value?.let { state ->
                        _auctionState.value = state.copy(isBidFreezeActive = false)
                    }
                }
            }
            PowerUpType.TIME_THIEF -> {
                // Drop auction clock to 2 seconds
                _auctionState.value = current.copy(secondsLeft = 2)
            }
            PowerUpType.BUDGET_INJECTION -> {
                updatedUserBudget += 25
            }
            PowerUpType.AUCTION_DISCOUNT -> {}
            PowerUpType.BRIBE_CHARM -> {}
        }

        _auctionState.value = current.copy(
            activePowerUps = current.activePowerUps + newCard,
            userSquad = updatedSquad,
            isBidFreezeActive = bidFreeze,
            userBudgetMillions = updatedUserBudget
        )
        soundManager.playWhistle()

        // Deduct coins from user balance
        val updatedUser = user.copy(careerCoins = user.careerCoins - type.coinCost)
        _userProfile.value = updatedUser
        viewModelScope.launch {
            repository.saveUser(updatedUser)
        }
    }

    private fun finishBiddingRound() {
        val current = _auctionState.value ?: return
        soundManager.playWhistle()
        soundManager.playBoomTaakhGavel()

        val winner = current.highestBidder
        val player = current.mysteryPlayer
        val slotId = current.targetSlotId

        val hasRebate = current.activePowerUps.any { it.type == PowerUpType.AUCTION_DISCOUNT }
        val effectivePrice = if (winner.isUser && hasRebate) {
            (current.currentBidMillions * 0.8).toInt()
        } else current.currentBidMillions

        val updatedUserSquad = if (winner.isUser) {
            current.userSquad.withPlayerAdded(slotId, player)
        } else current.userSquad

        val updatedOppSquad = if (!winner.isUser) {
            current.opponentSquad.withPlayerAdded(slotId, player)
        } else current.opponentSquad

        val updatedUserBudget = if (winner.isUser) {
            (current.userBudgetMillions - effectivePrice).coerceAtLeast(0)
        } else current.userBudgetMillions

        val updatedOppBudget = if (!winner.isUser) {
            (current.opponentBudgetMillions - current.currentBidMillions).coerceAtLeast(0)
        } else current.opponentBudgetMillions

        val announcement = "BOOM TAAKH! 🔨 Sold to ${winner.name} for €${effectivePrice}M!\n" +
                "The Mystery ${current.targetPosition.code} is ${player.name} (${player.rating} OVR, ${player.club})!"

        _auctionState.value = current.copy(
            phase = AuctionPhase.SOLD_BOOM_TAAKH,
            userSquad = updatedUserSquad,
            opponentSquad = updatedOppSquad,
            userBudgetMillions = updatedUserBudget,
            opponentBudgetMillions = updatedOppBudget,
            refereeAnnouncementText = announcement
        )
    }

    fun proceedToNextRound() {
        val current = _auctionState.value ?: return
        if (current.currentRound >= current.totalRounds) {
            calculateMatchShowdown(current)
            return
        }

        val nextIndex = current.currentRound
        val nextTarget = roundPositions[nextIndex]
        val existingPlayers = (current.userSquad.slots.values + current.opponentSquad.slots.values)
            .mapNotNull { it.player?.id }
            .toSet()
        val nextMystery = PlayerDatabase.getRandomPlayerForPosition(nextTarget.second, existingPlayers)

        val oppBidder = Bidder("opp", current.opponentSquad.teamName, isUser = false, avatarEmoji = "🤖")

        _auctionState.value = current.copy(
            currentRound = current.currentRound + 1,
            targetSlotId = nextTarget.first,
            targetPosition = nextTarget.second,
            mysteryPlayer = nextMystery,
            currentBidMillions = 5,
            highestBidder = oppBidder,
            secondsLeft = 10,
            phase = AuctionPhase.BIDDING_ACTIVE,
            revealedHints = emptyList(),
            activePowerUps = emptyList(),
            refereeBribeResult = null,
            refereeAnnouncementText = ""
        )

        startCountdownTimer()
    }

    private fun calculateMatchShowdown(state: ActiveAuctionState) {
        val userOvr = state.userSquad.totalSquadOvr
        val oppOvr = state.opponentSquad.totalSquadOvr
        val penaltyGoals = state.userSquad.penaltyAdvantageGoals

        val ovrDiff = userOvr - oppOvr
        val baseUserGoals = ((userOvr / 25.0) + (ovrDiff * 0.15) + (-1..2).random()).toInt().coerceAtLeast(0)
        val baseOppGoals = ((oppOvr / 25.0) + (-ovrDiff * 0.1) + (-1..2).random()).toInt().coerceAtLeast(0)

        val finalUserGoals = baseUserGoals + penaltyGoals
        val finalOppGoals = baseOppGoals

        val isUserWinner = finalUserGoals > finalOppGoals || (finalUserGoals == finalOppGoals && userOvr >= oppOvr)
        // User requested: "if player win the 11 matches in the game he get 500 coins and 1 win"
        val coinsWon = if (isUserWinner) 500 else 50
        val winsWon = if (isUserWinner) 1 else 0

        val result = MatchShowdownResult(
            userSquadOvr = userOvr,
            opponentSquadOvr = oppOvr,
            userGoals = finalUserGoals,
            opponentGoals = finalOppGoals,
            penaltyGoalsFromBribe = penaltyGoals,
            winnerName = if (isUserWinner) state.userSquad.teamName else state.opponentSquad.teamName,
            isUserWinner = isUserWinner,
            coinsEarned = coinsWon,
            winsEarned = winsWon
        )

        _showdownResult.value = result

        viewModelScope.launch {
            val updatedUser = repository.recordMatch(
                MatchHistoryEntity(
                    opponentName = state.opponentSquad.teamName,
                    userSquadOvr = userOvr,
                    opponentSquadOvr = oppOvr,
                    userScore = finalUserGoals,
                    opponentScore = finalOppGoals,
                    won = isUserWinner,
                    penaltiesScored = penaltyGoals,
                    coinsEarned = coinsWon
                )
            )
            if (updatedUser != null) {
                _userProfile.value = updatedUser
            }
        }

        _currentScreen.value = AppScreen.MATCH_SHOWDOWN
    }

    fun claimDailyChallenge(challenge: DailyChallengeEntity) {
        viewModelScope.launch {
            repository.claimChallenge(challenge)
            val updated = repository.getOrSeedUser()
            _userProfile.value = updated
        }
    }

    fun updateProfile(username: String, clubName: String, avatar: String) {
        viewModelScope.launch {
            repository.registerOrUpdateProfile(username, clubName, avatar)
            val updated = repository.getOrSeedUser()
            _userProfile.value = updated
        }
    }
}
