package ru.kithome.deal_bot.service

import org.springframework.stereotype.Service
import org.telegram.abilitybots.api.bot.AbilityBot
import org.telegram.telegrambots.meta.api.objects.Update
import ru.kithome.deal_bot.config.BotConfiguration

@Service
class DealBotService(
    botConfiguration: BotConfiguration
) : AbilityBot(botConfiguration.token, botConfiguration.botName, botConfiguration.getBotOptions()) {

    override fun creatorId(): Int {
        return 42
    }

    override fun onUpdateReceived(update: Update?) {
        super.onUpdateReceived(update)
        update?.let {
            println("Echo ${update.message.text}")
        }
    }
}