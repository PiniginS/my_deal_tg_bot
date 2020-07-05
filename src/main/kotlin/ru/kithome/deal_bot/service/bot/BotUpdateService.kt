package ru.kithome.deal_bot.service.bot

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.kithome.deal_bot.model.CallbackResponse

@Service
class BotUpdateService(
    private val botDealService: BotDealService,
    private val botKeyboardCommandService: BotKeyboardCommandService
) {

    fun processUpdate(update: Update?): CallbackResponse? {
        update?.let {
            if (update.hasMessage()) {
                return processMessage(update)
            } else if (update.hasCallbackQuery()) {
                return processCallback(update)
            }
        }
        return null
    }

    private fun processMessage(update: Update): CallbackResponse? {
        val updateText = update.message.text

        if (updateText.startsWith("/")) return null

        if (updateText.startsWith("+") || updateText.startsWith("-")) {
            return CallbackResponse(
                update.message.chatId,
                botDealService.processDealWithDefaultTag(update.message.text)
            )
        }
        return null
    }

    private fun processCallback(update: Update): CallbackResponse? {
        try {
            return if (update.callbackQuery.data.startsWith("@")) {
                val commandResponse = botKeyboardCommandService.processCommand(update.callbackQuery.data)
                CallbackResponse(
                    update.callbackQuery.message.chatId,
                    commandResponse.message,
                    commandResponse.nextKeyboard
                )
            } else {
                CallbackResponse(
                    update.callbackQuery.message.chatId,
                    update.callbackQuery.data
                )
            }
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
        return null
    }
}
