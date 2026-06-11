package id.skeypro.soundbox.model

data class RegisterResponse(
    val success: Boolean,
    val device_id: String,
    val merchant: String,
    val status: String,
    val keywords: Set<String>,
    val blockedWords: Set<String>
)