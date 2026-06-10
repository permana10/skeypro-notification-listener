package id.skeypro.soundbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_info)
findViewById<TextView>(R.id.txtInfo).text = """
SKEYPRO SOUNDBOX adalah aplikasi penerima notifikasi pembayaran QRIS yang terhubung dengan perangkat Soundbox berbasis ESP32.

Fitur:
• Registrasi QRIS secara otomatis.
• Sinkronisasi dengan perangkat Soundbox.
• Pemutaran suara notifikasi pembayaran.
• Update firmware perangkat melalui server.
• Dukungan berbagai provider QRIS.

Pengaturan yang harus diaktifkan:
1. Izin Akses Notifikasi.
2. Koneksi Internet.
3. QRIS harus sudah terdaftar.

Cara penggunaan:
1. Jalankan perangkat Soundbox.
2. Masukkan Device ID yang tampil di LCD.
3. Pilih dan upload gambar QRIS.
4. Tunggu proses registrasi selesai.
5. Setelah status aktif, aplikasi siap digunakan.

Jika terjadi kendala, silakan hubungi layanan bantuan SKEYPRO.
""".trimIndent()

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