package id.skeypro.soundbox.utils

import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

object TesNotificationHelper {

fun show(
activity: AppCompatActivity,
providers: Map<String, String>,
callback: (
deviceId: String,
packageName: String,
providerName: String
) -> Unit
) {

val layout =  
    LinearLayout(activity).apply {  

        orientation =  
            LinearLayout.VERTICAL  

        setPadding(  
            40,  
            20,  
            40,  
            20  
        )  
    }  

val edtDeviceId =  
    EditText(activity).apply {  

        hint =  
            "SBX-FE8CE0"  
    }  

val spinner =  
    Spinner(activity)  

val providerNames =  
    providers.keys.toList()  

spinner.adapter =  
    ArrayAdapter(  
        activity,  
        android.R.layout.simple_spinner_dropdown_item,  
        providerNames  
    )  

layout.addView(  
    edtDeviceId  
)  

layout.addView(  
    spinner  
)  

AlertDialog.Builder(activity)  
    .setTitle(  
        "Tes Notifikasi"  
    )  
    .setView(  
        layout  
    )  
    .setPositiveButton(  
        "Kirim"  
    ) { _, _ ->  

        val deviceId =  
            edtDeviceId.text  
                .toString()  
                .trim()  

        if(deviceId.isEmpty()){  
            return@setPositiveButton  
        }  

        val providerName =  
            spinner.selectedItem  
                .toString()  

        val packageName =  
            providers[  
                providerName  
            ] ?: ""  

        callback(  
            deviceId,  
            packageName,  
            providerName  
        )  
    }  
    .setNegativeButton(  
        "Batal",  
        null  
    )  
    .show()

}

}