package com.example.data

import kotlinx.coroutines.flow.Flow

sealed class AuthResult {
    data class Success(val user: UserEntity) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class BoomTaakhRepository(
    private val userDao: UserDao,
    private val matchHistoryDao: MatchHistoryDao,
    private val dailyChallengeDao: DailyChallengeDao
) {
    val userProfile: Flow<UserEntity?> = userDao.getActiveUserProfile()
    val allAccounts: Flow<List<UserEntity>> = userDao.getAllAccounts()
    val matchHistory: Flow<List<MatchHistoryEntity>> = matchHistoryDao.getAllMatches()
    val dailyChallenges: Flow<List<DailyChallengeEntity>> = dailyChallengeDao.getAllChallenges()

    suspend fun getOrSeedUser(): UserEntity {
        val existingActive = userDao.getActiveUserProfileSync()
        if (existingActive != null) {
            return existingActive
        }

        // Check if any user profile exists in database
        val existing = userDao.getUserByUsername("StrikerKing")
        if (existing != null) {
            userDao.setActiveUser(existing.id)
            return existing.copy(isLoggedIn = true)
        }

        val defaultUser = UserEntity(
            username = "StrikerKing",
            email = "coach@boomtaakh.com",
            password = "password",
            clubName = "Boom Taakh FC",
            avatarEmoji = "🦁",
            careerCoins = 500,
            careerWins = 0,
            careerLosses = 0,
            highestSquadOvr = 0,
            bribesAttempted = 0,
            bribesSuccessful = 0,
            isLoggedIn = true,
            lastSavedTimestamp = System.currentTimeMillis()
        )
        val id = userDao.insertUser(defaultUser)
        seedInitialChallenges()
        return defaultUser.copy(id = id)
    }

    suspend fun login(username: String, password: String): AuthResult {
        if (username.isBlank() || password.isBlank()) {
            return AuthResult.Error("Username and password cannot be empty")
        }
        val user = userDao.login(username.trim(), password)
        return if (user != null) {
            userDao.logoutAll()
            userDao.setActiveUser(user.id)
            AuthResult.Success(user.copy(isLoggedIn = true, lastSavedTimestamp = System.currentTimeMillis()))
        } else {
            val userExists = userDao.getUserByUsername(username.trim())
            if (userExists != null) {
                AuthResult.Error("Incorrect password for @${username.trim()}")
            } else {
                AuthResult.Error("Account @${username.trim()} not found. Please sign up!")
            }
        }
    }

    suspend fun signUp(
        username: String,
        email: String,
        password: String,
        clubName: String,
        avatarEmoji: String
    ): AuthResult {
        if (username.isBlank() || password.isBlank() || clubName.isBlank()) {
            return AuthResult.Error("Please fill in username, password, and club name")
        }
        val existing = userDao.getUserByUsername(username.trim())
        if (existing != null) {
            return AuthResult.Error("Username @${username.trim()} is already taken!")
        }

        userDao.logoutAll()
        val newUser = UserEntity(
            username = username.trim(),
            email = email.trim(),
            password = password,
            clubName = clubName.trim(),
            avatarEmoji = avatarEmoji,
            careerCoins = 500,
            careerWins = 0,
            careerLosses = 0,
            highestSquadOvr = 0,
            bribesAttempted = 0,
            bribesSuccessful = 0,
            isLoggedIn = true,
            lastSavedTimestamp = System.currentTimeMillis()
        )
        val newId = userDao.insertUser(newUser)
        seedInitialChallenges()
        return AuthResult.Success(newUser.copy(id = newId))
    }

    suspend fun logout() {
        userDao.logoutAll()
    }

    suspend fun switchAccount(userId: Long): UserEntity? {
        userDao.logoutAll()
        userDao.setActiveUser(userId)
        return userDao.getActiveUserProfileSync()
    }

    suspend fun manualSave(): Long {
        val user = userDao.getActiveUserProfileSync() ?: return 0L
        val updated = user.copy(lastSavedTimestamp = System.currentTimeMillis())
        userDao.updateUser(updated)
        return updated.lastSavedTimestamp
    }

    suspend fun seedInitialChallenges() {
        val challenges = listOf(
            DailyChallengeEntity(
                id = "ch_win_1",
                title = "Auction Domination",
                description = "Win any Boom Taakh 11-round squad match",
                target = 1,
                currentProgress = 0,
                rewardCoins = 500
            ),
            DailyChallengeEntity(
                id = "ch_bribe_1",
                title = "Smooth Operator",
                description = "Successfully bribe the referee for a penalty advantage",
                target = 1,
                currentProgress = 0,
                rewardCoins = 300
            ),
            DailyChallengeEntity(
                id = "ch_ovr_88",
                title = "Galácticos Squad",
                description = "Assemble a total squad with 88+ OVR in an auction",
                target = 88,
                currentProgress = 0,
                rewardCoins = 500
            ),
            DailyChallengeEntity(
                id = "ch_hints_1",
                title = "Intel Master",
                description = "Unlock or reveal 2 player hints during auctions",
                target = 2,
                currentProgress = 0,
                rewardCoins = 250
            )
        )
        dailyChallengeDao.insertChallenges(challenges)
    }

    suspend fun saveUser(user: UserEntity) {
        val saved = user.copy(lastSavedTimestamp = System.currentTimeMillis())
        userDao.updateUser(saved)
    }

    suspend fun registerOrUpdateProfile(username: String, clubName: String, avatar: String) {
        val user = userDao.getActiveUserProfileSync() ?: UserEntity(
            username = username,
            clubName = clubName,
            avatarEmoji = avatar
        )
        userDao.updateUser(user.copy(username = username, clubName = clubName, avatarEmoji = avatar, isLoggedIn = true, lastSavedTimestamp = System.currentTimeMillis()))
    }

    suspend fun recordMatch(match: MatchHistoryEntity): UserEntity? {
        matchHistoryDao.insertMatch(match)
        val user = userDao.getActiveUserProfileSync() ?: return null
        val updatedUser = user.copy(
            careerWins = if (match.won) user.careerWins + 1 else user.careerWins,
            careerLosses = if (!match.won) user.careerLosses + 1 else user.careerLosses,
            careerCoins = user.careerCoins + match.coinsEarned,
            highestSquadOvr = maxOf(user.highestSquadOvr, match.userSquadOvr),
            lastSavedTimestamp = System.currentTimeMillis()
        )
        userDao.updateUser(updatedUser)
        return updatedUser
    }

    suspend fun claimChallenge(challenge: DailyChallengeEntity) {
        if (challenge.isClaimed || challenge.currentProgress < challenge.target) return
        dailyChallengeDao.updateChallenge(challenge.copy(isClaimed = true))
        val user = userDao.getActiveUserProfileSync() ?: return
        userDao.updateUser(user.copy(careerCoins = user.careerCoins + challenge.rewardCoins, lastSavedTimestamp = System.currentTimeMillis()))
    }

    suspend fun resetStats(): UserEntity {
        val user = userDao.getActiveUserProfileSync() ?: UserEntity(
            username = "StrikerKing",
            clubName = "Boom Taakh FC"
        )
        val reset = user.copy(
            careerCoins = 500,
            careerWins = 0,
            careerLosses = 0,
            highestSquadOvr = 0,
            bribesAttempted = 0,
            bribesSuccessful = 0,
            lastSavedTimestamp = System.currentTimeMillis()
        )
        userDao.updateUser(reset)
        return reset
    }
}
