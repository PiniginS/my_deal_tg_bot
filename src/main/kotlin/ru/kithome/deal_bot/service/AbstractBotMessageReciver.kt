package ru.kithome.deal_bot.service

import org.telegram.telegrambots.meta.api.objects.Update

abstract class AbstractBotMessageReceiver {

    abstract fun onMessageReceived (update : Update)

    abstract fun isAllowed(update : Update) : Boolean
}