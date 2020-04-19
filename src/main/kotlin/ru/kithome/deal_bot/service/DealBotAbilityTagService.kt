package ru.kithome.deal_bot.service

import org.springframework.stereotype.Service
import org.telegram.abilitybots.api.objects.MessageContext
import ru.kithome.deal_bot.repository.SettingsRepository

@Service
class DealBotAbilityTagService(private val tagService: TagService,
                               private val settingsRepository: SettingsRepository) {

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

    fun setOrGetDefaultTag(context: MessageContext) : String {
        val tag = context.arguments().getOrNull(0)
        tag?.let {
            return setDefaultTag(tag);
        }
        return "Default tag is ${getDefaultTag()}"
    }

    private fun setDefaultTag(tag : String) : String{
        return try {
            tagService.setDefaultTag(tag)
            "Default tag has been changed to $tag"
        } catch (exception : Exception) {
            "Can't change default tag due : ${exception.message}"
        }
    }

    private fun getDefaultTag(): String {
        return try {
            "Default tag is ${tagService.getDefaultTag()}"
        } catch (exception : Exception) {
            "Can't get default tag due : ${exception.message}"
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