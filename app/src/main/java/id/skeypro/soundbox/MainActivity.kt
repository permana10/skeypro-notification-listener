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
private var replaceQrisMode = false
private var qrisLocked = false
private var unreadNotif = false
private var updateAvailable = false
private var latestVersion = ""
private var latestApkUrl = ""

private val pickImage =
    registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->

    if (uri != null) {
    selectedUri = uri
    findViewById<ImageView>(
        R.id.imgQris
    ).setImageURI(uri)

    if(replaceQrisMode){
        replaceQrisMode = false
        val deviceId =
            PrefHelper.getDeviceId(this)
        registerQris(deviceId)
    }
   }
 }

private fun registerQris(deviceIdInput: String){
    if(selectedUri == null){
        return
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
                File(
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

                if(response != null){

                    val json =
                        JSONObject(response)

                    if(
                        json.optBoolean(
                            "success",
                            false
                        )
                    ){

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

                        val totalAmount =
                            json.optLong(
                            "total_amount",
                             0
                        )

                        PrefHelper.saveSaldo(
                            this@MainActivity,
                            totalAmount
                        )

                        val status =
                            json.optString(
                                "status"
                            )

                        val filter =
                            json.optString(
                                "filter"
                            )

                        val packages = mutableSetOf<String>()
                        val jsonPackages =
                            json.optJSONArray(
                                "allowed_packages"
                            )

                        if(jsonPackages != null){
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
                            this@MainActivity,
                            packages
                        )
val keywords = mutableSetOf<String>()
val jsonKeywords =
    json.optJSONArray(
        "keywords"
    )

if(jsonKeywords != null){
    for(
        i in 0 until
        jsonKeywords.length()
    ){
        keywords.add(
            jsonKeywords.getString(i)
        )
    }
}

val blockedWords = mutableSetOf<String>()
val jsonBlockedWords =
    json.optJSONArray(
        "blocked_words"
    )

if(jsonBlockedWords != null){
    for(
        i in 0 until
        jsonBlockedWords.length()
    ){
        blockedWords.add(
            jsonBlockedWords.getString(i)
        )
    }
}

   PrefHelper.saveKeywords(
    this@MainActivity,
    keywords
)

PrefHelper.saveBlockedWords(
    this@MainActivity,
    blockedWords
)

                        PrefHelper.saveRegistration(
                            this@MainActivity,
                            deviceId,
                            merchant,
                            provider,
                            status, 
                            filter                          
                        )

                        val qrisFile =
                            File(
                                filesDir,
                                "qris.jpg"
                            )

                        tempFile.copyTo(
                            qrisFile,
                            overwrite = true
                        )

                        findViewById<TextView>(
                            R.id.txtDeviceId
                        ).text =
                            "ID : $deviceId"

                        findViewById<TextView>(
                            R.id.txtMerchant
                        ).text =
                            "Merchant : $merchant"

                        findViewById<TextView>(
                            R.id.txtProvider
                        ).text =
                            "Provider : $provider"

                        findViewById<TextView>(
                            R.id.txtSaldo
                        ).text =
                            "Saldo QRIS : Rp $totalAmount"

                        findViewById<TextView>(
                            R.id.txtStatus
                        ).text =
                            "Status : $status"

                        findViewById<TextView>(
                            R.id.txtConnection
                        ).text =
                            "● Connected"

                        findViewById<android.widget.EditText>(
                            R.id.edtDeviceId
                        ).visibility = View.GONE

                        findViewById<TextView>(
                            R.id.txtDeviceHint
                        ).visibility = View.GONE

                        findViewById<android.widget.Button>(
                            R.id.btnPilihQris
                        ).visibility = View.GONE

                        findViewById<android.widget.Button>(
                            R.id.btnDaftar
                        ).visibility = View.GONE

                        findViewById<ImageView>(
                            R.id.imgQris
                        ).setImageURI(Uri.fromFile(qrisFile))
                    }
                }
            }

        } catch(e: Exception){

            e.printStackTrace()

            runOnUiThread {

                findViewById<ProgressBar>(
                    R.id.progressRegister
                ).visibility = View.GONE
            }
        }
    }
}

