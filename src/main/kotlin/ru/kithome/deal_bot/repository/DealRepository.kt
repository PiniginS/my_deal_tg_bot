package ru.kithome.deal_bot.repository

import org.springframework.data.repository.CrudRepository
import ru.kithome.deal_bot.entity.DealEntity

interface DealRepository : CrudRepository<DealEntity, Int> {
    fun findAllByTag(tag: String): List<DealEntity>

    fun findFirstByDescriptionAndTag(description: String, tag: String): DealEntity?
}
