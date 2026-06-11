package id.skeypro.soundbox.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import id.skeypro.soundbox.config.Config
import id.skeypro.soundbox.network.WebhookClient
import id.skeypro.soundbox.utils.PrefHelper
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

    override fun onNotificationPosted(
        sbn: StatusBarNotification
    ) {

        super.onNotificationPosted(sbn)
        try {

            val extras =
                sbn.notification.extras

            val title = extras
                ?.getCharSequence(
                    "android.title"
                )
                ?.toString()
                ?: ""

            val text = extras
                ?.getCharSequence(
                    "android.text"
                )
                ?.toString()
                ?: ""

            val packageName =
                sbn.packageName

            if (
                !PrefHelper.isRegistered(
                    this
                )
            ) {
                return
            }

            val deviceId =
                PrefHelper.getDeviceId(
                    this
                )

            if (
                deviceId.isBlank()
            ) {
                return
            }

            val allowedPackages =
                PrefHelper.getAllowedPackages(
                    this
                )

            if (

                allowedPackages.isNotEmpty()

                &&

                packageName !in allowedPackages

            ) {

                Log.d(
                    "SKEYPRO",
                    "Ignored Package: $packageName"
                )
                return
            }

            val content =
    (title + " " + text)
        .lowercase()

val keywords =
    PrefHelper.getKeywords(
        this
    )

val blockedWords =
    PrefHelper.getBlockedWords(
        this
    )

if (
    blockedWords.isNotEmpty()
    &&
    blockedWords.any {
        content.contains(it)
    }
) {

    Log.d(
        "SKEYPRO",
        "Blocked Content: $title | $text"
    )
    return
}

if (
    keywords.isNotEmpty()
    &&
    !keywords.any {
        content.contains(it)
    }
) {

    Log.d(
        "SKEYPRO",
        "Ignored Content: $title | $text"
    )
    return
}

            val payload =
                JSONObject().apply {

                    put(
                        "device_id",
                        deviceId
                    )

                    put(
                        "package",
                        packageName
                    )

                    put(
                        "title",
                        title
                    )

                    put(
                        "text",
                        text
                    )

                    put(
                        "timestamp",
                        System.currentTimeMillis()
                    )

                }.toString()

            Log.d(
                "SKEYPRO",
                payload
            )

            thread {

                val success =
                    WebhookClient.send(
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