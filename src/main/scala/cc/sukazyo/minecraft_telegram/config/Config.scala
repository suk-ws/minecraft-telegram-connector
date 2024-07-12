package cc.sukazyo.minecraft_telegram.config

import cc.sukazyo.minecraft_telegram.bot.{BotApiServer, BotConfig}
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

case class Config (
	
	bot: BotConfig
	
)

object Config {
	implicit val configCodec: Codec[Config] = deriveCodec[Config]
	implicit val botConfigCodec: Codec[BotConfig] = deriveCodec[BotConfig]
	implicit val botApiServerCodec: Codec[BotApiServer] = deriveCodec[BotApiServer]
}
