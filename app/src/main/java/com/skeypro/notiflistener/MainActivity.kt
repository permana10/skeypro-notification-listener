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

class MainActivity : AppCompatActivity() {

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

        val btnUploadQris =
            findViewById<Button>(R.id.btnUploadQris)

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

            btnUploadQris.visibility =
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

            btnUploadQris.visibility =
                View.VISIBLE
        }
    }
}