package cc.sukazyo.minecraft_telegram.config

import cc.sukazyo.minecraft_telegram.config.ProxyConfig.ProxyType
import io.circe.{Codec, Decoder, HCursor, Json}
import io.circe.generic.semiauto.deriveCodec
import io.circe.Decoder.Result

import java.net.{InetSocketAddress, Proxy}

case class ProxyConfig (
	
	`type`: ProxyType,
	host: String,
	port: Int,
	
) {
	
	def getProxy: Proxy =
		Proxy(`type`.value, InetSocketAddress(host, port))
	
}

object ProxyConfig {
	
	enum ProxyType(val value: Proxy.Type):
		case socks5 extends ProxyType(Proxy.Type.SOCKS)
		case http extends ProxyType(Proxy.Type.HTTP)
	
	implicit val proxyConfigCodec: Codec[ProxyConfig] = deriveCodec[ProxyConfig]
	implicit val proxyTypeCodec: Codec[ProxyType] = new Codec[ProxyType] {
		override def apply (c: HCursor): Result[ProxyType] =
			c.as[String].map(ProxyType.valueOf)
		override def apply (a: ProxyType): Json =
			Json.fromString(a.toString)
	}
	
}
