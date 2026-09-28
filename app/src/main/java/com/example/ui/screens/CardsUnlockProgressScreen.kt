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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserEntity
import com.example.model.CardProgression
import com.example.model.HintType
import com.example.model.PowerUpType
import com.example.viewmodel.AppScreen
import com.example.viewmodel.GameViewModel

@Composable
fun CardsUnlockProgressScreen(
    viewModel: GameViewModel,
    user: UserEntity?,
    devUnlocked: Boolean
) {
    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    val wins = user?.careerWins ?: 0
    val hintsUnlocked = CardProgression.areHintsUnlocked(wins) || devUnlocked
    val powerUpsUnlocked = CardProgression.arePowerUpsUnlocked(wins) || devUnlocked

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D131F))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    modifier = Modifier.testTag("back_from_progression_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Cards & Power-Up Unlocks",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Win matches to unlock tactical cards during auctions!",
                        color = Color(0xFFA0B0C8),
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Dev Mode Toggle Card
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2333)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Test All Cards (Instant Dev Unlock)",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = if (devUnlocked) "All cards unlocked for testing" else "Standard unlock rules active",
                            color = Color(0xFF90A4AE),
                            fontSize = 10.sp
                        )
                    }
                    Switch(
                        checked = devUnlocked,
                        onCheckedChange = { viewModel.toggleDevUnlockCards() },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFFFB703))
                    )
                }
            }
        }

        // Section 1: Hint Cards (Unlocks at 15 Wins)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF141E30)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.5.dp,
                        if (hintsUnlocked) Color(0xFF00E676) else Color(0xFFFFB703),
                        RoundedCornerShape(16.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🔍", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Hint Cards Collection",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "Requires 15 Winning Matches",
                                    color = Color(0xFFA5D6A7),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (hintsUnlocked) Color(0xFF00C853) else Color(0xFF37474F)
                        ) {
                            Text(
                                text = if (hintsUnlocked) "UNLOCKED ✓" else "$wins / 15 Wins",
                                color = if (hintsUnlocked) Color.Black else Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val hintFraction = (wins.toFloat() / 15f).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { if (hintsUnlocked) 1f else hintFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = Color(0xFF00E676),
                        trackColor = Color(0xFF223049)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Included Clue Cards:",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    HintType.values().forEach { hint ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("• ", color = Color(0xFFFFB703), fontWeight = FontWeight.Bold)
                            Text(
                                text = "${hint.title} (🪙 ${hint.coinCost}): ",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = hint.description,
                                color = Color(0xFFB0C0D8),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Section 2: Power-Up Cards (Unlocks at 30 Wins)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A172E)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.5.dp,
                        if (powerUpsUnlocked) Color(0xFFE040FB) else Color(0xFF7E57C2),
                        RoundedCornerShape(16.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⚡", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Power-Up Cards Collection",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "Requires 30 Winning Matches",
                                    color = Color(0xFFCE93D8),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (powerUpsUnlocked) Color(0xFFE040FB) else Color(0xFF37474F)
                        ) {
                            Text(
                                text = if (powerUpsUnlocked) "UNLOCKED ✓" else "$wins / 30 Wins",
                                color = if (powerUpsUnlocked) Color.Black else Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val powerFraction = (wins.toFloat() / 30f).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { if (powerUpsUnlocked) 1f else powerFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = Color(0xFFE040FB),
                        trackColor = Color(0xFF292244)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Included Boost Cards:",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    PowerUpType.values().forEach { power ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("• ", color = Color(0xFFE040FB), fontWeight = FontWeight.Bold)
                            Text(
                                text = "${power.title} (🪙 ${power.coinCost}): ",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = power.description,
                                color = Color(0xFFD1C4E9),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
