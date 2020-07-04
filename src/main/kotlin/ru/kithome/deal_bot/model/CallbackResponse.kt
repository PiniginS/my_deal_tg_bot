package ru.kithome.deal_bot.model

data class CallbackResponse(
    val chatId: Long,
    val message: String?
)