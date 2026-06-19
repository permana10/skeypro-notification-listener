package id.skeypro.soundbox.network

import android.content.Context
import id.skeypro.soundbox.utils.PrefHelper

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

import org.json.JSONObject

object TestNotificationClient {

    private val client =
        OkHttpClient()

    fun send(
        context: Context
    ): Boolean {

        val deviceId =
            PrefHelper.getDeviceId(
                context
            )

        val providerName =
            PrefHelper.getProvider(
                context
            )

        val packageName =
            PrefHelper
                .getAllowedPackages(
                    context
                )
                .firstOrNull() ?: ""

        return try {

            val body =
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
                        "TEST PEMBAYARAN MASUK"
                    )

                    put(
                        "text",
                        "Rp 10.000 dari BRI berhasil diterima $providerName."
                    )

                    put(
                        "timestamp",
                        System.currentTimeMillis() / 1000
                    )
                }

            val request =
                Request.Builder()
                    .url(
                        "https://skeypro.id/api/notif"
                    )
                    .post(
                        body.toString()
                            .toRequestBody(
                                "application/json".toMediaType()
                            )
                    )
                    .build()

            val response =
                client.newCall(request)
                    .execute()

            response.isSuccessful

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }
}