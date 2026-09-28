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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CardProgression
import com.example.model.HintCard
import com.example.model.HintType
import com.example.model.PowerUpCard
import com.example.model.PowerUpType

@Composable
fun HintPowerUpTray(
    careerWins: Int,
    careerCoins: Int,
    devUnlocked: Boolean,
    revealedHints: List<HintCard>,
    activePowerUps: List<PowerUpCard>,
    onHintClick: (HintType) -> Unit,
    onPowerUpClick: (PowerUpType) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val hintsUnlocked = CardProgression.areHintsUnlocked(careerWins) || devUnlocked
    val powerUpsUnlocked = CardProgression.arePowerUpsUnlocked(careerWins) || devUnlocked

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF141926)),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF2B354F), RoundedCornerShape(16.dp))
            .testTag("cards_tray_root")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Header with Balance
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tactical Cards",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0x33FFD700)
                ) {
                    Text(
                        text = "Balance: 🪙 $careerCoins Coins",
                        color = Color(0xFFFFD700),
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            // Tabs: Hint Cards (15 Wins) vs Power-Ups (30 Wins)
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color(0xFFFFB703)
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Hint Cards",
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTab == 0) Color(0xFFFFB703) else Color.Gray,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            if (!hintsUnlocked) {
                                Icon(
                                    Icons.Default.Lock,
                                    contentDescription = "Locked",
                                    tint = Color(0xFFFFB703),
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Power-Ups (${PowerUpType.values().size})",
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTab == 1) Color(0xFFFFB703) else Color.Gray,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            if (!powerUpsUnlocked) {
                                Icon(
                                    Icons.Default.Lock,
                                    contentDescription = "Locked",
                                    tint = Color(0xFFFFB703),
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (selectedTab == 0) {
                // Hint Cards Section
                if (!hintsUnlocked) {
                    UnlockRequirementBanner(
                        title = "Hint Cards Locked",
                        subtitle = "Unlocked after 15 Winning Games! Current wins: $careerWins/15"
                    )
                } else {
                    Text(
                        text = "Cost: 🪙 50 Coins each. Tap to reveal clue about the mystery player:",
                        color = Color(0xFFA0B0C8),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(HintType.values()) { type ->
                            val existing = revealedHints.firstOrNull { it.type == type }
                            val canAfford = careerCoins >= type.coinCost
                            HintCardItem(
                                type = type,
                                revealed = existing != null,
                                hintText = existing?.hintText ?: "",
                                canAfford = canAfford,
                                onClick = { onHintClick(type) }
                            )
                        }
                    }
                }
            } else {
                // Power-Ups Section
                if (!powerUpsUnlocked) {
                    UnlockRequirementBanner(
                        title = "Power-Up Cards Locked",
                        subtitle = "Unlocked after 30 Winning Games! Current wins: $careerWins/30"
                    )
                } else {
                    Text(
                        text = "Uses Coins to activate OVR boosts, freeze bids & transfer funds:",
                        color = Color(0xFFA0B0C8),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(PowerUpType.values()) { type ->
                            val isUsed = activePowerUps.any { it.type == type }
                            val canAfford = careerCoins >= type.coinCost
                            PowerUpCardItem(
                                type = type,
                                isUsed = isUsed,
                                canAfford = canAfford,
                                onClick = { onPowerUpClick(type) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UnlockRequirementBanner(title: String, subtitle: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF1E2638),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Lock,
                contentDescription = "Locked requirement",
                tint = Color(0xFFFFB703),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(subtitle, color = Color(0xFFA0B0C8), fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun HintCardItem(
    type: HintType,
    revealed: Boolean,
    hintText: String,
    canAfford: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (revealed) Color(0xFF1E3A28) else Color(0xFF222B3D)
        ),
        modifier = Modifier
            .width(140.dp)
            .height(78.dp)
            .clickable(enabled = !revealed, onClick = onClick)
            .border(
                1.dp,
                if (revealed) Color(0xFF4CAF50) else if (canAfford) Color(0xFF3E4D6E) else Color(0xFF552222),
                RoundedCornerShape(12.dp)
            )
            .testTag("hint_card_${type.name}")
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = type.title,
                    color = if (revealed) Color(0xFFA5D6A7) else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    maxLines = 1
                )
                if (!revealed) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0x33FFD700)
                    ) {
                        Text(
                            text = "🪙 50",
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = if (revealed) hintText else "Tap to buy clue (50 coins)",
                color = if (revealed) Color.White else if (canAfford) Color(0xFF90A4AE) else Color(0xFFFF8A80),
                fontSize = 10.sp,
                maxLines = 2,
                lineHeight = 12.sp
            )
        }
    }
}

@Composable
private fun PowerUpCardItem(
    type: PowerUpType,
    isUsed: Boolean,
    canAfford: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUsed) Color(0xFF332A15) else Color(0xFF2C223A)
        ),
        modifier = Modifier
            .width(145.dp)
            .height(78.dp)
            .clickable(enabled = !isUsed, onClick = onClick)
            .border(
                1.dp,
                if (isUsed) Color(0xFFFFB703) else if (canAfford) Color(0xFF7E57C2) else Color(0xFF552222),
                RoundedCornerShape(12.dp)
            )
            .testTag("powerup_card_${type.name}")
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = type.title,
                    color = if (isUsed) Color(0xFFFFD54F) else Color(0xFFCE93D8),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    maxLines = 1
                )
                if (!isUsed) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0x33E040FB)
                    ) {
                        Text(
                            text = "🪙 ${type.coinCost}",
                            color = Color(0xFFE040FB),
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = if (isUsed) "ACTIVE ON SQUAD!" else type.description,
                color = if (isUsed) Color(0xFFFFE082) else if (canAfford) Color(0xFFB0B9C6) else Color(0xFFFF8A80),
                fontSize = 10.sp,
                maxLines = 2,
                lineHeight = 12.sp
            )
        }
    }
}
