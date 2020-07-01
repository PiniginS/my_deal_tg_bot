package ru.kithome.deal_bot.service

import org.springframework.stereotype.Service
import ru.kithome.deal_bot.entity.TagEntity
import ru.kithome.deal_bot.exception.DealBotException
import ru.kithome.deal_bot.repository.SettingsRepository
import ru.kithome.deal_bot.repository.TagRepository
import java.time.LocalDateTime

@Service
class TagService(
    private val tagRepository: TagRepository,
    private val settingsRepository: SettingsRepository
) {

    companion object {
        const val defaultTag = "defaultTag"
    }

    fun addTag(tag: String, description: String?) {
        val entity = TagEntity()
        entity.tag = tag
        entity.isActive = true
        entity.timestamp = LocalDateTime.now().toString()
        entity.description = description
        tagRepository.save(entity)
    }

    fun getAllActiveTags(): List<TagEntity> {
        return tagRepository.findAll().toList()
    }

    fun setDefaultTag(tag: String) {
        if (!isTagExist(tag)) throw DealBotException("Can't find tag \"$tag\"")
        val defaultFlagSetting = settingsRepository.findByKey(defaultTag)
        defaultFlagSetting.value = tag
        settingsRepository.save(defaultFlagSetting)
    }

    fun getDefaultTag(): String {
        val defaultFlagSetting = settingsRepository.findByKey(defaultTag)
        val defaultFlagValue = defaultFlagSetting.value
        defaultFlagValue?.let {
            return defaultFlagValue
        }
        throw DealBotException("Cant't get default flag")
    }

    fun isTagExist(tag: String): Boolean {
        return try {
            tagRepository.findByTag(tag)
            true
        } catch (exception: Exception) {
            false
        }
    }

    fun removeTag(tag: String) {
        if (!isTagExist(tag))
            throw DealBotException("Can't remove tag $tag, it doesn't exist")

        if (tag == getDefaultTag())
            throw DealBotException("Can't remove default tag $tag")
        tagRepository.deleteByTag(tag)
    }
}
