package ru.kithome.deal_bot.service.ability

import org.springframework.stereotype.Service
import ru.kithome.deal_bot.exception.DealBotException
import ru.kithome.deal_bot.service.DealService
import ru.kithome.deal_bot.service.TagService

@Service
class AbilityDealService(private val tagService: TagService,
                         private val dealService: DealService) {

    fun addDealWithDefaultTag(deal : String) : String {
        var defaultTag : String? = null
        return try {
            defaultTag = getDefaultTag()
            dealService.addDeal(defaultTag, deal)
            "$deal with tag $defaultTag successful created"
        } catch (exception : Exception) {
            "Error while try to add deal with default tag \"$defaultTag\" due : ${exception.message}"
        }
    }

    fun findAllDealsWithDefaultTag() : String {
        var defaultTag : String? = null
        return try {
            defaultTag = getDefaultTag()
            dealService.findDeals(defaultTag).joinToString(separator = "\n") { "${it.description}" }
        } catch (exception : Exception) {
            "Error while try to get deals with default tag \"$defaultTag\" due : ${exception.message}"
        }
    }

    private fun getDefaultTag() : String {
        return try {
            tagService.getDefaultTag()
        }
        catch (exception : Exception) {
            throw DealBotException("Can't get default tag")
        }
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