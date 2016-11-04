import info.mukel.telegrambot4s.api.{Commands, Polling, TelegramBot}
import info.mukel.telegrambot4s.methods.{SendMessage, SendSticker}
import info.mukel.telegrambot4s.models.Message
import info.mukel.telegrambot4s.Implicits._

class MainFunctionalityBot(val token: String) extends TelegramBot with Polling with Commands {
  on("hello") { implicit msg => args =>
    reply(
      "Hello " + msg.from.map(_.firstName).getOrElse("stranger")
    )
  }

  override def handleMessage(message: Message) {
    Console.println(message.toString)
    (message.sticker.map(_.fileId), message.text) match {
      case (Some(fileId), _) => {
        Console.println(fileId)
        api.request(SendSticker(message.chat.id, fileId))
      }
      case (_, Some(text)) if text.matches("(?i).*plastic bag.*") => api.request(SendMessage(message.chat.id, "No plastic bags for you " + message.from.map(_.firstName).getOrElse(("stranger"))))
      case _ => super.handleMessage(message)
    }
  }
}

object MainFunctionalityBot {
  def run(token: String): Unit = {
    new MainFunctionalityBot(token).run()
  }
}
