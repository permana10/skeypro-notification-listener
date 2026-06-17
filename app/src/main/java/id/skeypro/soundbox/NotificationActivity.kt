package id.skeypro.soundbox

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import id.skeypro.soundbox.network.RegisterClient
import kotlin.concurrent.thread

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        setContentView(
            R.layout.activity_notification
        )

        val txtNotif =
            findViewById<TextView>(
                R.id.txtNotif
            )

        txtNotif.text =
            "Memuat notifikasi..."

        thread {

            val deviceId =
                PrefHelper.getDeviceId(this)

            val result =
                RegisterClient
                    .getNotifikasi(
                        deviceId
                    )

            runOnUiThread {

                txtNotif.text =
                    result
                        ?: "Belum ada notifikasi."
            }
        }

        supportActionBar?.title =
            "Notifikasi"

        supportActionBar
            ?.setDisplayHomeAsUpEnabled(
                true
            )
    }

    override fun onSupportNavigateUp(): Boolean {

        finish()

        return true
    }
}