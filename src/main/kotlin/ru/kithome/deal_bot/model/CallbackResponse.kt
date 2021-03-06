package ru.kithome.deal_bot.model

import ru.kithome.deal_bot.type.KeyboardType

data class CallbackResponse(
    val chatId: Long,
    val message: String? = null,
    val nextKeyboard: KeyboardType? = null
)
