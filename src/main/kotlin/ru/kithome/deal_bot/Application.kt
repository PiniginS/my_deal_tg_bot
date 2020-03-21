package ru.kithome.deal_bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import ru.kithome.deal_bot.config.BotConfiguration
import ru.kithome.deal_bot.service.DealBotInitService


@SpringBootApplication
@EnableConfigurationProperties(BotConfiguration::class)
open class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
        .getBean(DealBotInitService::class.java)
        .init()
}