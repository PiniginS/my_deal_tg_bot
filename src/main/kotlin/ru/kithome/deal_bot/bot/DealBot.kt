package ru.kithome.deal_bot.bot

import org.springframework.stereotype.Component
import org.telegram.abilitybots.api.bot.AbilityBot
import org.telegram.abilitybots.api.objects.Ability
import org.telegram.abilitybots.api.objects.Locality
import org.telegram.abilitybots.api.objects.MessageContext
import org.telegram.abilitybots.api.objects.Privacy
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import ru.kithome.deal_bot.config.BotConfiguration
import ru.kithome.deal_bot.config.properties.BotProperties
import ru.kithome.deal_bot.model.CallbackResponse
import ru.kithome.deal_bot.service.bot.BotDealService
import ru.kithome.deal_bot.service.bot.BotTagService
import ru.kithome.deal_bot.service.bot.BotUpdateService

@Component
class DealBot(
    private val botTagService: BotTagService,
    private val botDealService: BotDealService,
    private val botUpdateService: BotUpdateService,
    botProperties: BotProperties,
    botConfiguration: BotConfiguration
) : AbilityBot(botProperties.token, botProperties.botName, botConfiguration.getBotOptions()) {

    override fun creatorId(): Int {
        return 261560926
    }

    override fun onUpdateReceived(update: Update?) {
        super.onUpdateReceived(update)
        processUpdateResponse(botUpdateService.processUpdate(update))
    }

    private fun processUpdateResponse(callbackResponse: CallbackResponse?) {
        callbackResponse?.let {
            if (!callbackResponse.message.isNullOrEmpty()) {
                silent.send(
                    callbackResponse.message,
                    callbackResponse.chatId
                )
            }
        }
    }

    fun addTag(): Ability {
        return Ability
            .builder()
            .name("tag")
            .info("addTag [TAG_NAME] [DESCRIPTION] - Add new tag")
            .locality(Locality.ALL)
            .privacy(Privacy.PUBLIC)
            .action { ctx: MessageContext ->
                val response = botTagService.addNewTag(ctx)
                silent.send(
                    response,
                    ctx.chatId()
                )
            }
            .build()
    }

    fun showTagsKeyboard(): Ability {
        return Ability
            .builder()
            .name("tags")
            .info("Tags keyboard")
            .locality(Locality.ALL)
            .privacy(Privacy.PUBLIC)
            .action { ctx: MessageContext ->
                execute(
                    sendKeyboard(ctx.chatId(), botTagService.getKeyboardMarkup(), "Tags")
                )
            }
            .build()
    }

    fun showDealsKeyboard(): Ability {
        return Ability
            .builder()
            .name("deals")
            .info("Deals keyboard keyboard")
            .locality(Locality.ALL)
            .privacy(Privacy.PUBLIC)
            .action { ctx: MessageContext ->
                execute(
                    sendKeyboard(ctx.chatId(), botDealService.getKeyboardMarkup(), "Deals")
                )
            }
            .build()
    }

    private fun sendKeyboard(chatId: Long, markup: InlineKeyboardMarkup, header: String): SendMessage {
        return SendMessage().setChatId(chatId).setText(header).setReplyMarkup(markup)
    }
}
