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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserEntity
import com.example.viewmodel.AppScreen
import com.example.viewmodel.GameViewModel

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val clubName: String,
    val countryFlag: String,
    val bestOvr: Int,
    val totalWins: Int,
    val isUser: Boolean = false
)

@Composable
fun LeaderboardScreen(
    viewModel: GameViewModel,
    user: UserEntity?
) {
    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    val userWins = user?.careerWins ?: 5
    val userBestOvr = if ((user?.highestSquadOvr ?: 0) > 0) user!!.highestSquadOvr else 88

    val leaderboard = listOf(
        LeaderboardEntry(1, "SultanOfGoals", "Galácticos FC", "🇸🇦", 92, 48),
        LeaderboardEntry(2, "StrikerLegend", "Al Hilal Stars", "🇦🇪", 91, 42),
        LeaderboardEntry(3, "TikiTakaMaster", "Catalan Dream", "🇪🇸", 90, 39),
        LeaderboardEntry(4, user?.username ?: "You", user?.clubName ?: "Boom Taakh FC", "⚽", userBestOvr, userWins, isUser = true),
        LeaderboardEntry(5, "LondonCannon", "Highbury Elite", "🇬🇧", 89, 31),
        LeaderboardEntry(6, "BavarianMachine", "Munich Blitz", "🇩🇪", 88, 27),
        LeaderboardEntry(7, "SambaMagic", "Maracanã Boys", "🇧🇷", 88, 24),
        LeaderboardEntry(8, "DesertFalcon", "Riyadh Titans", "🇸🇦", 87, 21),
        LeaderboardEntry(9, "ParisianPrince", "Parc des Princes", "🇫🇷", 87, 18),
        LeaderboardEntry(10, "Milanista99", "San Siro Glory", "🇮🇹", 86, 15)
    ).sortedByDescending { it.bestOvr * 100 + it.totalWins }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D131F))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    modifier = Modifier.testTag("back_from_leaderboard_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Global Squad Leaderboards",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
            }
        }

        itemsIndexed(leaderboard) { index, entry ->
            val rankColor = when (index) {
                0 -> Color(0xFFFFD700)
                1 -> Color(0xFFC0C0C0)
                2 -> Color(0xFFCD7F32)
                else -> Color.White
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (entry.isUser) Color(0xFF1E3A28) else Color(0xFF172033)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        if (entry.isUser) 1.5.dp else 1.dp,
                        if (entry.isUser) Color(0xFF00E676) else Color(0xFF26324A),
                        RoundedCornerShape(12.dp)
                    )
                    .testTag("leaderboard_item_${index + 1}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = rankColor.copy(alpha = 0.2f),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "#${index + 1}",
                                    color = rankColor,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${entry.countryFlag} ${entry.name}",
                                    color = if (entry.isUser) Color(0xFF00E676) else Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                if (entry.isUser) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFF00C853)
                                    ) {
                                        Text(
                                            "YOU",
                                            color = Color.Black,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 8.sp,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "${entry.clubName} • ${entry.totalWins} Career Wins",
                                color = Color(0xFFA0B0C8),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFFD700)
                    ) {
                        Text(
                            text = "${entry.bestOvr} OVR",
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
