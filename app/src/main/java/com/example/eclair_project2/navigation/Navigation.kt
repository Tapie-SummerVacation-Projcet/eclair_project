package com.example.eclair_project2.navigation

import SolutionWriteScreen
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eclair_project2.components.navigation.BottomNavigationBar
import com.example.eclair_project2.components.screen.*
import com.example.eclair_project2.fragment.CommunityScreen
import com.example.eclair_project2.fragment.DiaryListScreen
import com.example.eclair_project2.fragment.DiaryViewModel
import com.example.eclair_project2.fragment.DiaryWriteScreen
import com.example.eclair_project2.fragment.EmotionScreen
import com.example.eclair_project2.fragment.HomeScreen
import com.example.eclair_project2.fragment.ShareDiaryOrSolutionScreen
import com.example.eclair_project2.fragment.SharedContentDetailScreen
import com.example.eclair_project2.fragment.Starting

sealed class Screen(val route: String) {
    object Start : Screen("start")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object Emotion : Screen("emotion/{diaryIdParams}")
    object Diary : Screen("diary")
    object DiaryWrite : Screen("diary_write")
    object SolutionList : Screen("solutionList")
    object SolutionWrite : Screen("solutionWrite/{diaryId}")
    object Community : Screen("community")
    object ShareDiaryOrSolution : Screen("shareDiaryOrSolution")
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
                        composable(
                            route = Screen.Emotion.route,
                            arguments = listOf(navArgument("diaryIdParams") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val diaryIdParams = backStackEntry.arguments?.getString("diaryIdParams") ?: ""
                            Log.d("EmotionScreen", "Received diaryIdParams: $diaryIdParams")
                            EmotionScreen(navController, diaryIdParams)
                        }
                        composable(Screen.Diary.route) { DiaryListScreen(navController) }
                        composable(Screen.DiaryWrite.route) { DiaryWriteScreen(navController) }
                        composable(
                            route = Screen.SolutionWrite.route,
                            arguments = listOf(navArgument("diaryId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val diaryId = backStackEntry.arguments?.getString("diaryId") ?: ""
                            Log.d("SolutionWriteScreen", "Received diaryId: $diaryId")
                            SolutionWriteScreen(navController, diaryId)
                        }
                        composable(Screen.Community.route) {
                            CommunityScreen(navController)
                        }
                        composable(
                            route = Screen.ShareDiaryOrSolution.route
                        ) {
                            ShareDiaryOrSolutionScreen(navController)
                        }
                        composable("sharedContentDetail/{sharedContentId}") { backStackEntry ->
                            val sharedContentId = backStackEntry.arguments?.getString("sharedContentId") ?: return@composable
                            SharedContentDetailScreen(navController, sharedContentId)
                        }
                    }
                }
            }
        }
    )
}