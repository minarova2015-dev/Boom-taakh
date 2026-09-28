package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SquadSlot
import com.example.model.TeamSquad

@Composable
fun PitchFormationView(
    squad: TeamSquad,
    modifier: Modifier = Modifier,
    highlightSlotId: String? = null
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2B1D)),
        modifier = modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFF1E5E3A), RoundedCornerShape(16.dp))
            .testTag("pitch_formation_root")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0A331E), Color(0xFF062314), Color(0xFF0A331E))
                    )
                )
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Team Name and Big OVR Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = squad.teamName,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${squad.totalPlayersCount}/11 Players Signed",
                        color = Color(0xFFA5D6A7),
                        fontSize = 11.sp
                    )
                }

                // OVR Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFD700),
                    modifier = Modifier.testTag("team_ovr_badge")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "TEAM OVR",
                            color = Color(0xFF2C1E00),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "${squad.totalSquadOvr}",
                            color = Color(0xFF1B1200),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Attackers Row: LW, ST, RW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PitchSlotBadge(slot = squad.slots["slot_lw"], isHighlighted = highlightSlotId == "slot_lw")
                PitchSlotBadge(slot = squad.slots["slot_st"], isHighlighted = highlightSlotId == "slot_st")
                PitchSlotBadge(slot = squad.slots["slot_rw"], isHighlighted = highlightSlotId == "slot_rw")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Midfielders Row: CAM, CM, CDM
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PitchSlotBadge(slot = squad.slots["slot_cam"], isHighlighted = highlightSlotId == "slot_cam")
                PitchSlotBadge(slot = squad.slots["slot_cm"], isHighlighted = highlightSlotId == "slot_cm")
                PitchSlotBadge(slot = squad.slots["slot_cdm"], isHighlighted = highlightSlotId == "slot_cdm")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Defenders Row: LB, CB1, CB2, RB
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PitchSlotBadge(slot = squad.slots["slot_lb"], isHighlighted = highlightSlotId == "slot_lb")
                PitchSlotBadge(slot = squad.slots["slot_cb1"], isHighlighted = highlightSlotId == "slot_cb1")
                PitchSlotBadge(slot = squad.slots["slot_cb2"], isHighlighted = highlightSlotId == "slot_cb2")
                PitchSlotBadge(slot = squad.slots["slot_rb"], isHighlighted = highlightSlotId == "slot_rb")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Goalkeeper Row: GK
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                PitchSlotBadge(slot = squad.slots["slot_gk"], isHighlighted = highlightSlotId == "slot_gk")
            }
        }
    }
}

@Composable
private fun PitchSlotBadge(
    slot: SquadSlot?,
    isHighlighted: Boolean
) {
    val pos = slot?.position?.code ?: "??"
    val player = slot?.player

    val bgColor = when {
        isHighlighted -> Color(0xFFFFB703)
        player != null -> Color(0xFF1E3A2B)
        else -> Color(0x33FFFFFF)
    }

    val borderColor = when {
        isHighlighted -> Color(0xFFFFFFFF)
        player != null -> Color(0xFFFFD700)
        else -> Color(0x22FFFFFF)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(62.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(bgColor)
                .border(1.5.dp, borderColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (player != null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${player.rating}",
                        color = Color(0xFFFFD700),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = pos,
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Text(
                    text = pos,
                    color = if (isHighlighted) Color.Black else Color(0xFFA5D6A7),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text(
            text = player?.name?.split(" ")?.lastOrNull() ?: pos,
            color = if (player != null) Color.White else Color(0xFF88AA99),
            fontSize = 9.sp,
            maxLines = 1,
            fontWeight = if (player != null) FontWeight.SemiBold else FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}
