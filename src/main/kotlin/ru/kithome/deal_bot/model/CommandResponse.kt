package ru.kithome.deal_bot.model

import ru.kithome.deal_bot.type.KeyboardType

data class CommandResponse(
    var message: String? = null,
    val nextKeyboard: KeyboardType? = null
)
