package id.skeypro.soundbox.network

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

object WebhookClient {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    fun send(webhookUrl: String, jsonPayload: String): Boolean {
        return try {

            val body = jsonPayload.toRequestBody(
                "application/json".toMediaType()
            )

            val request = Request.Builder()
                .url(webhookUrl)
                .post(body)
                .build()

            client.newCall(request)
                .execute()
                .use { response ->
                    response.isSuccessful
                }

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}