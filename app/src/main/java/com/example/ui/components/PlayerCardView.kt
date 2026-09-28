package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Player
import com.example.model.Position

@Composable
fun PlayerCardView(
    player: Player,
    targetPosition: Position,
    isMystery: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )

    val cardBorder = if (isMystery) {
        Brush.linearGradient(listOf(Color(0xFFFFB703), Color(0xFFFB8500), Color(0xFFFFD166)))
    } else {
        Brush.linearGradient(listOf(Color(0xFFFFD700), Color(0xFFF39C12), Color(0xFFE67E22), Color(0xFFFFD700)))
    }

    Box(
        modifier = modifier
            .width(220.dp)
            .height(290.dp)
            .scale(if (isMystery) glowScale else 1f),
        contentAlignment = Alignment.Center
    ) {
        // Outer glowing border
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF141923)),
            modifier = Modifier
                .fillMaxSize()
                .border(3.dp, cardBorder, RoundedCornerShape(18.dp))
                .testTag("player_card_root")
        ) {
            if (isMystery) {
                // Mystery Unknown Player Card
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF1E2638), Color(0xFF0F141D))
                            )
                        )
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top header: Position Announcement
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFFB703),
                            modifier = Modifier.padding(2.dp)
                        ) {
                            Text(
                                text = targetPosition.code,
                                color = Color.Black,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = "??? OVR",
                            color = Color(0xFFFFD166),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    // Center: Dramatic Mystery Silhouette / Question Mark
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0x33FFB703)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = "Unknown mystery player",
                            tint = Color(0xFFFFB703),
                            modifier = Modifier.size(68.dp)
                        )
                    }

                    // Bottom info
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0x33FFFFFF)
                        ) {
                            Text(
                                text = "MYSTERY PLAYER",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Position: ${targetPosition.displayName}",
                            color = Color(0xFFB0B9C6),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                // Revealed Real Player Card
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF2C2416), Color(0xFF161922))
                            )
                        )
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top: Rating, Position, Flag
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${player.rating}",
                                color = Color(0xFFFFD700),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = player.position.code,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = player.flagEmoji,
                            fontSize = 28.sp
                        )
                    }

                    // Center Avatar representation
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(40.dp))
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0xFFFFD700), Color(0xFF8B6508))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "⚽",
                            fontSize = 42.sp
                        )
                    }

                    // Name and Club
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = player.name,
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "${player.club} • ${player.league}",
                            color = Color(0xFFCCCCCC),
                            fontSize = 11.sp,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    }

                    // 6 Core Stats Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0x33000000), RoundedCornerShape(8.dp))
                            .padding(vertical = 4.dp, horizontal = 6.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(label = "PAC", value = player.pace)
                        StatItem(label = "SHO", value = player.shooting)
                        StatItem(label = "PAS", value = player.passing)
                        StatItem(label = "DRI", value = player.dribbling)
                        StatItem(label = "DEF", value = player.defending)
                        StatItem(label = "PHY", value = player.physical)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$value",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
        )
        Text(
            text = label,
            color = Color(0xFFB0B9C6),
            fontSize = 9.sp
        )
    }
}
