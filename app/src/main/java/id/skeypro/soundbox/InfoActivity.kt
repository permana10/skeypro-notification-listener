package id.skeypro.soundbox

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import id.skeypro.soundbox.network.RegisterClient
import kotlin.concurrent.thread

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_info
        )

        val txtInfo =
            findViewById<TextView>(
                R.id.txtInfo
            )

        txtInfo.text =
            "Memuat informasi..."

        thread {

            val result =
                RegisterClient
                    .getInformasi()

            runOnUiThread {

                txtInfo.text =
                    result
                        ?: "Gagal memuat informasi."
            }
        }

        supportActionBar?.title =
            "Informasi"

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