package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserEntity
import com.example.model.ActiveAuctionState
import com.example.model.AppLanguage
import com.example.model.AuctionPhase
import com.example.model.StringsHelper
import com.example.ui.components.HintPowerUpTray
import com.example.ui.components.PitchFormationView
import com.example.ui.components.PlayerCardView
import com.example.ui.components.RefereeBribeDialog
import com.example.ui.components.VoiceChatBar
import com.example.viewmodel.AppScreen
import com.example.viewmodel.GameViewModel

@Composable
fun AuctionGameScreen(
    viewModel: GameViewModel,
    auctionState: ActiveAuctionState?,
    user: UserEntity?,
    currentLanguage: AppLanguage,
    devUnlocked: Boolean,
    actionFeedback: String?,
    isVoiceMuted: Boolean,
    activeSpeaker: String?,
    latestVoiceMessage: String?
) {
    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    if (auctionState == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0D131F)),
            contentAlignment = Alignment.Center
        ) {
            Text("Starting Auction...", color = Color.White)
        }
        return
    }

    var showBribeDialog by remember { mutableStateOf(false) }
    var showFormationPitch by remember { mutableStateOf(false) }

    val isUserLeading = auctionState.highestBidder.isUser

    if (showBribeDialog) {
        RefereeBribeDialog(
            userBudgetMillions = auctionState.userBudgetMillions,
            lastResult = auctionState.refereeBribeResult,
            onBribeSelected = { amount ->
                viewModel.slideMoneyToReferee(amount)
            },
            onDismiss = { showBribeDialog = false }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D131F))
            .padding(horizontal = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            // Top Navigation & Round Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    modifier = Modifier.testTag("back_to_home_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF1E283C)
                ) {
                    Text(
                        text = "ROUND ${auctionState.currentRound} / ${auctionState.totalRounds} • ${auctionState.aiDifficulty.title}",
                        color = Color(0xFFFFB703),
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF1E283C),
                    modifier = Modifier.clickable { showFormationPitch = !showFormationPitch }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Squad: ${auctionState.userSquad.totalSquadOvr} OVR",
                            color = Color(0xFF00E676),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                        Icon(
                            imageVector = if (showFormationPitch) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Toggle pitch",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Action Feedback Toast/Banner (e.g. "You cannot bid more against yourself!" or "Not enough coins!")
        if (!actionFeedback.isNullOrBlank()) {
            item {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF4A1A1A),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.clearFeedback() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFFF8A80), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = actionFeedback,
                                color = Color(0xFFFFCDD2),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Color.White, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }

        // Target Position Announcement Bar (e.g. ST, CM, etc.)
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF192233)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, Color(0xFFFFB703), RoundedCornerShape(14.dp))
                    .testTag("target_position_banner")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFFB703)
                        ) {
                            Text(
                                text = auctionState.targetPosition.code,
                                color = Color.Black,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = StringsHelper.get("target_position", currentLanguage),
                                color = Color(0xFFA0B0C8),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = auctionState.targetPosition.displayName,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Timer Countdown
                    val isUrgent = auctionState.secondsLeft <= 3 && auctionState.phase == AuctionPhase.BIDDING_ACTIVE
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isUrgent) Color(0xFFE53935) else Color(0xFF263238),
                        modifier = Modifier.testTag("auction_timer_badge")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Timer,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${auctionState.secondsLeft}s",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Expandable Full Pitch Formation
        item {
            AnimatedVisibility(visible = showFormationPitch) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PitchFormationView(
                        squad = auctionState.userSquad,
                        highlightSlotId = auctionState.targetSlotId
                    )
                    PitchFormationView(
                        squad = auctionState.opponentSquad,
                        highlightSlotId = auctionState.targetSlotId
                    )
                }
            }
        }

        // Central Mystery / Revealed Player Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                PlayerCardView(
                    player = auctionState.mysteryPlayer,
                    targetPosition = auctionState.targetPosition,
                    isMystery = auctionState.phase == AuctionPhase.BIDDING_ACTIVE
                )
            }
        }

        // Current Highest Bid Status Banner
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF151C2C)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.5.dp,
                        if (isUserLeading) Color(0xFF00E676) else Color(0xFF2A364F),
                        RoundedCornerShape(14.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "CURRENT BID",
                            color = Color(0xFFA0B0C8),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "€${auctionState.currentBidMillions} Million",
                            color = Color(0xFFFFD700),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "LEADING BIDDER",
                            color = Color(0xFFA0B0C8),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(auctionState.highestBidder.avatarEmoji, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isUserLeading) "YOU (Leading Bid!)" else auctionState.highestBidder.name,
                                color = if (isUserLeading) Color(0xFF00E676) else Color(0xFFFF9100),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }

        // User Budget vs Opponent Budget + Club Coins
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF1B2A1E),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Auction Budget", color = Color(0xFFA5D6A7), fontSize = 10.sp)
                        Text("€${auctionState.userBudgetMillions}M", color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF2B2516),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Club Coins", color = Color(0xFFFFD54F), fontSize = 10.sp)
                        Text("🪙 ${user?.careerCoins ?: 500}", color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF2A1E1E),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Rival Budget", color = Color(0xFFFFAB91), fontSize = 10.sp)
                        Text("€${auctionState.opponentBudgetMillions}M", color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                    }
                }
            }
        }

        // Dramatic BOOM TAAKH Announcement Banner when round finished
        if (auctionState.phase == AuctionPhase.SOLD_BOOM_TAAKH) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF332007)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, Color(0xFFFFB703), RoundedCornerShape(16.dp))
                        .testTag("boom_taakh_sold_banner")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "💥 BOOM TAAKH! 💥",
                            color = Color(0xFFFFD700),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = auctionState.refereeAnnouncementText,
                            color = Color.White,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { viewModel.proceedToNextRound() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB703)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("proceed_next_round_button")
                        ) {
                            Text(
                                text = if (auctionState.currentRound >= auctionState.totalRounds)
                                    "GO TO FINAL SHOWDOWN 🏆"
                                else
                                    "NEXT ROUND (${auctionState.currentRound + 1}/${auctionState.totalRounds}) →",
                                color = Color.Black,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        } else {
            // Interactive Bidding & Bribe Buttons
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Highest Bidder Warning Indicator: User already holds the highest bid!
                    if (isUserLeading) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF1B3D22),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🔒 You placed the highest bid! Waiting for opponent counter-bid.",
                                color = Color(0xFF00E676),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.placeUserBid(2) },
                            enabled = !isUserLeading && auctionState.userBudgetMillions >= auctionState.currentBidMillions + 2,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("bid_plus_2_button")
                        ) {
                            Text("+€2M BID", fontWeight = FontWeight.Black, color = Color.Black, fontSize = 13.sp)
                        }

                        Button(
                            onClick = { viewModel.placeUserBid(5) },
                            enabled = !isUserLeading && auctionState.userBudgetMillions >= auctionState.currentBidMillions + 5,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB703)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("bid_plus_5_button")
                        ) {
                            Text("+€5M BID", fontWeight = FontWeight.Black, color = Color.Black, fontSize = 13.sp)
                        }

                        OutlinedButton(
                            onClick = { viewModel.passRound() },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(0.8f)
                                .height(48.dp)
                                .testTag("pass_round_button")
                        ) {
                            Text("PASS", fontWeight = FontWeight.Bold, color = Color(0xFFFF5252), fontSize = 12.sp)
                        }
                    }

                    // Slide the referee money button
                    Button(
                        onClick = { showBribeDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("slide_the_referee_button")
                    ) {
                        Text("👨‍⚖️ Slide the Referee Some Money 💵", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                    }
                }
            }
        }

        // Voice Chat Bar
        item {
            VoiceChatBar(
                isMuted = isVoiceMuted,
                activeSpeaker = activeSpeaker,
                latestTranscript = latestVoiceMessage,
                onToggleMute = { viewModel.toggleVoiceMute() }
            )
        }

        // Bottom Hint and Power-Up Tray (Costs coins, unlocked at 15/30 wins)
        item {
            HintPowerUpTray(
                careerWins = user?.careerWins ?: 0,
                careerCoins = user?.careerCoins ?: 500,
                devUnlocked = devUnlocked,
                revealedHints = auctionState.revealedHints,
                activePowerUps = auctionState.activePowerUps,
                onHintClick = { type -> viewModel.useHintCard(type) },
                onPowerUpClick = { type -> viewModel.usePowerUpCard(type) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
