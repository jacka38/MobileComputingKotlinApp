package com.example.myapplication


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapplication.ui.theme.MyApplicationTheme
import android.os.Build
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room

/*
 * This code was made using a tutorial:
 * Jetpack Compose Tutorial
 * https://developer.android.com/jetpack/compose/tutorial
 * 10.1.2024
 */

class MainActivity : ComponentActivity() {

    lateinit var db: AppDatabase
    lateinit var navController: NavHostController
    lateinit var rotationSensor: RotationSensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id",
                "Channel name",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            rotationSensor = RotationSensor(this, sensorManager)
        }

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()

        setContent {
            MyApplicationTheme {

                navController = rememberNavController()
                SetupNavGraph(navController = navController, db = db)

            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        rotationSensor.unregister()
    }
}