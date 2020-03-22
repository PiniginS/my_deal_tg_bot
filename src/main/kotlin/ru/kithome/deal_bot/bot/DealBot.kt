package ru.kithome.deal_bot.bot

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Update
import ru.kithome.deal_bot.config.BotConfiguration
import ru.kithome.deal_bot.service.AbstractBotMessageReceiver

@Service
class DealBot(
    private val botConfiguration: BotConfiguration,
    private val receiverList: List<AbstractBotMessageReceiver>
) : AbstractBot(botConfiguration) {

    override fun creatorId(): Int {
        return 42
    }

    override fun onUpdateReceived(update: Update?) {
        super.onUpdateReceived(update)
        update?.let {
            receiverList
                .filter { it.isAllowed(update) }
                .forEach { it.onMessageReceived(update) }
        }
    }
}