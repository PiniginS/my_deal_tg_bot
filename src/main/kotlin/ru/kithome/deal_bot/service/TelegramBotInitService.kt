//package ru.kithome.deal_bot.service
//
//import org.springframework.stereotype.Service
//import org.telegram.telegrambots.ApiContextInitializer
//import org.telegram.telegrambots.meta.TelegramBotsApi
//import ru.kithome.deal_bot.bot.AbstractBot
//
//@Service
//class TelegramBotInitService (bots : List<AbstractBot>){
//    init {
//        bots.forEach { TelegramBotsApi().registerBot(it) }
//    }
//}