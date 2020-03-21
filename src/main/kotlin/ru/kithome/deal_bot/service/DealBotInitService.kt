package ru.kithome.deal_bot.service

import org.apache.http.HttpHost
import org.apache.http.client.config.RequestConfig
import org.springframework.stereotype.Service

import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.ApiContext
import org.telegram.telegrambots.meta.TelegramBotsApi
import ru.kithome.deal_bot.config.Bot
import ru.kithome.deal_bot.config.BotConfiguration

@Service
class DealBotInitService (private val botConfiguration: BotConfiguration){
    fun init() {
        ApiContextInitializer.init()

        val telegramBotApi = TelegramBotsApi()

        val botOptions = ApiContext.getInstance(DefaultBotOptions::class.java)

        val requestConfig = RequestConfig
            .custom()
            .setProxy(HttpHost(botConfiguration.proxyHost, botConfiguration.proxyPort.toInt()))
            .setAuthenticationEnabled(false)
            .build()

        botOptions.requestConfig = requestConfig
        botOptions.proxyHost = botConfiguration.proxyHost
        botOptions.proxyPort = botConfiguration.proxyPort.toInt()
        botOptions.proxyType = DefaultBotOptions.ProxyType.valueOf(botConfiguration.proxyType)

        telegramBotApi.registerBot(getBot(botOptions))
    }

    private fun getBot(botOptions: DefaultBotOptions) : Bot {
        return Bot(botConfiguration.token, botConfiguration.botName, botOptions)
    }
}