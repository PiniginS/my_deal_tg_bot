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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.kithome.deal_bot.config.BotConfiguration
import ru.kithome.deal_bot.config.properties.BotProperties
import ru.kithome.deal_bot.service.ability.AbilityDealService
import ru.kithome.deal_bot.service.ability.AbilityTagService
import ru.kithome.deal_bot.service.ability.KeyboardCallbackService


@Component
class DealBot(
    private val abilityTagService: AbilityTagService,
    private val abilityDealService: AbilityDealService,
    private val keyboardCallbackService: KeyboardCallbackService,
    botProperties: BotProperties,
    botConfiguration: BotConfiguration
) : AbilityBot(botProperties.token, botProperties.botName, botConfiguration.getBotOptions()) {

    override fun creatorId(): Int {
        return 261560926
    }

    override fun onUpdateReceived(update: Update?) {
        super.onUpdateReceived(update)
        update?.let {
            if (update.hasMessage()) {
                val updateText = update.message.text
                if (updateText.startsWith("/")) return

                if (updateText.startsWith("+") || updateText.startsWith("-")) {
                    sendMessage(update, abilityDealService.processDealWithDefaultTag(update.message.text))
                }
            } else if (update.hasCallbackQuery()) {
                try {
                    if (update.callbackQuery.data.startsWith("@")) {
                        execute(
                            SendMessage().setText(
                                keyboardCallbackService.processCommand(update.callbackQuery.data)
                            )
                                .setChatId(update.callbackQuery.message.chatId)
                        )
                    } else {
                        silent.send(
                            update.callbackQuery.data,
                            update.callbackQuery.message.chatId
                        )
                    }
                } catch (e: TelegramApiException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun sendMessage(update: Update, message: String) {
        sender.execute(SendMessage(update.message.chatId, message))
    }

    fun addTag(): Ability {
        return Ability
            .builder()
            .name("tag")
            .info("addTag [TAG_NAME] [DESCRIPTION] - Add new tag")
            .locality(Locality.ALL)
            .privacy(Privacy.PUBLIC)
            .action { ctx: MessageContext ->
                val response = abilityTagService.addNewTag(ctx)
                silent.send(
                    response,
                    ctx.chatId()
                )
            }
            .build()
    }

    fun allTags(): Ability {
        return Ability
            .builder()
            .name("tags")
            .info("List of all tags")
            .locality(Locality.ALL)
            .privacy(Privacy.PUBLIC)
            .action { ctx: MessageContext ->
                val response = abilityTagService.getAllTags()
                silent.send(
                    response,
                    ctx.chatId()
                )
            }
            .build()
    }

    fun defaultTag(): Ability {
        return Ability
            .builder()
            .name("dtag")
            .info("Set or get default tag")
            .locality(Locality.ALL)
            .privacy(Privacy.PUBLIC)
            .action { ctx: MessageContext ->
                val response = abilityTagService.setOrGetDefaultTag(ctx)
                silent.send(
                    response,
                    ctx.chatId()
                )
            }
            .build()
    }

    fun getDeals(): Ability {
        return Ability
            .builder()
            .name("deals")
            .info("Get deals")
            .locality(Locality.ALL)
            .privacy(Privacy.PUBLIC)
            .action { ctx: MessageContext ->
                val response = abilityDealService.findAllDealsWithDefaultTag()
                silent.send(
                    response,
                    ctx.chatId()
                )
            }
            .build()
    }

    fun removeDeals(): Ability {
        return Ability
            .builder()
            .name("rmdeals")
            .info("Remove all deals by tag")
            .locality(Locality.ALL)
            .privacy(Privacy.PUBLIC)
            .action { ctx: MessageContext ->
                val response = abilityDealService.removeDealsByTag(ctx)
                silent.send(
                    response,
                    ctx.chatId()
                )
            }
            .build()
    }

    fun tagsKeyboard(): Ability {
        return Ability
            .builder()
            .name("tgkb")
            .info("Tags keyboard")
            .locality(Locality.ALL)
            .privacy(Privacy.PUBLIC)
            .action { ctx: MessageContext ->
                execute(
                    sendKeyboard(ctx.chatId(), abilityTagService.getKeyboardMarkup(), "Tags")
                )
            }
            .build()
    }

    private fun sendKeyboard(chatId: Long, markup: InlineKeyboardMarkup, header: String): SendMessage {
        return SendMessage().setChatId(chatId).setText(header).setReplyMarkup(markup)
    }
}
