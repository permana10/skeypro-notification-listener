package id.skeypro.soundbox.network

import id.skeypro.soundbox.config.Config
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object RegisterClient {

    private val client =
        OkHttpClient()

    fun uploadQris(
        file: File,
        deviceId: String
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
                        "device_id",
                        deviceId
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

    fun getInformasi(): String? {

        return try {

            val request =
                Request.Builder()
                    .url(
                        "https://skeypro.id/api/informasi"
                    )
                    .get()
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