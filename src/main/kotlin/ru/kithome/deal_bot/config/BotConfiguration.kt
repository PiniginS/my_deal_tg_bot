package ru.kithome.deal_bot.config

import org.apache.http.HttpHost
import org.apache.http.client.config.RequestConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.ApiContext
import org.telegram.telegrambots.meta.generics.BotOptions
import kotlin.properties.Delegates

@ConstructorBinding
@ConfigurationProperties(prefix = "bot")
data class BotConfiguration(
    val token: String,
    val botName: String,
    val proxyHost: String,
    val proxyPort: Int,
    val proxyType: String
    ) {

    fun getBotOptions(): DefaultBotOptions {
        val botOptions = ApiContext.getInstance(DefaultBotOptions::class.java)

        val requestConfig = RequestConfig
            .custom()
            .setProxy(HttpHost(proxyHost, proxyPort))
            .setAuthenticationEnabled(false)
            .build()

        botOptions.requestConfig = requestConfig
        botOptions.proxyHost = proxyHost
        botOptions.proxyPort = proxyPort
        botOptions.proxyType = DefaultBotOptions.ProxyType.valueOf(proxyType)

        return botOptions
    }
}