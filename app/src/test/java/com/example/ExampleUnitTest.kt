package com.example

import com.example.data.UserEntity
import com.example.model.AIDifficulty
import com.example.model.CardProgression
import com.example.model.HintType
import com.example.model.Player
import com.example.model.Position
import com.example.model.PowerUpType
import com.example.model.RefereeManager
import com.example.model.TeamSquad
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testCardProgressionUnlocks() {
        assertFalse(CardProgression.areHintsUnlocked(14))
        assertTrue(CardProgression.areHintsUnlocked(15))
        assertTrue(CardProgression.areHintsUnlocked(25))

        assertFalse(CardProgression.arePowerUpsUnlocked(29))
        assertTrue(CardProgression.arePowerUpsUnlocked(30))
        assertTrue(CardProgression.arePowerUpsUnlocked(45))
    }

    @Test
    fun testSquadOvrCalculation() {
        var squad = TeamSquad("Test FC", isUserTeam = true)
        assertEquals(0, squad.totalSquadOvr)

        val player = Player("p1", "Test Striker", Position.ST, 90, "Real Madrid", "La Liga", "France", "🇫🇷", 26, "Right", 5, 90, 90, 80, 90, 40, 80)
        squad = squad.withPlayerAdded("slot_st", player)

        assertEquals(90, squad.totalSquadOvr)
    }

    @Test
    fun testRefereeBribe() {
        val result = RefereeManager.attemptBribe(amountMillions = 10, hasBribeCharm = true)
        assertTrue(result.penaltyAwarded)
        assertEquals(10, result.amountPaidMillions)
    }

    @Test
    fun testAIDifficulties() {
        assertEquals(110, AIDifficulty.ROOKIE.budgetMillions)
        assertEquals(140, AIDifficulty.PRO.budgetMillions)
        assertEquals(160, AIDifficulty.WORLD_CLASS.budgetMillions)
        assertEquals(185, AIDifficulty.LEGEND.budgetMillions)
    }

    @Test
    fun testCoinCostsAndPowerUpCardVariety() {
        assertEquals(50, HintType.LEAGUE.coinCost)
        assertTrue(PowerUpType.values().size >= 10)
        assertTrue(PowerUpType.GOLDEN_BOOST.coinCost > 0)
        assertTrue(PowerUpType.BID_FREEZE.coinCost > 0)
        assertTrue(PowerUpType.BUDGET_INJECTION.coinCost > 0)
    }

    @Test
    fun testUserEntityDefaults() {
        val user = UserEntity(
            username = "NewManager",
            clubName = "My Club FC"
        )
        assertEquals(500, user.careerCoins)
        assertEquals(0, user.careerWins)
        assertTrue(user.lastSavedTimestamp > 0)
    }
}
