package ru.kithome.deal_bot.bot.commands

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand

abstract class DealBotCommand(commandIdentifier: String?, description: String?) : BotCommand(commandIdentifier, description) {
}