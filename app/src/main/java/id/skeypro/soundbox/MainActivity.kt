package id.skeypro.soundbox

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import java.io.File
import android.widget.TextView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import id.skeypro.soundbox.utils.PermissionHelper
import id.skeypro.soundbox.utils.PrefHelper
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import kotlin.concurrent.thread
import org.json.JSONObject
import id.skeypro.soundbox.network.RegisterClient
import android.widget.ImageButton
import android.widget.PopupMenu

class MainActivity : AppCompatActivity() {
private var selectedUri: Uri? = null

private var qrisLocked = false
private val pickImage =
    registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->

    if (uri != null) {

    selectedUri = uri

    findViewById<ImageView>(
        R.id.imgQris
    ).setImageURI(uri)
   }
 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!PermissionHelper.isNotificationAccessEnabled(this)) {
            startActivity(
                Intent(
                    Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
                )
            )
        }

        setContentView(R.layout.activity_main)
        
        val txtDeviceId =
            findViewById<TextView>(R.id.txtDeviceId)
        val txtMerchant =
            findViewById<TextView>(R.id.txtMerchant)
        val txtProvider = 
            findViewById<TextView>(R.id.txtProvider)
        val txtStatus =
            findViewById<TextView>(R.id.txtStatus)
        val txtConnection =
            findViewById<TextView>(R.id.txtConnection)

       val imgQris =
    findViewById<ImageView>(
        R.id.imgQris
    )

val btnPilihQris =
    findViewById<android.widget.Button>(
        R.id.btnPilihQris
    )

btnPilihQris.setOnClickListener {

    pickImage.launch(
        "image/*"
    )
}

        val btnDaftar =
    findViewById<android.widget.Button>(
        R.id.btnDaftar
    )

btnDaftar.setOnClickListener {
    val deviceIdInput =
        findViewById<
            android.widget.EditText
        >(
            R.id.edtDeviceId
        )
        .text
        .toString()
        .trim()

    if (deviceIdInput.isEmpty()) {
        return@setOnClickListener
    }

    if (selectedUri == null) {
        return@setOnClickListener
    }

    findViewById<ProgressBar>(
    R.id.progressRegister
).visibility = View.VISIBLE

thread {

    try {

        val input =
            contentResolver
                .openInputStream(
                    selectedUri!!
                )

        val tempFile =
            java.io.File(
                cacheDir,
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

        runOnUiThread {

            findViewById<ProgressBar>(
                R.id.progressRegister
            ).visibility = View.GONE

            if (response != null) {

    val json =
        JSONObject(response)

    if (
        json.optBoolean(
            "success",
            false
        )
    ) {

        val deviceId =
            json.optString(
                "device_id"
            )

        val merchant =
            json.optString(
                "merchant"
            )

        val provider =
            json.optString(
                "provider"
            )

        val status =
            json.optString(
                "status"
            )

        PrefHelper.saveRegistration(
            this@MainActivity,
            deviceId,
            merchant,
            provider,
            status

        )

        val qrisFile =
            java.io.File(
                filesDir,
                "qris.jpg"
            )

        tempFile.copyTo(
            qrisFile,
            overwrite = true
        )

        qrisLocked = true

        txtDeviceId.text =
            "ID : $deviceId"

        txtMerchant.text =
            "Merchant : $merchant"

        txtProvider.text =
            "Provider : $provider"

        txtStatus.text =
            "Status : $status"

        txtConnection.text =
            "● Connected"

        findViewById<android.widget.EditText>(
            R.id.edtDeviceId
        ).visibility = View.GONE

        findViewById<android.widget.Button>(
            R.id.btnPilihQris
        ).visibility = View.GONE

        findViewById<android.widget.Button>(
            R.id.btnDaftar
        ).visibility = View.GONE
    }
}

        }

    } catch (e: Exception) {

        e.printStackTrace()

        runOnUiThread {

            findViewById<ProgressBar>(
                R.id.progressRegister
            ).visibility = View.GONE
        }
    }
  }
}       

     val btnMenu =
    findViewById<ImageButton>(
        R.id.btnMenu
    )      

       btnMenu.setOnClickListener {

    val popup =
        PopupMenu(
            this,
            btnMenu
        )

    popup.menu.add(
        "Notifikasi"
    )

    popup.menu.add(
        "Ganti QRIS"
    )

    popup.menu.add(
        "Informasi"
    )

    popup.menu.add(
        "Update"
    )

    popup.setOnMenuItemClickListener {

        when(it.title.toString()){

            "Ganti QRIS" -> {

    androidx.appcompat.app.AlertDialog
        .Builder(this)

        .setTitle(
            "Ganti QRIS"
        )

        .setMessage(
            "Yakin ingin mengganti QRIS?"
        )

        .setPositiveButton(
            "Ya"
        ) { _, _ ->

            pickImage.launch(
                "image/*"
            )

        }

        .setNegativeButton(
            "Tidak",
            null
        )

        .show()

    true
}

            else -> true
        }
    }

    popup.show()
}

        val registered =
    PrefHelper.isRegistered(this)

    qrisLocked = registered

        if (registered) {

            val deviceId =
                PrefHelper.getDeviceId(this)

            val merchant =
                PrefHelper.getMerchant(this)

            val provider =
                PrefHelper.getProvider(this)

            val status =
                PrefHelper.getStatus(this)

            txtDeviceId.text =
                "ID : $deviceId"         

            txtMerchant.text =
                "Merchant : $merchant"

            txtProvider.text =
                "Provider : $provider"

            txtStatus.text =
                "Status : $status"

            txtConnection.text =
                "● Connected"
    
             findViewById<android.widget.EditText>(
    R.id.edtDeviceId
             ).visibility = View.GONE
             findViewById<android.widget.Button>(
    R.id.btnPilihQris
             ).visibility = View.GONE
             findViewById<android.widget.Button>(
    R.id.btnDaftar
             ).visibility = View.GONE

val qrisFile =
    java.io.File(
        filesDir,
        "qris.jpg"
    )

if (qrisFile.exists()) {

    imgQris.setImageURI(
        Uri.fromFile(
            qrisFile
        )
    )
}

            imgQris.visibility =
    View.VISIBLE

        } else {

            txtDeviceId.text =
                "ID : Belum Terdaftar"

            txtMerchant.text = "Merchant : -"

            txtStatus.text =
                "Status : Belum Aktif"

            txtConnection.text =
                "● Disconnected"

            findViewById<android.widget.EditText>(
    R.id.edtDeviceId
            ).visibility = View.VISIBLE
            findViewById<android.widget.Button>(
    R.id.btnPilihQris
            ).visibility = View.VISIBLE
            findViewById<android.widget.Button>(
    R.id.btnDaftar
            ).visibility = View.VISIBLE
            imgQris.visibility = View.VISIBLE
        }
    }
}

