package ru.kithome.deal_bot.config

import org.telegram.abilitybots.api.bot.AbilityBot
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.objects.Update

class Bot(botToken: String?, botUsername: String?, botOptions: DefaultBotOptions?) :
    AbilityBot(botToken, botUsername, botOptions) {

    override fun creatorId(): Int {
        return 1
    }

    override fun onUpdateReceived(update: Update?) {
        print(update?.message?.text)
    }

}