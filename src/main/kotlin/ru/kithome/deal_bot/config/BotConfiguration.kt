package ru.kithome.deal_bot.config

import org.springframework.boot.context.properties.ConfigurationProperties
import kotlin.properties.Delegates

@ConfigurationProperties(prefix = "bot")
class BotConfiguration {
    lateinit var token : String
    lateinit var botName : String
    lateinit var proxyHost : String
    lateinit var proxyPort : Number
    lateinit var proxyType : String
}