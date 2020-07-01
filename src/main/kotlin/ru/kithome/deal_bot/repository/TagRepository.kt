package ru.kithome.deal_bot.repository

import org.springframework.data.repository.CrudRepository
import ru.kithome.deal_bot.entity.TagEntity

interface TagRepository : CrudRepository<TagEntity, Int> {
    fun findByTag(tag: String): TagEntity
}
