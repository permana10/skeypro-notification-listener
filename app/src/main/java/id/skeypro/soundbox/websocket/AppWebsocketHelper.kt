package id.skeypro.soundbox.websocket

import android.widget.TextView
import android.widget.Toast
import id.skeypro.soundbox.MainActivity
import id.skeypro.soundbox.R
import id.skeypro.soundbox.config.Config
import okhttp3.*
import org.json.JSONObject

object AppWebSocketHelper {

    fun connect(
        activity: MainActivity,
        deviceId: String
    ): WebSocket {

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(
                "${Config.WS_APP_URL}/$deviceId"
            )
            .build()

        return client.newWebSocket(
            request,
            object : WebSocketListener() {

                override fun onOpen(
                    webSocket: WebSocket,
                    response: Response
                ) {

                    activity.runOnUiThread {

                        activity.findViewById<TextView>(
                            R.id.txtConnection
                        ).text =
                            "● Connected"
                    }
                }

                override fun onMessage(
                    webSocket: WebSocket,
                    text: String
                ) {

                    try {

                        val json =
                            JSONObject(text)

                        when(
                            json.optString("type")
                        ) {

                            "saldo_update" -> {

                                val totalAmount =
                                    json.optLong(
                                        "total_amount",
                                        0
                                    )

                                activity.updateSaldo(
                                    totalAmount
                                )
                            }

                            "notification" -> {

                                val title =
                                    json.optString(
                                        "title"
                                    )

                                val message =
                                    json.optString(
                                        "message"
                                    )

                                activity.onNotificationReceived(
                                    title,
                                    message
                                )
                            }

                            "update_apk" -> {

                                activity.onUpdateApk(
                                    json.optString(
                                        "version"
                                    ),
                                    json.optString(
                                        "url"
                                    )
                                )
                            }
                        }

                    } catch(e: Exception) {

                        e.printStackTrace()
                    }
                }

                override fun onClosed(
                    webSocket: WebSocket,
                    code: Int,
                    reason: String
                ) {

                    activity.runOnUiThread {

                        activity.findViewById<TextView>(
                            R.id.txtConnection
                        ).text =
                            "● Disconnected"
                    }
                }
            }
        )
    }
}