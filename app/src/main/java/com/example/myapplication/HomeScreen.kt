package com.example.myapplication

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen(navController: NavController){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Button(
            onClick = { navController.navigate(route = Screen.ConversationScreen.route) }
        ) {
            Text(
                text = "Start",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
@Preview
fun HomeScreenPreview(){
    HomeScreen(
        navController = rememberNavController()
    )
}
