package com.example.myapplication

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// ...

@Composable
fun HomeScreen(navController: NavHostController, db: AppDatabase) {
    var name by remember { mutableStateOf("") }
    var profilePictureUri by remember { mutableStateOf<Uri?>(null) }
    var user by remember { mutableStateOf<User?>(null) }

    // Load user data if it already exists in the database
    LaunchedEffect(key1 = db) {
        user = withContext(Dispatchers.IO) {
            db.userDao().getUser()
        }

        // Set the initial values if the user exists
        user?.let {
            name = it.name
            profilePictureUri = Uri.parse(it.profilePictureUri)
        }
    }

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        profilePictureUri = uri
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Text input for entering name
            TextField(
                value = name,
                onValueChange = { newName ->
                    name = newName
                    user = User(name = newName, profilePictureUri = profilePictureUri.toString())
                },
                label = { Text("Enter your name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Image picking
            Image(
                painter = if (profilePictureUri != null) {
                    rememberAsyncImagePainter(profilePictureUri)
                } else {
                    painterResource(R.drawable.profile_pic)
                },
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .clickable {
                        // Open image picker
                        imagePickerLauncher.launch("image/*")
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Start conversation button
            Button(
                onClick = {
                    val newUser = User(
                        name = name,
                        profilePictureUri = profilePictureUri.toString()
                    )

                    // Use a coroutine to insert the new user into the database
                    CoroutineScope(Dispatchers.IO).launch {
                        db.userDao().insert(newUser)
                        println("User inserted: $newUser")
                    }

                    // Navigate to the ConversationScreen after insertion
                    navController.navigate(route = Screen.ConversationScreen.route)
                }
            ) {
                Text(
                    text = "Start Conversation",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            val context = LocalContext.current // Get the local context

            var hasNotificationPermission by remember {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    mutableStateOf(
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                } else mutableStateOf(true)
            }

            //Allow Notifications
            Button(
                onClick = {
                    showNotification(context)
                }
            ) {
                Text(
                    text = "Allow Notifications",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

private fun showNotification(context: Context){

    /*
    * This code was made using a tutorial:
    * Create a notification
    * https://developer.android.com/develop/ui/views/notifications/build-notification#kotlin
    * 7.2.2024
    */

    // Create an explicit intent for an Activity in your app.
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notification = NotificationCompat.Builder(context, "channel_id")
        .setContentText("here some text")
        .setContentTitle("Title")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()
    notificationManager.notify(1, notification)
}