package ru.kithome.deal_bot.service.bot

import com.vdurmont.emoji.EmojiParser
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.kithome.deal_bot.type.KeyboardType
import java.util.ArrayList

@Service
class BotMenuService() {
    fun getKeyboardMarkup(): InlineKeyboardMarkup {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        val buttonsRowList: MutableList<List<InlineKeyboardButton>> = ArrayList()

        val tagMenuRow: MutableList<InlineKeyboardButton> = ArrayList()
        val tagMenuButton = InlineKeyboardButton()
        tagMenuButton.text = EmojiParser.parseToUnicode("Tags")
        tagMenuButton.callbackData = "@showKeyboard:${KeyboardType.TAGS}"
        tagMenuRow.add(tagMenuButton)

        val dealsMenuRow: MutableList<InlineKeyboardButton> = ArrayList()
        val dealsMenuButton = InlineKeyboardButton()
        dealsMenuButton.text = EmojiParser.parseToUnicode("Deals")
        dealsMenuButton.callbackData = "@showKeyboard:${KeyboardType.DEALS}"
        dealsMenuRow.add(dealsMenuButton)

        buttonsRowList.add(tagMenuRow)
        buttonsRowList.add(dealsMenuRow)

        inlineKeyboardMarkup.keyboard = buttonsRowList
        return inlineKeyboardMarkup
    }
}
