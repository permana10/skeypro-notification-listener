package id.skeypro.soundbox.network

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object TestNotificationClient {

    private val client =
        OkHttpClient()

    fun send(
        deviceId: String,
        packageName: String,
        providerName: String
    ): Boolean {

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
                        "Pembayaran Masuk"
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