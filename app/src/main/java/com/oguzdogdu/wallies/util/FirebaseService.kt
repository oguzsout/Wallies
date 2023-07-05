package com.oguzdogdu.wallies.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.presentation.main.MainActivity

const val channelId = "notification_channel"
const val channelName = "com.oguzdogdu.wallies"

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.notification != null) {
            generateNotification(
                title = message.notification!!.title,
                message = message.notification!!.body
            )
        }
    }

    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title: String?, message: String?): RemoteViews {
        val remoteView = RemoteViews("com.oguzdogdu.wallies", R.layout.notification)
        remoteView.setTextViewText(R.id.textViewNotificationTitle, title)
        remoteView.setTextViewText(R.id.textViewNotificationMessage, message)
        remoteView.setImageViewResource(R.id.imageViewNotification, R.drawable.logo)
        return remoteView
    }

    private fun generateNotification(title: String?, message: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            channelId
        )
            .setSmallIcon(R.drawable.logo)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, message))

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())
    }
}
