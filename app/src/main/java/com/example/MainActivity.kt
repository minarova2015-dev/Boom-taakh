package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.AuctionGameScreen
import com.example.ui.screens.CardsUnlockProgressScreen
import com.example.ui.screens.DailyChallengesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.LoginProfileScreen
import com.example.ui.screens.MatchShowdownScreen
import com.example.ui.screens.MultiplayerLobbyScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                BoomTaakhApp()
            }
        }
    }
}

@Composable
fun BoomTaakhApp(viewModel: GameViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val auctionState by viewModel.auctionState.collectAsStateWithLifecycle()
    val showdownResult by viewModel.showdownResult.collectAsStateWithLifecycle()
    val dailyChallenges by viewModel.dailyChallenges.collectAsStateWithLifecycle()
    val isVoiceMuted by viewModel.isVoiceChatMuted.collectAsStateWithLifecycle()
    val activeSpeaker by viewModel.voiceChatActiveSpeaker.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val devUnlocked by viewModel.devUnlockAllCards.collectAsStateWithLifecycle()
    val actionFeedback by viewModel.actionFeedback.collectAsStateWithLifecycle()
    val allAccounts by viewModel.allAccounts.collectAsStateWithLifecycle()

    val latestVoiceMsg = chatMessages.lastOrNull { it.isVoiceTranscript }?.text

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars),
        containerColor = Color(0xFF0D131F)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF0D131F))
        ) {
            when (currentScreen) {
                AppScreen.AUTH_SCREEN -> com.example.ui.screens.AuthScreen(
                    viewModel = viewModel,
                    allAccounts = allAccounts
                )
                AppScreen.HOME -> HomeScreen(
                    viewModel = viewModel,
                    user = userProfile,
                    currentLanguage = currentLanguage,
                    devUnlocked = devUnlocked
                )
                AppScreen.AUCTION_ROOM -> AuctionGameScreen(
                    viewModel = viewModel,
                    auctionState = auctionState,
                    user = userProfile,
                    currentLanguage = currentLanguage,
                    devUnlocked = devUnlocked,
                    actionFeedback = actionFeedback,
                    isVoiceMuted = isVoiceMuted,
                    activeSpeaker = activeSpeaker,
                    latestVoiceMessage = latestVoiceMsg
                )
                AppScreen.MATCH_SHOWDOWN -> MatchShowdownScreen(
                    viewModel = viewModel,
                    auctionState = auctionState,
                    result = showdownResult
                )
                AppScreen.MULTIPLAYER_LOBBY -> MultiplayerLobbyScreen(
                    viewModel = viewModel,
                    user = userProfile,
                    isVoiceMuted = isVoiceMuted,
                    activeSpeaker = activeSpeaker
                )
                AppScreen.LEADERBOARDS -> LeaderboardScreen(
                    viewModel = viewModel,
                    user = userProfile
                )
                AppScreen.DAILY_CHALLENGES -> DailyChallengesScreen(
                    viewModel = viewModel,
                    challenges = dailyChallenges
                )
                AppScreen.CARDS_PROGRESSION -> CardsUnlockProgressScreen(
                    viewModel = viewModel,
                    user = userProfile,
                    devUnlocked = devUnlocked
                )
                AppScreen.LOGIN_PROFILE -> LoginProfileScreen(
                    viewModel = viewModel,
                    user = userProfile
                )
            }
        }
    }
}
