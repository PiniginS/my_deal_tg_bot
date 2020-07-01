package ru.kithome.deal_bot.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "bot")
data class BotProperties(
    var token: String = "",
    var botName: String = "",
    var proxyHost: String = "",
    var proxyPort: Int = 0,
    var proxyType: String = "",
    var proxyEnabled: Boolean = false
)
