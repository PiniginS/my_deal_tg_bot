package ru.kithome.deal_bot.service.ability

import org.springframework.stereotype.Service
import org.telegram.abilitybots.api.objects.MessageContext
import ru.kithome.deal_bot.exception.DealBotException
import ru.kithome.deal_bot.service.DealService
import ru.kithome.deal_bot.service.TagService

@Service
class AbilityDealService(
    private val tagService: TagService,
    private val dealService: DealService
) {

    fun processDealWithDefaultTag(deal: String): String {
        var defaultTag: String? = null
        return try {
            defaultTag = getDefaultTag()

            val dealText = trimDeal(deal)
            if (deal.startsWith("+")) {
                addDeal(defaultTag, dealText)
            } else {
                removeDeal(defaultTag, dealText)
            }
        } catch (exception: Exception) {
            "Error while try to add deal with default tag $defaultTag due : ${exception.message}"
        }
    }

    private fun addDeal(defaultTag: String, dealText: String): String {
        dealService.addDeal(defaultTag, dealText)
        return "$dealText with tag $defaultTag successful created"
    }

    private fun removeDeal(defaultTag: String, dealText: String): String {
        dealService.removeDeal(defaultTag, dealText)
        return "$dealText with tag $defaultTag successful removed"
    }

    fun findAllDealsWithDefaultTag(): String {
        var defaultTag: String? = null
        return try {
            defaultTag = getDefaultTag()

            val answer = dealService.findDeals(defaultTag).joinToString(separator = "\n") { "${it.description}" }

            if (answer.isEmpty())
                return "No items"
            else
                answer
        } catch (exception: Exception) {
            "Error while try to get deals with default tag \"$defaultTag\" due : ${exception.message}"
        }
    }

    fun removeDealsByTag(context: MessageContext): String {
        val tag = context.arguments().getOrNull(0)
        tag?.let {
            if (!tagService.isTagExist(tag)) throw DealBotException("\"$tag\" is not exist")
            dealService.removeDealsByTag(tag)
            return "Successful remove all deals for tag : \"$tag\""
        }

        return "Invalid argument, option [TAG] is missing"
    }

    private fun getDefaultTag(): String {
        return try {
            tagService.getDefaultTag()
        } catch (exception: Exception) {
            throw DealBotException("Can't get default tag")
        }
    }

    private fun trimDeal(deal: String): String {
        return deal.replace(Regex("^[+-]"), "")
    }

//
//    fun getTagsKeyboard(): SendMessage {
//        tagService.getAllActiveTags().map {
//            var row : List<InlineKeyboardButton> = listOf(
//
//            )
//        }
//    }
//
//    fun toggleActiveTag(tag : String) : String {
//        return try {
//            val newState = tagService.toggleTagFlag(tag)
//            "Tag : \"$tag\" is active? : $newState"
//        }
//        catch (exception : Exception) {
//            "Error while deactivate tag : \"$tag\" due : ${exception.message}"
//        }
//    }
}
