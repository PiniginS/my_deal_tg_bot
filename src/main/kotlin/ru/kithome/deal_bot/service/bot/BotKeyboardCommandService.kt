package ru.kithome.deal_bot.service.bot

import org.springframework.stereotype.Service
import ru.kithome.deal_bot.exception.DealBotException
import ru.kithome.deal_bot.model.CommandResponse
import ru.kithome.deal_bot.service.DealService
import ru.kithome.deal_bot.service.TagService
import ru.kithome.deal_bot.type.KeyboardType

@Service
class BotKeyboardCommandService(
    private val tagService: TagService,
    private val dealService: DealService
) {

    fun processCommand(callbackData: String): CommandResponse {
        val commandParts = callbackData.split(Regex("[:]"), 2)
        val command = commandParts[0]
        val arguments = commandParts[1].split(":")
        when (command) {
            "@setDefaultTag" -> return CommandResponse(setDefaultTag(arguments), KeyboardType.TAGS)
            "@removeTag" -> return CommandResponse(deleteTag(arguments), KeyboardType.TAGS)
            "@switchDealStatus" -> return CommandResponse(switchDealStatus(arguments), KeyboardType.DEALS)
            "@removeDeal" -> return CommandResponse(removeDeal(arguments), KeyboardType.DEALS)
            "@clearDeals" -> return CommandResponse(clearDeals(arguments), KeyboardType.DEALS)
            "@showKeyboard" -> return CommandResponse(nextKeyboard = getKeyboardType(arguments))
        }

        return CommandResponse("Command not found", KeyboardType.TAGS)
    }

    private fun setDefaultTag(arguments: List<String>): String {
        return try {
            tagService.setDefaultTag(arguments[0])
            "Default tag changed to ${arguments[0]}"
        } catch (e: DealBotException) {
            e.message + ""
        } catch (e: Exception) {
            "Error while set default tag ${arguments[0]}"
        }
    }

    private fun deleteTag(arguments: List<String>): String {
        return try {
            tagService.removeTag(arguments[0])
            "Tag ${arguments[0]} was removed"
        } catch (e: DealBotException) {
            return e.message + ""
        } catch (e: Exception) {
            "Error while remove tag ${arguments[0]}"
        }
    }

    private fun switchDealStatus(arguments: List<String>): String {
        try {
            val id = Integer.valueOf(arguments[0])
            dealService.switchDealStatus(id)
        } catch (e: DealBotException) {
            return e.message + ""
        } catch (e: Exception) {
            return "Error while switch deal status"
        }
        return ""
    }

    private fun removeDeal(arguments: List<String>): String {
        try {
            val id = Integer.valueOf(arguments[0])
            dealService.removeDeal(id)
        } catch (e: DealBotException) {
            return e.message + ""
        } catch (e: Exception) {
            return "Error while removing deal"
        }
        return ""
    }

    private fun clearDeals(arguments: List<String>): String {
        try {
            dealService.removeDealsByTag(arguments[0])
        } catch (e: DealBotException) {
            return e.message + ""
        } catch (e: Exception) {
            return "Error while removing deal all deals with tag : ${arguments[0]}"
        }
        return "All deals with tag : ${arguments[0]} was removed"
    }

    private fun getKeyboardType(arguments: List<String>): KeyboardType {
        return KeyboardType.valueOf(arguments[0])
    }
}
