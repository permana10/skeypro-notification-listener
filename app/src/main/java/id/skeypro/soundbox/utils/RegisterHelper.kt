package id.skeypro.soundbox.utils

import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import id.skeypro.soundbox.MainActivity
import id.skeypro.soundbox.R
import id.skeypro.soundbox.network.RegisterClient
import java.io.File
import kotlin.concurrent.thread

object RegisterHelper {

fun register(
    activity: MainActivity,
    selectedUri: Uri?,
    deviceIdInput: String
) {

    if(selectedUri == null){
        return
    }

    activity.findViewById<ProgressBar>(
        R.id.progressRegister
    ).visibility = View.VISIBLE

    thread {

        try {

            val input =
                activity.contentResolver
                    .openInputStream(
                        selectedUri
                    )

            val tempFile =
                File(
                    activity.cacheDir,
                    "qris.jpg"
                )

            input?.use { inp ->

                tempFile.outputStream()
                    .use { out ->

                        inp.copyTo(out)

                    }
            }

            val response =
                RegisterClient
                    .uploadQris(
                        tempFile,
                        deviceIdInput
                    )

            activity.runOnUiThread {

                activity.handleRegisterSuccess(
                    response,
                    tempFile
                )
            }

        } catch(e: Exception){

            e.printStackTrace()

            activity.runOnUiThread {

                activity.findViewById<ProgressBar>(
                    R.id.progressRegister
                ).visibility = View.GONE
            }
        }
    }
}

}