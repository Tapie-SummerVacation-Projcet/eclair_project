package com.example.eclair_project2.navigation

import SolutionWriteScreen
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.eclair_project2.components.navigation.BottomNavigationBar

import com.example.eclair_project2.components.screen.LoginScreen
import com.example.eclair_project2.components.screen.SignUpScreen
import com.example.eclair_project2.fragment.CommunityScren
import com.example.eclair_project2.fragment.DiaryViewModel
import com.example.eclair_project2.fragment.DiaryWriteScreen
import com.example.eclair_project2.fragment.EmotionScreen
import com.example.eclair_project2.fragment.HomeScreen
import com.example.eclair_project2.fragment.ProblemSolvingScreen
import com.example.eclair_project2.fragment.SolutionListScreen
import com.example.eclair_project2.fragment.Starting


sealed class Screen(val route: String) {
    object Start : Screen("start")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object Emotion : Screen("emotion/{diaryId}")
    object Diary : Screen("diary")
    object DiaryWrite : Screen("diary_write")
    object SolutionList : Screen("solutionList/{diaryId}")
    object SolutionWrite : Screen("solutionWrite/{diaryId}")
    object Community : Screen("community")
}

@Composable
fun Navigation(viewModel: DiaryViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Login.route && currentRoute != Screen.SignUp.route && currentRoute != Screen.Start.route) {
                BottomNavigationBar(navController = navController, viewModel = viewModel)
            }
        },
        content = { innerPadding ->
                Row(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        NavHost(navController, startDestination = Screen.Start.route) {
                            composable(Screen.Start.route) { Starting(navController) }
                            composable(Screen.Login.route) { LoginScreen(navController) }
                            composable(Screen.SignUp.route) { SignUpScreen(navController) }
                            composable(Screen.Home.route) { HomeScreen(navController) }
                            composable(Screen.Emotion.route) { backStackEntry ->
                                val diaryId = backStackEntry.arguments?.getString("diaryId") ?: ""
                                EmotionScreen(navController, diaryId)
                            }
                            composable(Screen.Diary.route) { ProblemSolvingScreen(navController) }
                            composable(Screen.DiaryWrite.route) { DiaryWriteScreen(navController) }
                            composable(Screen.SolutionList.route) { backStackEntry ->
                                val diaryId = backStackEntry.arguments?.getString("diaryId") ?: ""
                                SolutionListScreen(navController, diaryId)
                            }
                            composable(Screen.SolutionWrite.route) { backStackEntry ->
                                val diaryId = backStackEntry.arguments?.getString("diaryId") ?: ""
                                SolutionWriteScreen(navController, diaryId)
                            }
                            composable(Screen.Community.route) { CommunityScren(navController) }
                        }
                    }
                }
        }
    )
}