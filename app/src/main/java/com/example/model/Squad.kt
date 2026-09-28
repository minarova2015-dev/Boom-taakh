package com.example.model

data class SquadSlot(
    val slotId: String,
    val position: Position,
    val player: Player? = null
)

data class TeamSquad(
    val teamName: String,
    val isUserTeam: Boolean,
    val slots: Map<String, SquadSlot> = defaultSlots(teamName, isUserTeam),
    val powerUpBonusOvr: Int = 0,
    val penaltyAdvantageGoals: Int = 0
) {
    val totalPlayersCount: Int
        get() = slots.values.count { it.player != null }

    val rawAverageRating: Double
        get() {
            val filled = slots.values.mapNotNull { it.player }
            if (filled.isEmpty()) return 0.0
            return filled.map { it.rating }.average()
        }

    val totalSquadOvr: Int
        get() {
            val filled = slots.values.mapNotNull { it.player }
            if (filled.isEmpty()) return 0
            val avg = filled.map { it.rating }.average()
            // Squad fullness factor and chemistry synergy bonus (same nation or club +1 each)
            var synergy = 0
            val nations = filled.groupBy { it.nation }
            if (nations.any { it.value.size >= 3 }) synergy += 1
            val clubs = filled.groupBy { it.club }
            if (clubs.any { it.value.size >= 2 }) synergy += 1

            return (avg.toInt() + synergy + powerUpBonusOvr).coerceIn(0, 99)
        }

    fun withPlayerAdded(slotId: String, player: Player): TeamSquad {
        val currentSlot = slots[slotId] ?: return this
        val updatedMap = slots.toMutableMap()
        updatedMap[slotId] = currentSlot.copy(player = player)
        return copy(slots = updatedMap)
    }

    fun getNextEmptySlot(): SquadSlot? {
        return slots.values.firstOrNull { it.player == null }
    }

    companion object {
        fun defaultSlots(teamName: String, isUser: Boolean): Map<String, SquadSlot> {
            val formationOrder = listOf(
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
            return formationOrder.associate { (id, pos) ->
                id to SquadSlot(slotId = id, position = pos, player = null)
            }
        }
    }
}
