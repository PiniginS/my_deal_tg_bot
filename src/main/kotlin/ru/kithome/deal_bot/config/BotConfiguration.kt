package ru.kithome.deal_bot.config

import org.apache.http.HttpHost
import org.apache.http.client.config.RequestConfig
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.ApiContext
import ru.kithome.deal_bot.config.properties.BotProperties

@Service
class BotConfiguration(
    private val botProperties: BotProperties
) {

    fun getBotOptions(): DefaultBotOptions {
        val botOptions = ApiContext.getInstance(DefaultBotOptions::class.java)

        if (botProperties.proxyEnabled) {

            val requestConfig = RequestConfig
                .custom()
                .setProxy(HttpHost(botProperties.proxyHost, botProperties.proxyPort))
                .setAuthenticationEnabled(false)
                .build()

            botOptions.requestConfig = requestConfig
            botOptions.proxyHost = botProperties.proxyHost
            botOptions.proxyPort = botProperties.proxyPort
            botOptions.proxyType = DefaultBotOptions.ProxyType.valueOf(botProperties.proxyType)
        }

        return botOptions
    }
}
