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

class MainActivity : AppCompatActivity() {
private var selectedUri: Uri? = null

private var qrisLocked = false
private var tapCount = 0
private val pickImage =
    registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->

    if (uri != null) {

    selectedUri = uri

    findViewById<ImageView>(
        R.id.imgQris
    ).setImageURI(uri)

    findViewById<ProgressBar>(
        R.id.progressRegister
    ).visibility = View.VISIBLE

    thread {

    try {

        val input =
            contentResolver.openInputStream(uri)

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

        val deviceUid =
    PrefHelper.ensureDeviceUid(
        this@MainActivity
        )

        val response =
    RegisterClient
        .uploadQris(
            tempFile,
            deviceUid
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

                    val deviceUid =
    json.optString(
        "device_uid",
        PrefHelper.getDeviceUid(
            this
        )
    )

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

val packages =
    mutableSetOf<String>()

val jsonPackages =
    json.optJSONArray(
        "allowed_packages"
    )

if(
    jsonPackages != null
){

    for(
        i in 0 until
        jsonPackages.length()
    ){

        packages.add(
            jsonPackages.getString(i)
        )
    }
}

PrefHelper.saveAllowedPackages(
    this,
    packages
)

val status =
    json.optString(
        "status"
    )

                    PrefHelper.saveRegistration(

                        this,
                        deviceUid,
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

                    findViewById<TextView>(
                        R.id.txtDeviceId
                    ).text =
                        "ID : $deviceId"

                    findViewById<TextView>(
                        R.id.txtMerchant
                    ).text =
                       "Merchant : $merchant ($provider)"

                    findViewById<TextView>(
                        R.id.txtStatus
                    ).text =
                        "Status : $status"

                    findViewById<TextView>(
                        R.id.txtConnection
                    ).text =
                        "● Connected"
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
        PrefHelper.ensureDeviceUid(this)

        val txtDeviceId =
            findViewById<TextView>(R.id.txtDeviceId)

        val txtMerchant =
            findViewById<TextView>(R.id.txtMerchant)

        val txtStatus =
            findViewById<TextView>(R.id.txtStatus)

        val txtConnection =
            findViewById<TextView>(R.id.txtConnection)

       val imgQris =
    findViewById<ImageView>(
        R.id.imgQris
    )

imgQris.setOnClickListener {

    if (!qrisLocked) {

    pickImage.launch("image/*")
    return@setOnClickListener

}

    tapCount++

    txtStatus.text =
        "Ketuk QRIS : $tapCount/10"

    if (tapCount >= 10) {

    tapCount = 0

    androidx.appcompat.app.AlertDialog
        .Builder(this)

        .setTitle(
            "Update QRIS"
        )

        .setMessage(
            "Yakin ingin mengganti foto QRIS?"
        )

        .setNegativeButton(
            "Tidak",
            null
        )

        .setPositiveButton(
            "Ya"
        ) { _, _ ->

            txtStatus.text =
                "Mode Update QRIS Aktif"

            pickImage.launch(
                "image/*"
            )

        }

        .show()
 }
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
                "Merchant : $merchant ($provider)"

            txtStatus.text =
                "Status : $status"

            txtConnection.text =
                "● Connected"
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

            imgQris.visibility = View.VISIBLE
        }
    }
}