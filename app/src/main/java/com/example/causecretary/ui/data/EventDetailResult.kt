package com.example.causecretary.ui.data

data class EventDetailResult(
    val eventName: String,
    val belong: String,
    val kakaoChatUrl: String,
    val phone: String,
    val period: String,
    val contents: String,
    val imgs: List<String>,
    val location: String,
    val pointIdx: Int

)