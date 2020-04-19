package ru.kithome.deal_bot.repository

import org.springframework.data.repository.CrudRepository
import ru.kithome.deal_bot.entity.SettingsEntity
import ru.kithome.deal_bot.entity.TagEntity

interface SettingsRepository : CrudRepository<SettingsEntity, Int>{
    fun findByKey(key : String) : SettingsEntity
}