package ru.kithome.deal_bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.TelegramBotsApi
import ru.kithome.deal_bot.config.BotConfiguration
import ru.kithome.deal_bot.service.DealBotService


@SpringBootApplication
@EnableConfigurationProperties(BotConfiguration::class)
open class Application

fun main(args: Array<String>) {
    ApiContextInitializer.init()
    val context = runApplication<Application>(*args)
    val dealBot = context.getBean(DealBotService::class.java)
    TelegramBotsApi().registerBot(dealBot)
}