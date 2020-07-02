package ru.kithome.deal_bot.service.ability

import com.vdurmont.emoji.EmojiParser
import org.springframework.stereotype.Service
import org.telegram.abilitybots.api.objects.MessageContext
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.kithome.deal_bot.exception.DealBotException
import ru.kithome.deal_bot.service.DealService
import ru.kithome.deal_bot.service.TagService
import java.util.ArrayList

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

            val answer = dealService.findActiveDealsByTag(defaultTag).joinToString(separator = "\n") { "${it.description}" }

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

    fun getKeyboardMarkup(): InlineKeyboardMarkup {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        val buttonsRowList: MutableList<List<InlineKeyboardButton>> = ArrayList()

        for (deal in dealService.findDealsWithDefaultTag()) {
            val dealNameButton = InlineKeyboardButton()
            val removeDealButton = InlineKeyboardButton()
            val buttonsRow: MutableList<InlineKeyboardButton> = ArrayList()

            if (deal.isActive) {
                dealNameButton.text = EmojiParser.parseToUnicode(":white_check_mark:${deal.description}:white_check_mark:")
            } else {
                dealNameButton.text = "${deal.description}"
            }
            dealNameButton.callbackData = "@switchDealStatus:${deal.id}"

            removeDealButton.text = EmojiParser.parseToUnicode(":wastebasket:")
            removeDealButton.callbackData = "@removeDeal:${deal.id}"

            buttonsRow.add(dealNameButton)
            buttonsRow.add(removeDealButton)
            buttonsRowList.add(buttonsRow)
        }

        val buttonsRow: MutableList<InlineKeyboardButton> = ArrayList()
        val clearAllDealsButton = InlineKeyboardButton()
        clearAllDealsButton.text = EmojiParser.parseToUnicode(":x:Remove all:x:")
        clearAllDealsButton.callbackData = "@clearDeals:${tagService.getDefaultTag()}"
        buttonsRow.add(clearAllDealsButton)
        buttonsRowList.add(buttonsRow)

        inlineKeyboardMarkup.keyboard = buttonsRowList
        return inlineKeyboardMarkup
    }
}
