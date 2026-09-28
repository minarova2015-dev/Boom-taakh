package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserEntity
import com.example.viewmodel.AppScreen
import com.example.viewmodel.GameViewModel

@Composable
fun AuthScreen(
    viewModel: GameViewModel,
    allAccounts: List<UserEntity>
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Log In, 1 = Sign Up

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var signUpUsername by remember { mutableStateOf("") }
    var signUpEmail by remember { mutableStateOf("") }
    var signUpPassword by remember { mutableStateOf("") }
    var signUpClubName by remember { mutableStateOf("") }
    var signUpAvatar by remember { mutableStateOf("🦁") }

    val availableAvatars = listOf("🦁", "⚽", "🦅", "👑", "⚡", "🔥", "🏆", "💎", "⭐", "🐺")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D131F))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            // Logo / Branding Header
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFB703)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "⚽", fontSize = 36.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "BOOM TAAKH!",
                color = Color(0xFFFFD700),
                fontWeight = FontWeight.Black,
                fontSize = 24.sp
            )

            Text(
                text = "Club Manager Authentication & Database Saving",
                color = Color(0xFFA0B0C8),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }

        // Database Auto-Save Status Banner
        item {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF16251E),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF00E676), RoundedCornerShape(10.dp))
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Storage,
                        contentDescription = "Database saving",
                        tint = Color(0xFF00E676),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "SQLite Database Saving System Active",
                            color = Color(0xFF00E676),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "All coins, wins, squads, bribes & card unlocks save automatically",
                            color = Color(0xFFCFD8DC),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        // Tabs: Log In vs Create Account
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161F33)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, Color(0xFFFFB703), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
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
                                Text(
                                    "Log In",
                                    fontWeight = FontWeight.Black,
                                    color = if (selectedTab == 0) Color(0xFFFFB703) else Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = {
                                Text(
                                    "Sign Up",
                                    fontWeight = FontWeight.Black,
                                    color = if (selectedTab == 1) Color(0xFFFFB703) else Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (selectedTab == 0) {
                        // Log In Form
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username / Manager Name", color = Color(0xFFA0B0C8)) },
                            singleLine = true,
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFFFB703))
                            },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color(0xFF1E283C),
                                unfocusedContainerColor = Color(0xFF1E283C)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_username_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password", color = Color(0xFFA0B0C8)) },
                            singleLine = true,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFFFFB703))
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle password",
                                        tint = Color.Gray
                                    )
                                }
                            },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color(0xFF1E283C),
                                unfocusedContainerColor = Color(0xFF1E283C)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_password_input")
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                viewModel.login(username, password) { success, msg ->
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB703)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("login_submit_button")
                        ) {
                            Text("LOG IN & LOAD PROFILE", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 14.sp)
                        }
                    } else {
                        // Sign Up Form
                        OutlinedTextField(
                            value = signUpUsername,
                            onValueChange = { signUpUsername = it },
                            label = { Text("Username", color = Color(0xFFA0B0C8)) },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFFFB703)) },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color(0xFF1E283C),
                                unfocusedContainerColor = Color(0xFF1E283C)
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("signup_username_input")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = signUpClubName,
                            onValueChange = { signUpClubName = it },
                            label = { Text("Club Name (e.g. Boom Taakh FC)", color = Color(0xFFA0B0C8)) },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFFFFB703)) },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color(0xFF1E283C),
                                unfocusedContainerColor = Color(0xFF1E283C)
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("signup_club_input")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = signUpPassword,
                            onValueChange = { signUpPassword = it },
                            label = { Text("Create Password", color = Color(0xFFA0B0C8)) },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFFFFB703)) },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color(0xFF1E283C),
                                unfocusedContainerColor = Color(0xFF1E283C)
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("signup_password_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Select Club Crest Emoji:", color = Color(0xFFA0B0C8), fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(availableAvatars) { avatar ->
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(if (avatar == signUpAvatar) Color(0xFFFFB703) else Color(0xFF222C42))
                                        .clickable { signUpAvatar = avatar },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(avatar, fontSize = 20.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                viewModel.signUp(
                                    username = signUpUsername,
                                    email = signUpEmail,
                                    password = signUpPassword,
                                    clubName = signUpClubName,
                                    avatarEmoji = signUpAvatar
                                ) { success, msg ->
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("signup_submit_button")
                        ) {
                            Text("CREATE ACCOUNT & START PLAYING", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Quick Guest Mode / Instant Play
        item {
            OutlinedButton(
                onClick = {
                    viewModel.continueAsGuest()
                    Toast.makeText(context, "Logged in as Guest Manager!", Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("guest_login_button")
            ) {
                Text("CONTINUE AS GUEST (QUICK START) →", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        // Saved Accounts List (Multi-Account Switcher)
        if (allAccounts.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF141A28)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Saved Accounts in Database (${allAccounts.size})",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        allAccounts.forEach { acc ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(Color(0xFF1F283C), RoundedCornerShape(10.dp))
                                    .clickable {
                                        viewModel.switchAccount(acc.id)
                                        Toast.makeText(context, "Switched to @${acc.username}", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(acc.avatarEmoji, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(acc.username, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text("${acc.clubName} • 🪙 ${acc.careerCoins} • 🏆 ${acc.careerWins}W", color = Color(0xFFA0B0C8), fontSize = 10.sp)
                                    }
                                }
                                Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFFFB703)) {
                                    Text("Switch", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
