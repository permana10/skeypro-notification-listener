package id.skeypro.soundbox.utils

import android.content.Context
import android.provider.Settings

object PermissionHelper {

    fun isNotificationAccessEnabled(
        context: Context
    ): Boolean {

        val enabledListeners =
            Settings.Secure.getString(
                context.contentResolver,
                "enabled_notification_listeners"
            )

        return enabledListeners?.contains(
            context.packageName
        ) == true
    }
}