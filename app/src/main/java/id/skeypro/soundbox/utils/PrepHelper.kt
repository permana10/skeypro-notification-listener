package id.skeypro.soundbox.utils

import android.content.Context

object PrefHelper {

    private const val PREF_NAME = "skeybox"

    fun saveRegistration(
        context: Context,
        deviceUid: String,
        deviceId: String,
        merchant: String,
        provider: String,
        status: String
    ) {

        context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putBoolean("registered", true)
            .putString("device_uid", deviceUid)
            .putString("device_id", deviceId)
            .putString("merchant", merchant)            
            .putString("provider", provider)
            .putString("status", status)
            .apply()
    }

    fun ensureDeviceUid(
        context: Context
    ): String {

    val prefs =
        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

    var uid =
        prefs.getString(
            "device_uid",
            ""
        ) ?: ""

    if(uid.isBlank()){

        uid =
            "UID-" +
            java.util.UUID
                .randomUUID()
                .toString()
                .replace("-", "")
                .take(12)
                .uppercase()

        prefs.edit()
            .putString(
                "device_uid",
                uid
            )
            .apply()
    }

    return uid
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

    fun getDeviceUid(
        context: Context
    ): String {

    return context
        .getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
        .getString(
            "device_uid",
            ""
        ) ?: ""
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
        .remove("allowed_packages")
        .apply()
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

