package com.skeypro.notiflistener.network

import com.skeypro.notiflistener.config.Config
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object RegisterClient {

    private val client =
        OkHttpClient()

    fun uploadQris(
        file: File
    ): String? {

        return try {

            val requestFile =
                file.asRequestBody(
                    "image/*".toMediaTypeOrNull()
                )

            val body =
                MultipartBody.Builder()
                    .setType(
                        MultipartBody.FORM
                    )
                    .addFormDataPart(
                        "file",
                        file.name,
                        requestFile
                    )
                    .build()

            val request =
                Request.Builder()
                    .url(
                        Config.REGISTER_URL
                    )
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