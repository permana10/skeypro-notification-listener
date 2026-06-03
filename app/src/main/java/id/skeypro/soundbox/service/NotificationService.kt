package id.skeypro.soundbox.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import id.skeypro.soundbox.config.Config
import id.skeypro.soundbox.network.WebhookClient
import org.json.JSONObject
import kotlin.concurrent.thread
import id.skeypro.soundbox.utils.PrefHelper

class NotificationService : NotificationListenerService() {

    private val allowedPackages = setOf(

        "id.dana",
        "ovo.id",
        "com.gojek.gopaymerchant",
        "com.shopeepay.id",
        "id.co.bni.merchant",
        "id.ocbc.merchant",
        "com.gojek.resto",
        "com.bca.msb"

    )

    private val keywords = listOf(
    "diterima",
    "masuk",
    "berhasil",
    "pembayaran",
    "qris"
)

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

            if (!allowedPackages.contains(packageName)) {

    Log.d(
        "SKEYPRO",
        "Ignored Package: $packageName"
    )

    return
}
       
            val content =
    (title + " " + text)
        .lowercase()

val blockedWords = listOf(
    "promo",
    "voucher",
    "cashback",
    "iklan",
    "tagihan"
)

if (blockedWords.any {
        content.contains(it)
    }) {

    Log.d(
        "SKEYPRO",
        "Blocked Content: $title | $text"
    )

    return
}

if (!keywords.any {
        content.contains(it)
    }) {

    Log.d(
        "SKEYPRO",
        "Ignored Content: $title | $text"
    )

    return
}

            val payload = JSONObject().apply {

    put(
        "device_id",
        PrefHelper.getDeviceId(
            this@NotificationService
        )
    )

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