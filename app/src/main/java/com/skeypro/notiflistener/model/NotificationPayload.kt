package com.skeypro.notiflistener.model

data class NotificationPayload(
    val packageName: String,
    val appName: String,
    val title: String,
    val text: String,
    val timestamp: Long
)