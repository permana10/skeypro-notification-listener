package id.skeypro.soundbox

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.ProgressBar
import android.widget.LinearLayout
import android.widget.Toast
import android.net.Uri
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

import id.skeypro.soundbox.utils.PermissionHelper
import id.skeypro.soundbox.utils.PrefHelper
import id.skeypro.soundbox.utils.RegisterHelper
import id.skeypro.soundbox.utils.TesNotificationHelper
import id.skeypro.soundbox.websocket.AppWebSocketHelper
import id.skeypro.soundbox.network.TestNotificationClient
import id.skeypro.soundbox.network.RegisterClient

import java.io.File
import org.json.JSONObject
import kotlin.concurrent.thread
import okhttp3.WebSocket


class MainActivity : AppCompatActivity() {
private var appWebSocket: WebSocket? = null
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

private fun registerQris(
    deviceIdInput: String
){
    RegisterHelper.register(
        this,
        selectedUri,
        deviceIdInput
    )
}

fun handleRegisterSuccess(
    response: String?,
    tempFile: File
) {

    findViewById<ProgressBar>(
        R.id.progressRegister
    ).visibility = View.GONE

    if(response == null){
        return
    }

    val json =
        JSONObject(response)

    if(
        !json.optBoolean(
            "success",
            false
        )
    ){
        return
    }

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
        this,
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

    val packages =
        mutableSetOf<String>()

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
        this,
        packages
    )

    val providers =
    mutableMapOf<String, String>()

val jsonProviders =
    json.optJSONObject(
        "providers"
    )

if(jsonProviders != null){

    jsonProviders.keys().forEach { key ->

        providers[key] =
            jsonProviders.getString(
                key
            )
    }
}

PrefHelper.saveProviders(
    this,
    providers
)

    val keywords =
        mutableSetOf<String>()

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

    val blockedWords =
        mutableSetOf<String>()

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
        this,
        keywords
    )

    PrefHelper.saveBlockedWords(
        this,
        blockedWords
    )

    PrefHelper.saveRegistration(
        this,
        deviceId,
        merchant,
        provider,
        status,
        filter
    )

    appWebSocket =
        AppWebSocketHelper.connect(
            this,
            deviceId
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
        "Rp %,d".format(
            totalAmount
        )

    findViewById<TextView>(
        R.id.txtStatus
    ).text =
        "Status : $status"

    findViewById<TextView>(
        R.id.txtStatus
    ).setTextColor(
        getColor(
            R.color.primary_green
        )
    )

    findViewById<TextView>(
        R.id.txtConnection
    ).text =
        "● Connected"

    findViewById<TextView>(
        R.id.txtConnection
    ).setTextColor(
        getColor(
            R.color.primary_green
        )
    )

    findViewById<LinearLayout>(
        R.id.layoutWelcome
    ).visibility =
        View.GONE

    findViewById<LinearLayout>(
        R.id.layoutMerchantInfo
    ).visibility =
        View.VISIBLE

    findViewById<android.widget.EditText>(
        R.id.edtDeviceId
    ).visibility =
        View.GONE

    findViewById<TextView>(
        R.id.txtDeviceHint
    ).visibility =
        View.GONE

    findViewById<android.widget.Button>(
        R.id.btnPilihQris
    ).visibility =
        View.GONE

    findViewById<android.widget.Button>(
        R.id.btnDaftar
    ).visibility =
        View.GONE

    findViewById<ImageView>(
        R.id.imgQris
    ).setImageURI(
        Uri.fromFile(
            qrisFile
        )
    )
}

private fun updateBadge() {

    val badge =
        findViewById<View>(
            R.id.viewBadge
        )

    badge.visibility =
        if(
            unreadNotif ||
            updateAvailable
        )
            View.VISIBLE
        else
            View.GONE
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

fun updateSaldo(
    totalAmount: Long
){

    PrefHelper.saveSaldo(
        this,
        totalAmount
    )

    runOnUiThread {

        findViewById<TextView>(
            R.id.txtSaldo
        ).text =
            "Rp %,d".format(
                totalAmount
            )
    }
}

fun onNotificationReceived(
    title: String,
    message: String
){

    unreadNotif = true

    PrefHelper.saveUnreadNotif(
        this,
        true
    )

    runOnUiThread {

        updateBadge()

        Toast.makeText(
            this,
            "$title\n$message",
            Toast.LENGTH_LONG
        ).show()
    }
}

fun onUpdateApk(
    version: String,
    url: String
){

    updateAvailable = true

    latestVersion = version
    latestApkUrl = url

    runOnUiThread {

        updateBadge()
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

    unreadNotif =
    PrefHelper.getUnreadNotif(
        this
    )

updateBadge()

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

val registered =
    PrefHelper.isRegistered(this)

if (registered) {

    popup.menu.add(
        "Tes Notifikasi"
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

} else {

    popup.menu.add(
        "Informasi"
    )

    popup.menu.add(
        if(updateAvailable)
            "Update Aplikasi ●"
        else
            "Update Aplikasi"
    )
}

    popup.setOnMenuItemClickListener {
        when(it.title.toString()) {


    "Tes Notifikasi" -> {

    TesNotificationHelper.show(
        this
    ){

        thread {

            val success =
                TestNotificationClient.send(
                    this@MainActivity
                )

            runOnUiThread {

                Toast.makeText(
                    this,
                    if(success)
                        "Tes notifikasi berhasil dikirim"
                    else
                        "Gagal mengirim tes notifikasi",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    true
}

            "Notifikasi",
"Notifikasi ●" -> {

    unreadNotif = false

    PrefHelper.saveUnreadNotif(
        this,
        false
    )

    updateBadge()

    startActivity(
        Intent(
            this,
            NotificationActivity::class.java
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
                val deviceId =
                    PrefHelper.getDeviceId(this)
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                    "https://skeypro.id/hapus-akun/$deviceId"
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
        val layoutMerchantInfo =
    findViewById<LinearLayout>(
        R.id.layoutMerchantInfo
    )

        if (registered) {

        layoutMerchantInfo.visibility = View.VISIBLE       

            val deviceId =
                PrefHelper.getDeviceId(this)

            appWebSocket =
    AppWebSocketHelper.connect(
        this,
        deviceId
    )

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
                "Rp %,d".format(totalAmount)
            
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

             findViewById<LinearLayout>(
    R.id.layoutWelcome
             ).visibility = View.GONE
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
            layoutMerchantInfo.visibility = View.GONE

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

            findViewById<LinearLayout>(
    R.id.layoutWelcome
            ).visibility = View.VISIBLE
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

