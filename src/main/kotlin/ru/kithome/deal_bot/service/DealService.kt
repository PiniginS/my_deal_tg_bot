package ru.kithome.deal_bot.service

import org.springframework.stereotype.Service
import ru.kithome.deal_bot.entity.DealEntity
import ru.kithome.deal_bot.exception.DealBotException
import ru.kithome.deal_bot.repository.DealRepository
import java.time.LocalDateTime

@Service
class DealService(
    private val dealRepository: DealRepository,
    private val tagService: TagService
) {
    fun addDeal(tag: String, deal: String) {
        if (!tagService.isTagExist(tag)) throw DealBotException("Can't find tag \"$tag\"")

        var dealEntity = dealRepository.findFirstByDescriptionAndTag(deal, tag)

        if (dealEntity == null) {
            dealEntity = DealEntity()
            dealEntity.isActive = true
            dealEntity.tag = tag
            dealEntity.timestamp = LocalDateTime.now().toString()
            dealEntity.description = deal
        } else {
            dealEntity.isActive = true
            dealEntity.timestamp = LocalDateTime.now().toString()
        }

        dealRepository.save(dealEntity)
    }

    fun removeDeal(tag: String, deal: String) {
        if (!tagService.isTagExist(tag)) throw DealBotException("Can't find tag \"$tag\"")
        val dealEntity = dealRepository.findFirstByDescriptionAndTag(deal, tag)
            ?: throw DealBotException("Can't find deal : \"$deal\" with tag : \"$tag\"")
        dealEntity.isActive = false
        dealRepository.save(dealEntity)
    }

    fun findDeals(tag: String): List<DealEntity> {
        return dealRepository.findAllByTag(tag)
            .filter { it.isActive }
    }

    fun removeDealsByTag(tag: String) {
        dealRepository.findAllByTag(tag)
            .filter { it.isActive }
            .forEach {
                it.isActive = false
                dealRepository.save(it)
            }
    }
}
