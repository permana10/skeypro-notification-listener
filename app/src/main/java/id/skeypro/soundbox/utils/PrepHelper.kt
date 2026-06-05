package id.skeypro.soundbox.utils

import android.content.Context

object PrefHelper {

    private const val PREF_NAME = "skeybox"

    fun saveRegistration(
        context: Context,
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
            .putString("device_id", deviceId)
            .putString("merchant", merchant)            
            .putString("provider", provider)
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

