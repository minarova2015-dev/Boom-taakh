package com.example.model

enum class HintType(
    val title: String,
    val iconName: String,
    val description: String,
    val coinCost: Int = 50
) {
    LEAGUE("League Hint", "emoji_events", "Reveals the mystery player's domestic league", 50),
    NATION("Nationality Hint", "flag", "Reveals the player's national flag and country", 50),
    AGE_RANGE("Age Range", "cake", "Reveals whether player is young prodigy or veteran", 50),
    CLUB_HINT("Club Clue", "shield", "Reveals the first letter of their current club", 50),
    SKILL_STARS("Skill Moves", "star", "Reveals player skill moves & strong foot", 50),
    RATING_BRACKET("Tier Radar", "radar", "Reveals if rating is 80-85, 86-89, or 90+", 50)
}

data class HintCard(
    val id: String,
    val type: HintType,
    val costCoins: Int = 50,
    val isRevealed: Boolean = false,
    val hintText: String = ""
)

enum class PowerUpType(
    val title: String,
    val iconName: String,
    val description: String,
    val ovrBoost: Int = 0,
    val coinCost: Int = 100
) {
    GOLDEN_BOOST("+3 Squad OVR", "bolt", "Increases your final team rating by +3 OVR", ovrBoost = 3, coinCost = 100),
    LEGEND_PASS("+5 Squad OVR", "workspace_premium", "Massive +5 OVR boost applied to final showdown", ovrBoost = 5, coinCost = 150),
    DEFENSE_WALL("+2 Defense OVR", "shield", "Boosts CB, LB and RB rating synergy by +2 OVR", ovrBoost = 2, coinCost = 80),
    ATTACK_BLITZ("+2 Attack OVR", "local_fire_department", "Boosts ST, LW and RW shooting power by +2 OVR", ovrBoost = 2, coinCost = 80),
    MIDFIELD_MAESTRO("+2 Midfield OVR", "hub", "Increases CAM, CM, and CDM playmaking by +2 OVR", ovrBoost = 2, coinCost = 80),
    BID_FREEZE("Freeze Rival (4s)", "ac_unit", "Locks rival bidding engine for 4 full seconds", ovrBoost = 0, coinCost = 100),
    TIME_THIEF("Rush Clock (2s)", "timer", "Instantly drops auction timer down to 2 seconds!", ovrBoost = 0, coinCost = 120),
    AUCTION_DISCOUNT("20% Bid Rebate", "payments", "Refunds 20% of your final winning bid price", ovrBoost = 0, coinCost = 80),
    BRIBE_CHARM("Ref Whisperer", "thumb_up", "Guarantees your next referee bribe succeeds 100%!", ovrBoost = 1, coinCost = 120),
    BUDGET_INJECTION("+€25M Auction Cash", "account_balance", "Instantly adds €25M transfer funds to this match", ovrBoost = 0, coinCost = 100),
    SCOUT_RADAR("Scout Radar (+1 OVR)", "radar", "Advanced scout analysis adding +1 OVR bonus", ovrBoost = 1, coinCost = 60)
}

data class PowerUpCard(
    val id: String,
    val type: PowerUpType,
    val costCoins: Int = type.coinCost,
    var isUsed: Boolean = false
)

object CardProgression {
    const val WINS_REQUIRED_FOR_HINTS = 15
    const val WINS_REQUIRED_FOR_POWERUPS = 30

    fun areHintsUnlocked(wins: Int): Boolean = wins >= WINS_REQUIRED_FOR_HINTS
    fun arePowerUpsUnlocked(wins: Int): Boolean = wins >= WINS_REQUIRED_FOR_POWERUPS

    fun generateHintForPlayer(player: Player, type: HintType): String {
        return when (type) {
            HintType.LEAGUE -> "Plays in: ${player.league}"
            HintType.NATION -> "Country: ${player.nation} ${player.flagEmoji}"
            HintType.AGE_RANGE -> "Age: ${player.age} years old (${if (player.age < 24) "Young Talent" else if (player.age < 30) "Prime Era" else "Legend Veteran"})"
            HintType.CLUB_HINT -> "Club starts with '${player.club.first()}' (${player.club.length} letters)"
            HintType.SKILL_STARS -> "Foot: ${player.foot} | Skill Moves: ${"★".repeat(player.skillStars)}"
            HintType.RATING_BRACKET -> when {
                player.rating >= 90 -> "Rating: 90+ World Class Elite! 🌟"
                player.rating >= 86 -> "Rating: 86-89 Top Tier Star ⭐"
                else -> "Rating: 80-85 Solid First Team ⚽"
            }
        }
    }
}
