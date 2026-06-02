package com.skeypro.notiflistener.network

import com.skeypro.notiflistener.config.Config
import okhttp3.*
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
                RequestBody.create(
                    MediaType.parse("application/json"),
                    json.toString()
                )

            val request =
                Request.Builder()
                .url(Config.REGISTER_URL)
                .post(body)
                .build()

            client.newCall(request)
                .execute()
                .body()
                ?.string()

        } catch (e: Exception) {
            null
        }
    }
}