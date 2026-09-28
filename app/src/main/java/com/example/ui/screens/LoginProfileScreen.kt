package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserEntity
import com.example.viewmodel.AppScreen
import com.example.viewmodel.GameViewModel

@Composable
fun LoginProfileScreen(
    viewModel: GameViewModel,
    user: UserEntity?
) {
    val context = LocalContext.current
    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    var username by remember(user) { mutableStateOf(user?.username ?: "StrikerKing") }
    var clubName by remember(user) { mutableStateOf(user?.clubName ?: "Boom Taakh FC") }
    var selectedAvatar by remember(user) { mutableStateOf(user?.avatarEmoji ?: "🦁") }

    val availableAvatars = listOf("🦁", "⚽", "🦅", "👑", "⚡", "🔥", "🏆", "💎", "⭐", "🐺")

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
                    modifier = Modifier.testTag("back_from_profile_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Club Manager Profile & Stats",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
            }
        }

        // Profile Edit Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161F33)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, Color(0xFFFFB703), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFB703)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = selectedAvatar, fontSize = 40.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Choose Club Crest Avatar",
                        color = Color(0xFFA0B0C8),
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(availableAvatars) { avatar ->
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(if (avatar == selectedAvatar) Color(0xFFFFB703) else Color(0xFF222C42))
                                    .border(
                                        2.dp,
                                        if (avatar == selectedAvatar) Color.White else Color.Transparent,
                                        CircleShape
                                    )
                                    .clickable { selectedAvatar = avatar },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(avatar, fontSize = 22.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Manager Name", color = Color(0xFFA0B0C8)) },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color(0xFF1E283C),
                            unfocusedContainerColor = Color(0xFF1E283C)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("username_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = clubName,
                        onValueChange = { clubName = it },
                        label = { Text("Club Name", color = Color(0xFFA0B0C8)) },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color(0xFF1E283C),
                            unfocusedContainerColor = Color(0xFF1E283C)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("club_name_input")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            viewModel.updateProfile(username, clubName, selectedAvatar)
                            Toast.makeText(context, "Profile Saved to Database!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB703)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("save_profile_button")
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SAVE PROFILE & CLUB", color = Color.Black, fontWeight = FontWeight.Black)
                    }
                }
            }
        }

        // Career Statistics
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161F33)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Career Statistics",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val wins = user?.careerWins ?: 0
                    val losses = user?.careerLosses ?: 0
                    val totalGames = wins + losses
                    val winRate = if (totalGames > 0) ((wins.toDouble() / totalGames) * 100).toInt() else 0

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatTile(label = "Matches Won", value = "$wins", color = Color(0xFF00E676), modifier = Modifier.weight(1f))
                        StatTile(label = "Matches Lost", value = "$losses", color = Color(0xFFFF5252), modifier = Modifier.weight(1f))
                        StatTile(label = "Win Rate", value = "$winRate%", color = Color(0xFFFFD700), modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatTile(label = "Best Squad OVR", value = "${user?.highestSquadOvr ?: 0}", color = Color(0xFFFFB703), modifier = Modifier.weight(1f))
                        StatTile(label = "Club Coins", value = "🪙 ${user?.careerCoins ?: 500}", color = Color(0xFFFFD54F), modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.resetCareerStats()
                                Toast.makeText(context, "Stats reset to 0 Wins and 500 Coins!", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Reset Stats (0 Wins / 500 Coins)", color = Color(0xFFFF8A80), fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Bribe Track Record
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF1E283C),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Referee Bribe Record", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("Envelopes given on the sideline", color = Color(0xFFA0B0C8), fontSize = 10.sp)
                            }
                            Text(
                                text = "${user?.bribesSuccessful ?: 0} Successful / ${user?.bribesAttempted ?: 0} Paid",
                                color = Color(0xFFFFD700),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Account & Database Saving Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.manualSaveGame { msg ->
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("💾 Save Game", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Button(
                            onClick = { viewModel.navigateTo(AppScreen.AUTH_SCREEN) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1.3f)
                        ) {
                            Text("Switch Account / Log In", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun StatTile(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF1E283C),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, color = color, fontWeight = FontWeight.Black, fontSize = 16.sp)
            Text(label, color = Color(0xFFA0B0C8), fontSize = 10.sp)
        }
    }
}
