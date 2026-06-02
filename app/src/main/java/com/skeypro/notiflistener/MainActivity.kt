package com.skeypro.notiflistener

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.skeypro.notiflistener.utils.PermissionHelper
import com.skeypro.notiflistener.utils.PrefHelper
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts

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

    // TODO:
    // Upload ke /api/register

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

        val txtStatus =
            findViewById<TextView>(R.id.txtStatus)

        val txtConnection =
            findViewById<TextView>(R.id.txtConnection)

       val imgQris =
    findViewById<ImageView>(
        R.id.imgQris
    )
   
    val progressRegister =
    findViewById<ProgressBar>(
        R.id.progressRegister
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

        txtStatus.text =
            "Mode Update QRIS Aktif"

        pickImage.launch("image/*")
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

            val status =
                PrefHelper.getStatus(this)

            txtDeviceId.text =
                "ID : $deviceId"

            txtMerchant.text =
                "Merchant : $merchant"

            txtStatus.text =
                "Status : $status"

            txtConnection.text =
                "● Connected"

            imgQris.visibility =
    View.VISIBLE

        } else {

            txtDeviceId.text =
                "ID : Belum Terdaftar"

            txtMerchant.text =
                "Merchant : -"

            txtStatus.text =
                "Status : Belum Aktif"

            txtConnection.text =
                "● Disconnected"

            imgQris.visibility = View.VISIBLE
        }
    }
}