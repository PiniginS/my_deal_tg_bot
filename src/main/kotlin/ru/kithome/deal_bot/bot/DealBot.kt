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
import ru.kithome.deal_bot.service.bot.BotMenuService
import ru.kithome.deal_bot.service.bot.BotTagService
import ru.kithome.deal_bot.service.bot.BotUpdateService
import ru.kithome.deal_bot.type.KeyboardType

@Component
class DealBot(
    private val botTagService: BotTagService,
    private val botDealService: BotDealService,
    private val botMenuService: BotMenuService,
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
            if (callbackResponse.nextKeyboard != null) {
                showKeyboard(callbackResponse.chatId, callbackResponse.nextKeyboard)
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
                showKeyboard(ctx.chatId(), KeyboardType.TAGS)
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
                showKeyboard(ctx.chatId(), KeyboardType.TAGS)
            }
            .build()
    }

    fun showDealsKeyboard(): Ability {
        return Ability
            .builder()
            .name("deals")
            .info("Deals keyboard")
            .locality(Locality.ALL)
            .privacy(Privacy.PUBLIC)
            .action { ctx: MessageContext ->
                showKeyboard(ctx.chatId(), KeyboardType.DEALS)
            }
            .build()
    }

    fun showMenuKeyboard(): Ability {
        return Ability
            .builder()
            .name("menu")
            .info("Menu keyboard")
            .locality(Locality.ALL)
            .privacy(Privacy.PUBLIC)
            .action { ctx: MessageContext ->
                showKeyboard(ctx.chatId(), KeyboardType.MENU)
            }
            .build()
    }

    private fun showKeyboard(chatId: Long, keyboardType: KeyboardType) {
        when (keyboardType) {
            KeyboardType.DEALS -> sendKeyboard(chatId, botDealService.getKeyboardMarkup(), botTagService.getDefaultTagDescription())
            KeyboardType.TAGS -> sendKeyboard(chatId, botTagService.getKeyboardMarkup(), "Tags")
            KeyboardType.MENU -> sendKeyboard(chatId, botMenuService.getKeyboardMarkup(), "Menu")
        }
    }

    private fun sendKeyboard(chatId: Long, markup: InlineKeyboardMarkup, header: String) {
        execute(SendMessage().setChatId(chatId).setText(header).setReplyMarkup(markup))
    }
}
