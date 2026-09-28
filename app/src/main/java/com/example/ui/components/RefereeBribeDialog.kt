package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.BribeStatus
import com.example.model.RefereeBribeResult

@Composable
fun RefereeBribeDialog(
    userBudgetMillions: Int,
    lastResult: RefereeBribeResult?,
    onBribeSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E222D)),
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color(0xFFFFB703), RoundedCornerShape(20.dp))
                .testTag("referee_bribe_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header referee emoji and title
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color(0x33FFB703), RoundedCornerShape(32.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👨‍⚖️💸", fontSize = 32.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Slide The Referee Some Cash",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Slip an envelope to the ref. He might whistle a Penalty Shot in your favor... or take your money and pretend nothing happened!",
                    color = Color(0xFFB0B9C6),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                // Current Result if any
                if (lastResult != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    val bannerBg = when (lastResult.status) {
                        BribeStatus.ACCEPTED_PENALTY -> Color(0xFF1E4620)
                        BribeStatus.POCKETED_NOTHING -> Color(0xFF4A3410)
                        BribeStatus.WARNING_ISSUED -> Color(0xFF4D1414)
                        else -> Color(0xFF262E3D)
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = bannerBg,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = when (lastResult.status) {
                                    BribeStatus.ACCEPTED_PENALTY -> "⚽ PENALTY SHOT GRANTED!"
                                    BribeStatus.POCKETED_NOTHING -> "🤦‍♂️ HE POCKETED IT & WALKED AWAY!"
                                    BribeStatus.WARNING_ISSUED -> "⚠️ STRICT WARNING ISSUED!"
                                    else -> ""
                                },
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = lastResult.message,
                                color = Color(0xFFE0E0E0),
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Current Budget: €${userBudgetMillions}M",
                    color = Color(0xFFFFD700),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Bribe Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BribeOptionButton(
                        amountMillions = 5,
                        label = "€5M",
                        chance = "35% Luck",
                        enabled = userBudgetMillions >= 5,
                        onClick = { onBribeSelected(5) },
                        modifier = Modifier.weight(1f)
                    )
                    BribeOptionButton(
                        amountMillions = 10,
                        label = "€10M",
                        chance = "50% Luck",
                        enabled = userBudgetMillions >= 10,
                        onClick = { onBribeSelected(10) },
                        modifier = Modifier.weight(1f)
                    )
                    BribeOptionButton(
                        amountMillions = 20,
                        label = "€20M",
                        chance = "65% Luck",
                        enabled = userBudgetMillions >= 20,
                        onClick = { onBribeSelected(20) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dismiss_bribe_button")
                ) {
                    Text("Close / Back to Bidding", color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun BribeOptionButton(
    amountMillions: Int,
    label: String,
    chance: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.testTag("bribe_button_$amountMillions")
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 14.sp
            )
            Text(
                text = chance,
                color = Color(0xFFA5D6A7),
                fontSize = 9.sp
            )
        }
    }
}
