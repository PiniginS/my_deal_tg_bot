package ru.kithome.deal_bot.service.ability

import org.springframework.stereotype.Service
import ru.kithome.deal_bot.exception.DealBotException
import ru.kithome.deal_bot.service.DealService
import ru.kithome.deal_bot.service.TagService

@Service
class KeyboardCallbackService(
    private val tagService: TagService,
    private val dealService: DealService
) {

    fun processCommand(callbackData: String): String {
        val commandParts = callbackData.split(Regex("[:]"), 2)
        val command = commandParts[0]
        val arguments = commandParts[1].split(":")
        when (command) {
            "@setDefaultTag" -> return setDefaultTag(arguments)
            "@removeTag" -> return deleteTag(arguments)
            "@switchDealStatus" -> return switchDealStatus(arguments)
            "@removeDeal" -> return removeDeal(arguments)
            "@clearDeals" -> return clearDeals(arguments)
        }

        return "Command not found"
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
}
