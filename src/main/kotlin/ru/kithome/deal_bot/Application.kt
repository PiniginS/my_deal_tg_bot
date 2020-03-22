package ru.kithome.deal_bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.TelegramBotsApi
import ru.kithome.deal_bot.bot.AbstractBot
import ru.kithome.deal_bot.config.BotConfiguration


@SpringBootApplication
@EnableConfigurationProperties(BotConfiguration::class)
open class Application

fun main(args: Array<String>) {
    ApiContextInitializer.init()
    val context = runApplication<Application>(*args)
    context.getBeansOfType(AbstractBot::class.java).forEach { (_,v) -> TelegramBotsApi().registerBot(v)}
}