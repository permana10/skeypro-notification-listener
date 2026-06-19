package id.skeypro.soundbox.utils

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

object TesNotificationHelper {

    fun show(
        activity: AppCompatActivity,
        callback: () -> Unit
    ) {

        val deviceId =
            PrefHelper.getDeviceId(
                activity
            )

        val provider =
            PrefHelper.getProvider(
                activity
            )

        AlertDialog.Builder(activity)
            .setTitle(
                "Tes Notifikasi"
            )
            .setMessage(
    "\nDevice ID : $deviceId\nProvider : $provider\n\nKirim simulasi pembayaran?")
            .setPositiveButton(
                "Kirim"
            ) { _, _ ->

                callback()
            }
            .setNegativeButton(
                "Batal",
                null
            )
            .show()
    }
}
