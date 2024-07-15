package cc.sukazyo.minecraft_telegram.bot.events

import cc.sukazyo.cono.morny.system.telegram_api.event.{EventEnv, EventListener}
import cc.sukazyo.cono.morny.system.telegram_api.formatting.TelegramFormatter.*
import cc.sukazyo.minecraft_telegram.bot.{Bot, BotExt}
import cc.sukazyo.minecraft_telegram.utils.Magics
import cc.sukazyo.minecraft_telegram.ModMinecraftTelegram
import cc.sukazyo.minecraft_telegram.utils.CommonFormat.formatDuration
import cc.sukazyo.minecraft_telegram.utils.MinecraftServerExtension.MinecraftServerExt
import cc.sukazyo.std.data.EncapsulateValue
import net.minecraft.text.{Style, Text}
import net.minecraft.util.Formatting
import org.apache.logging.log4j.Logger

class OnTelegram2Minecraft (using bot: Bot)(using logger: Logger) extends EventListener with BotExt {
	
	override def onMessage (using event: EventEnv): Unit = {
		import event.update.message
		
		if message.chat.id != minecraftLinkedChat then return;
		
		val messageFormatted: Text =
			if message.text != null then
				Text.literal(message.text)
			else if message.sticker != null then
				Text.empty
					.append(Text.literal("[Sticker").formatted(Formatting.GREEN))
					.append(Text.literal(" " + message.sticker.emoji))
					.append(Text.literal("]").formatted(Formatting.GREEN))
			else if message.animation != null then
				Text.empty
					.append(Text.literal("[Animation").formatted(Formatting.GREEN))
					.append(Text.literal(" " + formatDuration(message.animation.duration)).formatted(Formatting.YELLOW))
					.append(message.animation.fileName match { case null => Text.empty; case fileName => Text.literal(" " + fileName) })
					.append(Text.literal("]").formatted(Formatting.GREEN))
			else if message.photo != null then
				Text.literal(s"[Photo]").formatted(Formatting.GREEN)
			else if message.video != null then
				Text.empty
					.append(Text.literal("[Video").formatted(Formatting.GREEN))
					.append(Text.literal(" " + formatDuration(message.video.duration)).formatted(Formatting.YELLOW))
					.append(message.video.fileName match { case null => Text.empty; case fileName => Text.literal(" " + fileName) })
					.append(Text.literal("]").formatted(Formatting.GREEN))
			else if message.audio != null then
				Text.empty
					.append(Text.literal("[Audio").formatted(Formatting.GREEN))
					.append(Text.literal(" " + formatDuration(message.audio.duration)).formatted(Formatting.YELLOW))
					.append(message.audio.fileName match { case null => Text.empty; case fileName => Text.literal(" " + fileName) })
					.append(Text.literal("]").formatted(Formatting.GREEN))
			else if message.voice != null then
				Text.empty
					.append(Text.literal("[Voice").formatted(Formatting.GREEN))
					.append(Text.literal(" " + formatDuration(message.voice.duration)).formatted(Formatting.YELLOW))
					.append(Text.literal("]").formatted(Formatting.GREEN))
			else if message.videoNote != null then
				Text.empty
					.append(Text.literal("[VideoNote").formatted(Formatting.GREEN))
					.append(Text.literal(" " + formatDuration(message.videoNote.duration)).formatted(Formatting.YELLOW))
					.append(Text.literal("]").formatted(Formatting.GREEN))
			else if message.paidMedia != null then
				Text.empty
					.append(Text.literal("[PaidMedia").formatted(Formatting.GREEN))
					.append(Text.literal(" $" + message.paidMedia.starCount).formatted(Formatting.GOLD))
					.append(Text.literal("]").formatted(Formatting.GREEN))
			else if message.document != null then
				Text.empty
					.append(Text.literal("[Document").formatted(Formatting.GREEN))
					.append(message.document.fileName match { case null => Text.empty; case fileName => Text.literal(" " + fileName) })
					.append(Text.literal("]").formatted(Formatting.GREEN))
			else if message.story != null then
				Text.literal(s"[Story]").formatted(Formatting.GREEN)
			else if message.contact != null then
				Text.empty
					.append(Text.literal("[Contact").formatted(Formatting.GREEN))
					.append(Text.literal(" " + message.contact.firstName + (if message.contact.lastName ne null then " " + message.contact.lastName else "")))
					.append(Text.literal("]").formatted(Formatting.GREEN))
			else if message.location != null then
				Text.literal(s"[Location]").formatted(Formatting.GREEN)
			else if message.venue != null then
				Text.empty
					.append(Text.literal("[Venue").formatted(Formatting.GREEN))
					.append(Text.literal(" " + message.venue.title + " : " + message.venue.address))
					.append(Text.literal("]").formatted(Formatting.GREEN))
			else if message.game != null then
				Text.empty
					.append(Text.literal("[Game").formatted(Formatting.GREEN))
					.append(Text.literal(" " + message.game.title).formatted(Formatting.AQUA))
					.append(Text.literal("]").formatted(Formatting.GREEN))
			else if message.poll != null then
				Text.empty
					.append(Text.literal("[Pool").formatted(Formatting.GREEN))
					.append(Text.literal(" " + message.poll.question))
					.append(Text.literal("]").formatted(Formatting.GREEN))
			else if message.dice != null then
				Text.empty()
					.append(Text.literal("[Dice ").formatted(Formatting.GREEN))
					.append(Text.literal(message.dice.value.toString))
					.append(Text.literal("]").formatted(Formatting.GREEN))
			else
				Text.literal("[Unsupported Message]").formatted(Formatting.RED)
		
		val maybeViaBot: Option[Text] =
			if message.viaBot != null then
				Text.empty
					.append(Text.literal("[via").formatted(Formatting.DARK_AQUA))
					.append(Text.literal(s" @${message.viaBot.username}").formatted(Formatting.AQUA))
					.append(Text.literal("]").formatted(Formatting.DARK_AQUA))
					.toSome
			else None
		
		val maybeForwardInfo: Option[Text] =
			if message.forwardOrigin != null then
				Text.empty
					.append(Text.literal("[Forwarded from").formatted(Formatting.DARK_AQUA))
					.append(Text.literal(s" ${message.forwardFrom.fullname}").formatted(Formatting.AQUA))
					.append(Text.literal("]").formatted(Formatting.DARK_AQUA))
					.toSome
			else None
		
		val maybeReplyTo: Option[Text] =
			if message.replyToMessage != null then
				Text.empty
					.append(Text.literal("[In reply to")).formatted(Formatting.DARK_PURPLE)
					.append(Text.literal(" " + message.replyToMessage.from.fullname).formatted(Formatting.AQUA))
					.append(Text.literal("]")).formatted(Formatting.DARK_PURPLE)
					.toSome
			else None
		
		val maybeCaption: Option[Text] =
			if message.caption != null then
				Text.literal(message.caption).toSome
			else
				None
		
		val senderName = message.from.fullname
		val senderNameTag: Text =
			Text.literal(s"<<$senderName>>")
				.setStyle(Style.EMPTY.withColor(Magics.COLOR_TELEGRAM_ICON))
		
		val mcMessage = Text.empty()
		var started = false
		for (maybeText <- senderNameTag.toSome :: maybeReplyTo :: maybeForwardInfo :: maybeViaBot :: messageFormatted.toSome :: maybeCaption :: Nil) {
			if maybeText.nonEmpty then
				val text = maybeText.get
				if started then
					mcMessage.append(Text.literal(" "))
				mcMessage.append(text)
				started = true
		}
		
		ModMinecraftTelegram.SERVER.broadcastMessage(mcMessage)
		
		event.setEventOk
		
	}
	
}
