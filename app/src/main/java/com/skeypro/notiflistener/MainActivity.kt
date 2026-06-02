package com.skeypro.notiflistener

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.skeypro.notiflistener.utils.PermissionHelper
import com.skeypro.notiflistener.utils.PrefHelper
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
private var selectedUri: Uri? = null

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

        val txtStatus =
            findViewById<TextView>(R.id.txtStatus)

        val txtConnection =
            findViewById<TextView>(R.id.txtConnection)

       val imgQris =
    findViewById<ImageView>(
        R.id.imgQris
    )

imgQris.setOnClickListener {

    pickImage.launch("image/*")

}


        val registered =
            PrefHelper.isRegistered(this)

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
    View.GONE

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