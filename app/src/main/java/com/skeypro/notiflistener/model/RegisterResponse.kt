package com.skeypro.notiflistener.model

data class RegisterResponse(
    val success: Boolean,
    val device_id: String,
    val merchant: String,
    val status: String
)