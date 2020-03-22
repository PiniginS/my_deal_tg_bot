package ru.kithome.deal_bot.service


import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class BotEchoMessageReceiverService : AbstractBotMessageReceiver() {
    override fun onMessageReceived(update: Update) {
        println("Echo ${update.message.text}")
    }

    override fun isAllowed(update: Update): Boolean {
        return true
    }
}