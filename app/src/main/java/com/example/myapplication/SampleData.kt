package com.example.myapplication

import com.example.myapplication.Message

/**
 * SampleData for Jetpack Compose Tutorial
 */
object SampleData {
    // Sample conversation data
    val conversationSample = listOf(
        Message(
            "Jack",
            "Test...Test...Test...",
            "profilePictureUri"
        ),
        Message(
            "Jack",
            """List of Android versions:
            |Android KitKat (API 19)
            |Android Lollipop (API 21)
            |Android Marshmallow (API 23)
            |Android Nougat (API 24)
            |Android Oreo (API 26)
            |Android Pie (API 28)
            |Android 10 (API 29)
            |Android 11 (API 30)
            |Android 12 (API 31)""".trim(),
            "profilePictureUri"
        ),
        Message(
            "Jack",
            """I think Kotlin is my favorite programming language.
            |It's so much fun!""".trim(),
            "profilePictureUri"
        ),
        Message(
            "Jack",
            "Searching for alternatives to XML layouts...",
            "profilePictureUri"
        ),
        Message(
            "Jack",
            """Hey, take a look at Jetpack Compose, it's great!
            |It's the Android's modern toolkit for building native UI.
            |It simplifies and accelerates UI development on Android.
            |Less code, powerful tools, and intuitive Kotlin APIs :)""".trim(),
            "profilePictureUri"
        ),
        Message(
            "Jack",
            "It's available from API 21+ :)",
            "profilePictureUri"
        ),
        Message(
            "Jack",
            "Writing Kotlin for UI seems so natural, Compose where have you been all my life?",
            "profilePictureUri"
        ),
        Message(
            "Jack",
            "Android Studio next version's name is Arctic Fox",
            "profilePictureUri"
        ),
        Message(
            "Jack",
            "Android Studio Arctic Fox tooling for Compose is top notch ^_^",
            "profilePictureUri"
        ),
        Message(
            "Jack",
            "I didn't know you can now run the emulator directly from Android Studio",
            "profilePictureUri"
        ),
        Message(
            "Jack",
            "Compose Previews are great to check quickly how a composable layout looks like",
            "profilePictureUri"
        ),
        Message(
            "Jack",
            "Previews are also interactive after enabling the experimental setting",
            "profilePictureUri"
        ),
        Message(
            "Jack",
            "Have you tried writing build.gradle with KTS?",
            "profilePictureUri"
        ),
    )
}