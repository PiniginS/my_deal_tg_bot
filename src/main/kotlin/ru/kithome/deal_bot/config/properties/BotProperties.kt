package ru.kithome.deal_bot.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "bot")
data class BotProperties(
    val token: String,
    val botName: String,
    val proxyHost: String,
    val proxyPort: Int,
    val proxyType: String,
    val proxyEnabled : Boolean) {
}