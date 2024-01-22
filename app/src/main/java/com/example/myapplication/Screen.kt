package com.example.myapplication

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object ConversationScreen : Screen("conversation_screen")
}
