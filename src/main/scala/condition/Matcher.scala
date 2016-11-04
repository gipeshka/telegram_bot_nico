package condition

import info.mukel.telegrambot4s.models.Message

object Matcher
{
  type Matcher = Message => Boolean

  private def TextMatcher(stringMatches: String => Boolean): Matcher = _.text.map(stringMatches).getOrElse(false)

  val ExactTextMatcher = (s: String) => TextMatcher(_ == s)

  val RegexpTextMatcher = (regexp: String) => TextMatcher(_.matches(regexp))

  val StickerSentMatcher: Matcher = _.sticker.nonEmpty
}