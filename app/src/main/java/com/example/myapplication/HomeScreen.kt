package com.example.myapplication

import android.Manifest
import android.app.AlertDialog
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.AppSettings.areNotificationsAllowed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                    AlertDialog.Builder(context)
                        .setTitle("Allow Notifications")
                        .setMessage("Do you want to allow notifications?")
                        .setPositiveButton("Yes") { _, _ ->
                            areNotificationsAllowed = true
                            // Open the app's system settings to allow the user to enable notifications
                            val intent = Intent().apply {
                                action = "android.settings.APP_NOTIFICATION_SETTINGS"
                                putExtra("app_package", context.packageName)
                                putExtra("app_uid", context.applicationInfo.uid)
                                putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
                            }
                            context.startActivity(intent)
                        }
                        .setNegativeButton("No", null)
                        .show()
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
object AppSettings {
    var areNotificationsAllowed = false
}