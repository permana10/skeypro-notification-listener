package id.skeypro.soundbox.utils

import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.util.TypedValue

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

        setTextSize(
            TypedValue.COMPLEX_UNIT_SP,
            18f
        )
    }

val spinner =  
    Spinner(activity)  
    spinner.minimumHeight = 120

val providerNames =  
    providers.keys.toList()  

val adapter =
    ArrayAdapter(
        activity,
        android.R.layout.simple_spinner_item,
        providerNames
    )

adapter.setDropDownViewResource(
    android.R.layout.simple_spinner_dropdown_item
)

spinner.adapter =
    adapter

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