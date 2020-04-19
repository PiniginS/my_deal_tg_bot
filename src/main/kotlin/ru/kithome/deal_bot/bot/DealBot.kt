package ru.kithome.deal_bot.bot

import org.springframework.stereotype.Component
import org.telegram.abilitybots.api.bot.AbilityBot
import org.telegram.abilitybots.api.objects.Ability
import org.telegram.abilitybots.api.objects.Locality
import org.telegram.abilitybots.api.objects.MessageContext
import org.telegram.abilitybots.api.objects.Privacy
import ru.kithome.deal_bot.config.BotConfiguration
import ru.kithome.deal_bot.service.DealBotAbilityTagService


@Component
class DealBot(private val botAbilityTagService: DealBotAbilityTagService,
              botConfiguration: BotConfiguration
) : AbilityBot(botConfiguration.token, botConfiguration.botName, botConfiguration.getBotOptions()) {

    override fun creatorId(): Int {
        return 261560926
    }

//    override fun onUpdateReceived(update: Update?) {
//        super.onUpdateReceived(update)
//        update?.let {
//            try {
//                println("Echo ${update.message.text}")
//                tagService.addTag(update.message.text, "Новый тег")
//                sendMessage(update, "Tag created \"${update.message.text}\"")
//                sendMessage(update, "Tags : \n ${tagService.getAllTags().map { "${it.tag}:${it.description}" }}")
//            }
//            catch(e : Exception) {
//                sendMessage(update, "Error while creating tag : \"${update.message.text}\". Reason : " +
//                        "${e.message}")
//            }
//        }
//    }
//
//    fun sendTet (update: Update)  {
//        sender.execute(SendMessage(update.message.chatId,update.message.text))
//    }
//    fun sendMessage (update: Update, message : String)  {
//        sender.execute(SendMessage(update.message.chatId,message))
//    }

    fun addTag() : Ability {
        return Ability
            .builder()
            .name("tag")
            .info("addTag [TAG_NAME] [DESCRIPTION] - Add new tag")
            .locality(Locality.ALL)
            .privacy(Privacy.PUBLIC)
            .action { ctx: MessageContext ->
                val response = botAbilityTagService.addNewTag(ctx)
                silent.send(
                    response,
                    ctx.chatId()
                )
            }
            .build()
    }

    fun allTags() : Ability {
        return Ability
            .builder()
            .name("tags")
            .info("List of all tags")
            .locality(Locality.ALL)
            .privacy(Privacy.PUBLIC)
            .action { ctx: MessageContext ->
                val response = botAbilityTagService.getAllTags()
                silent.send(
                    response,
                    ctx.chatId()
                )
            }
            .build()
    }

    fun defaultTag() : Ability {
        return Ability
            .builder()
            .name("dtag")
            .info("Set or get default tag")
            .locality(Locality.ALL)
            .privacy(Privacy.PUBLIC)
            .action { ctx: MessageContext ->
                val response = botAbilityTagService.setOrGetDefaultTag(ctx)
                silent.send(
                    response,
                    ctx.chatId()
                )
            }
            .build()
    }

}