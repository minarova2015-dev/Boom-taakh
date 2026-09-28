package com.example.model

enum class BribeStatus {
    IDLE,
    PROCESSING,
    ACCEPTED_PENALTY,
    POCKETED_NOTHING,
    WARNING_ISSUED
}

data class RefereeBribeResult(
    val status: BribeStatus,
    val amountPaidMillions: Int,
    val message: String,
    val penaltyAwarded: Boolean,
    val bonusSquadOvr: Int = 0
)

object RefereeManager {
    val BRIBE_OPTIONS_MILLIONS = listOf(5, 10, 20)

    fun attemptBribe(amountMillions: Int, hasBribeCharm: Boolean = false): RefereeBribeResult {
        if (hasBribeCharm) {
            return RefereeBribeResult(
                status = BribeStatus.ACCEPTED_PENALTY,
                amountPaidMillions = amountMillions,
                message = "The referee gave a subtle nod and pocketed the €${amountMillions}M! Penalty shot awarded in your favor!",
                penaltyAwarded = true,
                bonusSquadOvr = 2
            )
        }

        val roll = (1..100).random()
        return when {
            // Higher amount increases chance of penalty shot
            amountMillions >= 20 && roll <= 65 -> {
                RefereeBribeResult(
                    status = BribeStatus.ACCEPTED_PENALTY,
                    amountPaidMillions = amountMillions,
                    message = "💰 The referee secretly slipped the cash into his socks! He blew his whistle and awarded you a PENALTY SHOT!",
                    penaltyAwarded = true,
                    bonusSquadOvr = 3
                )
            }
            amountMillions >= 10 && roll <= 50 -> {
                RefereeBribeResult(
                    status = BribeStatus.ACCEPTED_PENALTY,
                    amountPaidMillions = amountMillions,
                    message = "💰 Ref accepted the envelope! 'Watch the box closely next round,' he whispered. Penalty advantage awarded!",
                    penaltyAwarded = true,
                    bonusSquadOvr = 2
                )
            }
            amountMillions == 5 && roll <= 35 -> {
                RefereeBribeResult(
                    status = BribeStatus.ACCEPTED_PENALTY,
                    amountPaidMillions = amountMillions,
                    message = "💰 Ref pocketed the €5M on the sideline and granted a sneaky penalty kick advantage!",
                    penaltyAwarded = true,
                    bonusSquadOvr = 1
                )
            }
            roll <= 88 -> {
                RefereeBribeResult(
                    status = BribeStatus.POCKETED_NOTHING,
                    amountPaidMillions = amountMillions,
                    message = "🤦‍♂️ The referee took your €${amountMillions}M, stuffed it in his pocket, looked the other way, and whistled innocently! Nothing was given!",
                    penaltyAwarded = false,
                    bonusSquadOvr = 0
                )
            }
            else -> {
                RefereeBribeResult(
                    status = BribeStatus.WARNING_ISSUED,
                    amountPaidMillions = amountMillions,
                    message = "⚠️ The referee glared back angrily! 'No funny business here!' He took the cash into evidence and flashed a strict verbal warning!",
                    penaltyAwarded = false,
                    bonusSquadOvr = 0
                )
            }
        }
    }
}
