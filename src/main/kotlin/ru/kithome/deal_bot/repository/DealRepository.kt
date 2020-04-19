package ru.kithome.deal_bot.repository

import org.springframework.data.repository.CrudRepository
import ru.kithome.deal_bot.entity.DealEntity
import ru.kithome.deal_bot.entity.TagEntity

interface DealRepository : CrudRepository<DealEntity, Int>{
    fun findAllByTag(tag : String) : List<DealEntity>
}