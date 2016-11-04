import info.mukel.telegrambot4s.api.{Commands, Polling, TelegramBot}
import info.mukel.telegrambot4s.methods.{SendMessage, SendSticker}
import info.mukel.telegrambot4s.models.Message
import info.mukel.telegrambot4s.Implicits._
import condition._
import Matcher._

import scala.collection.mutable

class MainFunctionalityBot(val token: String) extends TelegramBot with Polling with Commands
{
  type MessageAction = Message => Unit

  private val commands = mutable.HashMap[Matcher, MessageAction]()

  on(ExactTextMatcher("hello")) { implicit message =>
    reply(
      "Hello " + message.from.map(_.firstName).getOrElse("stranger")
    )
  }

  on(RegexpTextMatcher("(?i).*plastic bag.*")) { message =>
    api.request(SendSticker(message.chat.id, "BQADAgADOwMAAs7Y6AudsK8PsN71zAI"))
  }

  on(RegexpTextMatcher("(?i).*fuck it.*")) { implicit message =>
    reply("Fuck You too")
  }

  on(StickerSentMatcher) { message =>
    val fileId = message.sticker.map(_.fileId).get
    Console.println(fileId)
    api.request(SendSticker(message.chat.id, fileId))
  }

  override def handleMessage(message: Message)
  {
    Console.println(message.toString)
    val matchedAction = commands.find { case (matcher, action) => matcher(message) }.map(_._2)

    matchedAction match {
      case Some(action) => action(message)
      case _ => super.handleMessage(message)
    }
  }

  def on(matcher: Matcher)(action: MessageAction) {
    commands += (matcher -> action)
  }
}

object MainFunctionalityBot
{
  def run(token: String)
  {
    new MainFunctionalityBot(token).run()
  }
}