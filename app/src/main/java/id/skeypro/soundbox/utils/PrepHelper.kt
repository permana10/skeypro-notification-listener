package id.skeypro.soundbox.utils

import android.content.Context

object PrefHelper {

    private const val PREF_NAME = "skeybox"

    fun saveRegistration(
        context: Context,
        deviceId: String,
        merchant: String,
        status: String
    ) {

        context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putBoolean("registered", true)
            .putString("device_id", deviceId)
            .putString("merchant", merchant)
            .putString("status", status)
            .apply()
    }

    fun isRegistered(
        context: Context
    ): Boolean {

        return context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .getBoolean(
                "registered",
                false
            )
    }

    fun getDeviceId(
        context: Context
    ): String {

        return context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .getString(
                "device_id",
                "-"
            ) ?: "-"
    }

    fun getMerchant(
        context: Context
    ): String {

        return context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .getString(
                "merchant",
                "-"
            ) ?: "-"
    }

    fun getStatus(
        context: Context
    ): String {

        return context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .getString(
                "status",
                "-"
            ) ?: "-"
    }
}