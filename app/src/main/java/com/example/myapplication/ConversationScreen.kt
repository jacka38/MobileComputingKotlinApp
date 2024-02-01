package com.example.myapplication

import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ConversationScreen(navController: NavHostController, db: AppDatabase) {
    var user by remember { mutableStateOf<User?>(null) }
    var profilePictureUri by remember { mutableStateOf<Uri?>(null) }
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }

    val viewModelStore = remember { ViewModelStore() }

    LaunchedEffect(key1 = db) {
        user = withContext(Dispatchers.IO) {
            db.userDao().getUser()
        }
    }

    LaunchedEffect(key1 = user) {
        if (user != null) {
            messages = SampleData.conversationSample.map {
                Message(
                    author = user?.name ?: "Unknown",
                    body = it.body,
                    profilePictureUri = user?.profilePictureUri
                )
            }
        }
    }

    MyApplicationTheme {
        Column {
            Button(onClick = { navController.popBackStack() }) {
                Text(
                    text = "Go Back",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            // Display the profile picture if available
            if (messages.isEmpty()) {
                user?.profilePictureUri?.let { uri ->
                    Image(
                        painter = rememberImagePainter(data = uri),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(40.dp)    // Sets image size to 40 dp
                            .clip(CircleShape)  // Clips image to have rounded corners
                            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape) //Colorful border for profile pic
                    )
                }
            }

            // Use the messages from the database
            Conversation(messages)
        }
    }
}


data class Message(val author: String, val body: String, val profilePictureUri: String?)


@Composable
fun MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        // Display the profile picture if available
        msg.profilePictureUri?.let { uri ->
            Image(
                painter = rememberImagePainter(data = Uri.parse(uri)),
                contentDescription = "Contact profile picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }

        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
            label = "",
        )

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Row {
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = msg.author,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}



@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(message)
        }
    }
}

@Preview
@Composable
fun ConversationScreenPreview() {
    MyApplicationTheme {
        Conversation(SampleData.conversationSample)
    }
}