private fun checkServerInfo(){
    thread {
        try{

            // nanti request ke server
            // /api/app_info?device_id=xxxx

            // contoh hasil:
            // unreadNotif = true
            // updateAvailable = true
            // latestVersion = "1.0.3"
            // latestApkUrl = "https://....apk"

        }catch(e: Exception){
            e.printStackTrace()
        }
    }
}


    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main)

    if (!PermissionHelper.isNotificationAccessEnabled(this)) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Izin Notifikasi")
            .setMessage(
                "SKEYBOX memerlukan akses notifikasi untuk mendeteksi pembayaran QRIS dan memutar suara secara otomatis.\n\nTekan OK untuk membuka pengaturan."
            )
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
                startActivity(
                    Intent(
                        Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
                    )
                )
            }
            .setNegativeButton("Nanti", null)
            .show()
    }

    checkServerInfo()
    android.util.Log.d("SKEYBOX", "CHECK SERVER INFO")

        val txtDeviceId =
            findViewById<TextView>(R.id.txtDeviceId)
        val txtMerchant =
            findViewById<TextView>(R.id.txtMerchant)
        val txtProvider = 
            findViewById<TextView>(R.id.txtProvider)
        val txtSaldo =
            findViewById<TextView>(R.id.txtSaldo)
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
        findViewById<android.widget.EditText>(
            R.id.edtDeviceId
        )
        .text
        .toString()
        .trim()

    if(deviceIdInput.isEmpty()){
        return@setOnClickListener
    }

    registerQris(
        deviceIdInput
    )
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
        if(unreadNotif)
            "Notifikasi ●"
        else
            "Notifikasi"
    )

    popup.menu.add(
        "Ganti QRIS"
    )

    popup.menu.add(
    "Informasi"
)

    popup.menu.add(
        if(updateAvailable)
            "Update Aplikasi ●"
        else
            "Update Aplikasi"
    )

    popup.menu.add(
        "Hapus Akun"
    )

    popup.setOnMenuItemClickListener {

        when(it.title.toString()) {

            "Notifikasi",
"Notifikasi ●" -> {

    startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(
                "https://skeypro.id/notifikasi"
            )
        )
    )

    true
}

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

                        replaceQrisMode = true

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

            "Informasi" -> {

    startActivity(
        Intent(
            this,
            InfoActivity::class.java
        )
    )

    true
}

            "Update Aplikasi",
"Update Aplikasi ●" -> {

    startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(
                "https://github.com/permana10/skeypro-notification-listener/releases/latest"
            )
        )
    )

    true
}

            "Hapus Akun" -> {

                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            "https://skeypro.id/hapus-akun"
                        )
                    )
                )

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

            val totalAmount =
                PrefHelper.getSaldo(this)

            val status =
                PrefHelper.getStatus(this)

            txtDeviceId.text =
                "ID : $deviceId"         

            txtMerchant.text =
                "Merchant : $merchant"

            txtProvider.text =
                "Provider : $provider"

            txtSaldo.text =
                "Saldo QRIS : Rp $totalAmount"
            
            val txtStatusView =
                findViewById<TextView>(
                R.id.txtStatus
            )

            txtStatusView.text =
                "Status : $status"

            txtStatusView.setTextColor(
                getColor(R.color.primary_green)
            )

            val txtConnectionView =
                findViewById<TextView>(
                R.id.txtConnection
            )

            txtConnectionView.text =
                "● Connected"

             
             txtConnectionView.setTextColor(
                 getColor(R.color.primary_green)
             )           

             findViewById<android.widget.EditText>(
    R.id.edtDeviceId
             ).visibility = View.GONE
             findViewById<TextView>(
    R.id.txtDeviceHint
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

            txtStatus.setTextColor(
                getColor(R.color.text_secondary)
            )

            txtConnection.text = "● Disconnected"
            txtConnection.setTextColor(
                getColor(R.color.text_secondary)
            )

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

