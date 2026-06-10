package id.skeypro.soundbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class InfoActivity : AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        setContentView(
            R.layout.activity_info
        )

        supportActionBar?.title =
            "Informasi"

        supportActionBar?.setDisplayHomeAsUpEnabled(
            true
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}