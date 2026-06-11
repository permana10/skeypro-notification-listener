package id.skeypro.soundbox.model

data class RegisterResponse(
    val success: Boolean,
    val device_id: String,
    val merchant: String,
    val status: String,
    val keywords: List<String>,
    val blocked_words: List<String>
)