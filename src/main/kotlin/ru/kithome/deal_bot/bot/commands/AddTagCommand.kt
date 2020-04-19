package ru.kithome.deal_bot.bot.commands

import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.kithome.deal_bot.service.TagService

class AddTagCommand(commandIdentifier: String?,
                    description: String?,
                    private val tagService: TagService
) : DealBotCommand(commandIdentifier, description) {

    override fun execute(sender: AbsSender?, user: User?, chat: Chat?, args: Array<out String>?) {
        val tag = args?.get(0)
        tag?.let {
            tagService.addTag(tag, args[0].orEmpty())
        }
    }

}