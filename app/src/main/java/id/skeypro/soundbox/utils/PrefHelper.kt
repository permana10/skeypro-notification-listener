package id.skeypro.soundbox.utils

import android.content.Context

object PrefHelper {

    private const val PREF_NAME = "skeybox"

    fun saveRegistration(
        context: Context,
        deviceId: String,
        merchant: String,
        provider: String,
        status: String,
        filter: String
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
            .putString("provider", provider)
            .putString("status", status)
            .putString("filter", filter)
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
                ""
            ) ?: ""
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
                ""
            ) ?: ""
    }

    fun getProvider(
        context: Context
    ): String {

        return context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .getString(
                "provider",
                "UNKNOWN"
            ) ?: "UNKNOWN"
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
                ""
            ) ?: ""
    }

    fun getFilter(
        context: Context
    ): String {

        return context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .getString(
                "filter",
                "UNKNOWN"
            ) ?: "UNKNOWN"
    }

    fun saveSaldo(
    context: Context,
    saldo: Long
) {

    context
        .getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
        .edit()
        .putLong(
            "saldo_qris",
            saldo
        )
        .apply()
}

fun getSaldo(
    context: Context
): Long {

    return context
        .getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
        .getLong(
            "saldo_qris",
            0
        )
}

fun saveUnreadNotif(
    context: Context,
    value: Boolean
) {

    context
        .getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
        .edit()
        .putBoolean(
            "unread_notif",
            value
        )
        .apply()
}

fun getUnreadNotif(
    context: Context
): Boolean {

    return context
        .getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
        .getBoolean(
            "unread_notif",
            false
        )
}

    fun clearRegistration(
        context: Context
    ) {

        context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putBoolean(
                "registered",
                false
            )
            .remove("device_id")
            .remove("merchant")
            .remove("provider")
            .remove("status")
            .remove("filter")
            .apply()
    }

    fun saveKeywords(
        context: Context,
        keywords: Set<String>
    ) {

        context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putStringSet(
                "keywords",
                keywords
            )
            .apply()
    }

    fun getKeywords(
        context: Context
    ): Set<String> {

        return context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .getStringSet(
                "keywords",
                emptySet()
            ) ?: emptySet()
    }

    fun saveBlockedWords(
        context: Context,
        blockedWords: Set<String>
    ) {

        context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putStringSet(
                "blocked_words",
                blockedWords
            )
            .apply()
    }

    fun getBlockedWords(
        context: Context
    ): Set<String> {

        return context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .getStringSet(
                "blocked_words",
                emptySet()
            ) ?: emptySet()
    }

    fun saveAllowedPackages(
        context: Context,
        packages: Set<String>
    ) {

        context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putStringSet(
                "allowed_packages",
                packages
            )
            .apply()
    }

    fun getAllowedPackages(
        context: Context
    ): Set<String> {

        return context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .getStringSet(
                "allowed_packages",
                emptySet()
            ) ?: emptySet()
    }
}
