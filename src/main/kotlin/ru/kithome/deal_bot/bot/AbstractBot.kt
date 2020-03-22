package ru.kithome.deal_bot.bot

import org.telegram.abilitybots.api.bot.AbilityBot
import ru.kithome.deal_bot.config.BotConfiguration

abstract class AbstractBot(private val botConfiguration: BotConfiguration) :
    AbilityBot(botConfiguration.token, botConfiguration.botName, botConfiguration.getBotOptions()) {
}