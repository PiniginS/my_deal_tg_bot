package ru.kithome.deal_bot.service

import org.springframework.stereotype.Service
import ru.kithome.deal_bot.entity.DealEntity
import ru.kithome.deal_bot.exception.DealBotException
import ru.kithome.deal_bot.repository.DealRepository
import java.lang.Exception
import java.time.LocalDateTime

@Service
class DealService(
    private val dealRepository: DealRepository,
    private val tagService: TagService
) {
    fun addDeal(tag: String, deal: String) {
        if (!tagService.isTagExist(tag)) throw DealBotException("Can't find tag \"$tag\"")
        val dealEntity = DealEntity()
        dealEntity.isActive = true
        dealEntity.tag = tag
        dealEntity.timestamp = LocalDateTime.now().toString()
        dealEntity.description = deal
        dealRepository.save(dealEntity)
    }

    fun findDeals(tag: String) : List<DealEntity>{
        return dealRepository.findAllByTag(tag)
    }

}