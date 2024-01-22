package com.example.myapplication

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun ConversationScreen(navController: NavHostController) {
    MyApplicationTheme {
        Column {
            Button(onClick = { navController.popBackStack() }) {
                Text(
                    text = "Go Back",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleSmall
                    )
            }
            Conversation(SampleData.conversationSample)
        }
    }
}

data class Message(val author: String, val body: String)

@Composable
fun MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {  // Adds padding around the message
        Image(
            painter = painterResource(R.drawable.profile_pic),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(40.dp)    // Sets image size to 40 dp
                .clip(CircleShape)  // Clips image to have rounded corners
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape) //Colorful border for profile pic
        )

        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }    //Tracks if the message is expanded or not in this

        val surfaceColor by animateColorAsState(    //surfaceColors gradual update when clicked on
            if (isExpanded) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
            label = "",
        )

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {    //Toggles the isExpanded variable when clicking on this Column
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))    // Adds a vertical space between the author and message texts

            Surface(shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1, //If not expanded show only 1 line
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