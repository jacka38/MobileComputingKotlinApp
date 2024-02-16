package com.example.myapplication

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.core.app.NotificationCompat
import kotlin.math.abs

class RotationSensor(private val context: Context, private val sensorManager: SensorManager) :
    SensorEventListener {

    private val rotationSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    private var lastNotifiedRotation: Float = 0f
    private val rotationThreshold = 45

    init {
        sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.values?.let { rotationVector ->
            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector)

            val adjustedRotationMatrix = FloatArray(9)
            SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, adjustedRotationMatrix)

            val orientation = FloatArray(3)
            SensorManager.getOrientation(adjustedRotationMatrix, orientation)

            val rotation = Math.toDegrees(orientation[0].toDouble()).toFloat()
            val rotationDifference = abs(rotation - lastNotifiedRotation)

            if (rotationDifference > rotationThreshold) {
                lastNotifiedRotation = rotation
                showNotification(context, "Phone rotated more than 45 degrees")
            }
        }
    }

    private fun showNotification(context: Context, message: String) {
        if (AppSettings.areNotificationsAllowed) {
            // Create an explicit intent for an Activity in your app.
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(context, "channel_id")
                .setContentTitle("WOW SPINNING")
                .setContentText("That phone really doing tricks")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
            notificationManager.notify(1, notification)
        }
    }

    fun unregister() {
        sensorManager.unregisterListener(this)
    }
}