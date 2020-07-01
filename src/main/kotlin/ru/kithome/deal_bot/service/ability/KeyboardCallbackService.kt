package ru.kithome.deal_bot.service.ability

import org.springframework.stereotype.Service
import ru.kithome.deal_bot.exception.DealBotException
import ru.kithome.deal_bot.service.TagService

@Service
class KeyboardCallbackService(
    private val tagService: TagService
) {

    fun processCommand(callbackData: String): String {
        val commandParts = callbackData.split(Regex("[:]"), 2)
        val command = commandParts[0]
        val arguments = commandParts[1].split(":")
        when (command) {
            "@setDefaultTag" -> return setDefaultTag(arguments)
            "@removeTag" -> return deleteTag(arguments)
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
}