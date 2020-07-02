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
            dealEntity.isActive = false
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
        removeDeal(dealEntity.id)
    }

    fun findActiveDealsByTag(tag: String): List<DealEntity> {
        return dealRepository.findAllByTag(tag)
            .filter { it.isActive }
    }

    fun findDealsWithDefaultTag(): List<DealEntity> {
        return dealRepository.findAllByTag(tagService.getDefaultTag())
    }


    fun removeDealsByTag(tag: String) {
        dealRepository.findAllByTag(tag)
            .forEach {
                removeDeal(it.id)
            }
    }

    fun switchDealStatus(id: Int) {
        val deal = dealRepository.findById(id).orElseThrow {
            DealBotException("Can't find deal")
        }
        try {
            deal.isActive = !deal.isActive
            dealRepository.save(deal)
        }
        catch (exception : Exception) {
            throw DealBotException("Got error while save deal status")
        }
    }

    fun removeDeal(id: Int) {
        dealRepository.findById(id).orElseThrow {
            DealBotException("Can't find deal")
        }
        try {
            dealRepository.deleteById(id)
        }
        catch (exception : Exception) {
            throw DealBotException("Got error while removing deal")
        }
    }
}
