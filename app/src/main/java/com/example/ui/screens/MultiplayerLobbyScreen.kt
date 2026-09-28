package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserEntity
import com.example.model.GameMode
import com.example.ui.components.VoiceChatBar
import com.example.viewmodel.AppScreen
import com.example.viewmodel.GameViewModel

@Composable
fun MultiplayerLobbyScreen(
    viewModel: GameViewModel,
    user: UserEntity?,
    isVoiceMuted: Boolean,
    activeSpeaker: String?
) {
    val context = LocalContext.current
    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    var roomCode by remember { mutableStateOf("BT-${(1000..9999).random()}") }
    var inputJoinCode by remember { mutableStateOf("") }
    var isOpponentJoined by remember { mutableStateOf(true) }

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
                    modifier = Modifier.testTag("back_from_lobby_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Multiplayer Arena & Voice Chat",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
            }
        }

        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF161E2E)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            "Create Room",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == 0) Color(0xFFFFB703) else Color.Gray
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            "Join With Code",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == 1) Color(0xFFFFB703) else Color.Gray
                        )
                    }
                )
            }
        }

        if (selectedTab == 0) {
            // Create Room Tab
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF151C2C)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, Color(0xFFFFB703), RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ROOM CODE",
                            color = Color(0xFFA0B0C8),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = roomCode,
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Black,
                            fontSize = 32.sp,
                            letterSpacing = 4.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Boom Taakh Room", roomCode))
                                    Toast.makeText(context, "Room Code Copied to Clipboard!", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Copy Code", color = Color.White, fontSize = 12.sp)
                            }

                            Button(
                                onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "Join my Boom Taakh Football Auction match! Room Code: $roomCode ⚽ Let's see who builds the best squad!"
                                        )
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Invite Friend to Boom Taakh"))
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Invite Friend", color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Players in Room
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF172033)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Lobby Players (2/2 Ready)",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Player 1 (You)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF222B3D), RoundedCornerShape(10.dp))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(user?.avatarEmoji ?: "⚽", fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("${user?.username ?: "Player"} (Host)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(user?.clubName ?: "Boom Taakh FC", color = Color(0xFFA0B0C8), fontSize = 11.sp)
                                }
                            }
                            Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFF00C853)) {
                                Text("READY", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Player 2 (Friend / Online Challenger)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF222B3D), RoundedCornerShape(10.dp))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🔥", fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Rival_Master99", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Ping: 24ms • Voice Active", color = Color(0xFF00E676), fontSize = 11.sp)
                                }
                            }
                            Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFF00C853)) {
                                Text("READY", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                    }
                }
            }

            // Start Match Button
            item {
                Button(
                    onClick = { viewModel.startNewMatch(mode = GameMode.ONLINE_MULTIPLAYER, roomCode = roomCode) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB703)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("start_multiplayer_auction_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "START MULTIPLAYER AUCTION ⚽",
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        } else {
            // Join With Code Tab
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF151C2C)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Enter Friend's Room Code",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = inputJoinCode,
                            onValueChange = { inputJoinCode = it.uppercase() },
                            placeholder = { Text("e.g. BT-4821", color = Color.Gray) },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color(0xFF1E283C),
                                unfocusedContainerColor = Color(0xFF1E283C)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("room_code_input")
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                if (inputJoinCode.isNotBlank()) {
                                    viewModel.startNewMatch(mode = GameMode.ONLINE_MULTIPLAYER, roomCode = inputJoinCode)
                                } else {
                                    Toast.makeText(context, "Please enter a valid room code", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("join_room_button")
                        ) {
                            Text("CONNECT & JOIN ROOM", color = Color.Black, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // Voice Chat Bar Integration
        item {
            VoiceChatBar(
                isMuted = isVoiceMuted,
                activeSpeaker = activeSpeaker,
                latestTranscript = "Voice room connected! Tap to speak with friends.",
                onToggleMute = { viewModel.toggleVoiceMute() }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
