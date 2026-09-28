package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.example.model.ActiveAuctionState
import com.example.model.MatchShowdownResult
import com.example.ui.components.PitchFormationView
import com.example.viewmodel.AppScreen
import com.example.viewmodel.GameViewModel

@Composable
fun MatchShowdownScreen(
    viewModel: GameViewModel,
    auctionState: ActiveAuctionState?,
    result: MatchShowdownResult?
) {
    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    if (result == null || auctionState == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0D131F)),
            contentAlignment = Alignment.Center
        ) {
            Text("Calculating Squad Showdown...", color = Color.White)
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0F1A))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            // Winner Trophy Header
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFFFFD700), Color(0xFFB8860B))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.EmojiEvents,
                    contentDescription = "Trophy",
                    tint = Color.Black,
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (result.isUserWinner) "VICTORY! SQUAD CHAMPION! 🏆" else "MATCH DEFEAT! 💔",
                color = if (result.isUserWinner) Color(0xFFFFD700) else Color(0xFFFF5252),
                fontWeight = FontWeight.Black,
                fontSize = 22.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = "11-Round Squad OVR & Referee Penalty Shootout",
                color = Color(0xFFA0B0C8),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }

        // Final Match Score Board Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161F33)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFFFFB703), RoundedCornerShape(18.dp))
                    .testTag("match_scoreboard_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // User Team
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = auctionState.userSquad.teamName,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFFFD700),
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${result.userSquadOvr} OVR",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "${result.userGoals}",
                                color = Color(0xFFFFD700),
                                fontWeight = FontWeight.Black,
                                fontSize = 38.sp
                            )
                        }

                        // VS Divider
                        Text(
                            text = "VS",
                            color = Color(0xFF8899AA),
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )

                        // Opponent Team
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = auctionState.opponentSquad.teamName,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF37474F),
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${result.opponentSquadOvr} OVR",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "${result.opponentGoals}",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 38.sp
                            )
                        }
                    }

                    // Penalty Advantage Note from Bribes
                    if (result.penaltyGoalsFromBribe > 0) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF1E3A24),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "💰 +${result.penaltyGoalsFromBribe} Goal awarded via successful Referee Bribe!",
                                color = Color(0xFFA5D6A7),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Explicit Rewards Banner: 500 Coins & 1 Win as requested by user!
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (result.isUserWinner) Color(0xFF1A3D24) else Color(0xFF262E3D),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (result.isUserWinner)
                                    "🎉 MATCH REWARD: +🪙 ${result.coinsEarned} COINS & +${result.winsEarned} WIN!"
                                else
                                    "MATCH REWARD: +🪙 ${result.coinsEarned} Consolation Coins",
                                color = if (result.isUserWinner) Color(0xFF00E676) else Color(0xFFFFD54F),
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Saved to your club career database profile",
                                color = Color(0xFFA0B0C8),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        // Full Pitch Formation Overview for Your Squad
        item {
            Text(
                text = "YOUR FINAL 11-MAN SQUAD (${result.userSquadOvr} OVR)",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.fillMaxWidth()
            )
            PitchFormationView(squad = auctionState.userSquad)
        }

        // Full Pitch Formation Overview for Opponent Squad
        item {
            Text(
                text = "OPPONENT'S SQUAD (${result.opponentSquadOvr} OVR)",
                color = Color(0xFFA0B0C8),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.fillMaxWidth()
            )
            PitchFormationView(squad = auctionState.opponentSquad)
        }

        // Action Buttons: Play Again or Return Home
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { viewModel.startNewMatch(auctionState.gameMode, auctionState.aiDifficulty) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB703)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("play_again_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "START NEW AUCTION MATCH",
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }
                }

                OutlinedButton(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("showdown_home_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Home, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "RETURN TO HOME",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
