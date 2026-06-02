package com.skeypro.notiflistener.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.skeypro.notiflistener.config.Config
import com.skeypro.notiflistener.network.WebhookClient
import org.json.JSONObject
import kotlin.concurrent.thread

class NotificationService : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()

        Log.d(
            "SKEYPRO",
            "Notification Listener Connected"
        )
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        try {

            val extras = sbn.notification.extras

            val title = extras
                ?.getCharSequence("android.title")
                ?.toString()
                ?: ""

            val text = extras
                ?.getCharSequence("android.text")
                ?.toString()
                ?: ""

            val packageName = sbn.packageName

            val payload = JSONObject().apply {

                put("package", packageName)
                put("title", title)
                put("text", text)
                put("timestamp", System.currentTimeMillis())

            }.toString()

            Log.d(
                "SKEYPRO",
                payload
            )

            thread {

                val success = WebhookClient.send(
                    Config.WEBHOOK_URL,
                    payload
                )

                Log.d(
                    "SKEYPRO",
                    "Webhook Result = $success"
                )

            }

        } catch (e: Exception) {

            Log.e(
                "SKEYPRO",
                "Notification Error",
                e
            )

        }
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()

        Log.d(
            "SKEYPRO",
            "Notification Listener Disconnected"
        )
    }
}