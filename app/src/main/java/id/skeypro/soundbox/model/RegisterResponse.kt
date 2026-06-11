package id.skeypro.soundbox.model

data class RegisterResponse(
    val success: Boolean,
    val device_id: String,
    val merchant: String,
    val status: String,
)