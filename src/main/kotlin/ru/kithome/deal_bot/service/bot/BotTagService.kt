package ru.kithome.deal_bot.service.bot

import com.vdurmont.emoji.EmojiParser
import org.springframework.stereotype.Service
import org.telegram.abilitybots.api.objects.MessageContext
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.kithome.deal_bot.service.TagService
import java.util.ArrayList

@Service
class BotTagService(
    private val tagService: TagService
) {

    fun addNewTag(context: MessageContext): String {
        try {
            val tag = context.arguments().getOrNull(0)
            tag?.let {
                tagService.addTag(tag, context.arguments().getOrNull(1))
                return "Tag $tag create successful"
            }
            return "Invalid params : ${context.arguments().map { it }}"
        } catch (e: Exception) {
            return "Can't add tag due : ${e.message}"
        }
    }

    fun getAllTags(): String {
        return try {
            tagService.getAllActiveTags().joinToString(
                separator = "\n",
                prefix = "Tags ={\n",
                postfix = "\n}"
            ) { "${it.tag} : ${it.description}" }
        } catch (e: Exception) {
            "Can't get tags list due : ${e.message}"
        }
    }

    fun setOrGetDefaultTag(context: MessageContext): String {
        val tag = context.arguments().getOrNull(0)
        tag?.let {
            return setDefaultTag(tag)
        }
        return getDefaultTag()
    }

    private fun setDefaultTag(tag: String): String {
        return try {
            tagService.setDefaultTag(tag)
            "Default tag has been changed to $tag"
        } catch (exception: Exception) {
            "Can't change default tag due : ${exception.message}"
        }
    }

    private fun getDefaultTag(): String {
        return try {
            "Default tag is ${tagService.getDefaultTag()}"
        } catch (exception: Exception) {
            "Can't get default tag due : ${exception.message}"
        }
    }

    fun getKeyboardMarkup(): InlineKeyboardMarkup {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        val buttonsRowList: MutableList<List<InlineKeyboardButton>> = ArrayList()

        val defaultTagName = tagService.getDefaultTag()

        for (tag in tagService.getAllActiveTags()) {
            val tagNameButton = InlineKeyboardButton()
            val removeTagButton = InlineKeyboardButton()
            val buttonsRow: MutableList<InlineKeyboardButton> = ArrayList()

            if (tag.tag.equals(defaultTagName)) {
                tagNameButton.text = EmojiParser.parseToUnicode(":white_check_mark:${tag.tag} : ${tag.description}")
            } else {
                tagNameButton.text = "${tag.tag} : ${tag.description}"
            }
            tagNameButton.callbackData = "@setDefaultTag:${tag.tag}"

            removeTagButton.text = EmojiParser.parseToUnicode(":wastebasket:")
            removeTagButton.callbackData = "@removeTag:${tag.tag}"

            buttonsRow.add(tagNameButton)
            buttonsRow.add(removeTagButton)
            buttonsRowList.add(buttonsRow)
        }
        inlineKeyboardMarkup.keyboard = buttonsRowList
        return inlineKeyboardMarkup
    }
}
