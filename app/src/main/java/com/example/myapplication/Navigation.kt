package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


/*
I used the navigation tutorial from this site
https://developer.android.com/guide/navigation/use-graph/navigate

Searched on 24.1.2024
*/

@Composable
fun SetupNavGraph(
    navController: NavHostController = rememberNavController(),
    db: AppDatabase
) {
    // Initialize the ViewModelStore
    val viewModelStore = remember { ViewModelStore() }

    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(
            route = Screen.HomeScreen.route
        ) {
            HomeScreen(navController, db)
        }
        composable(
            route = Screen.ConversationScreen.route
        ) {
            ConversationScreen(navController, db)
        }
        composable(
            route = Screen.VideoScreen.route
        ) {
            VideoScreen(videoUri)
        }
    }
}



