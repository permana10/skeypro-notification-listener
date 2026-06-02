package com.skeypro.notiflistener.network

import com.skeypro.notiflistener.config.Config
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object RegisterClient {

    private val client = OkHttpClient()

    fun register(
        androidId: String
    ): String? {

        return try {

            val json = JSONObject()
            json.put(
                "device_uid",
                androidId
            )

            val body =
                json.toString()
                    .toRequestBody(
                        "application/json"
                            .toMediaType()
                    )

            val request =
                Request.Builder()
                    .url(Config.REGISTER_URL)
                    .post(body)
                    .build()

            client.newCall(request)
                .execute()
                .body
                ?.string()

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}