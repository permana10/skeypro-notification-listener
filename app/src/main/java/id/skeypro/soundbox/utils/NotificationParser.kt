package id.skeypro.soundbox.utils

import android.service.notification.StatusBarNotification
import org.json.JSONObject

object NotificationParser {

    fun parse(sbn: StatusBarNotification): String {

        val extras = sbn.notification.extras

        val title = extras
            ?.getCharSequence("android.title")
            ?.toString()
            ?: ""

        val text = extras
            ?.getCharSequence("android.text")
            ?.toString()
            ?: ""

        return JSONObject().apply {

            put("package", sbn.packageName)
            put("title", title)
            put("text", text)
            put("timestamp", System.currentTimeMillis())

        }.toString()
    }
}