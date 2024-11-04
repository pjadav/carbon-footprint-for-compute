package com.trainline.carbonfootprintforcompute

import cats.effect.Concurrent
import cats.implicits.catsSyntaxMonadError
import com.trainline.carbonfootprintforcompute.CO2API.Emission
import com.trainline.carbonfootprintforcompute.EmissionsForEc2.emissionError
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.{EntityDecoder, EntityEncoder, Header, Headers}
import org.http4s.Method.GET
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.implicits.http4sLiteralsSyntax
import org.typelevel.ci.CIString

import java.time.Instant

trait CO2API[F[_]] {
  def get(region: String): F[Emission]
}

object CO2API {
  final case class Emission(
    zone: String,
    carbonIntensity: Int,
    datetime: Instant,
    updatedAt: Instant,
    createdAt: Instant,
    emissionFactorType: String,
    isEstimated: Boolean,
    estimationMethod: String
  )

  object Emission {
    implicit val emissionDecoder: Decoder[Emission] = deriveDecoder[Emission]

    implicit def emissionEntityDecoder[F[_]: Concurrent]: EntityDecoder[F, Emission] =
      jsonOf

    implicit val emissioncoder: Encoder[Emission] = deriveEncoder[Emission]

    implicit def emissionEntityEncoder[F[_]]: EntityEncoder[F, Emission] =
      jsonEncoderOf
  }

  def impl[F[_]: Concurrent](C: Client[F]): CO2API[F] = new CO2API[F] {
    val dsl = new Http4sClientDsl[F] {}
    import dsl._
    def get(region: String): F[Emission] = {
      val request = GET(
        uri"https://api.electricitymap.org/v3/carbon-intensity/latest"
          .withQueryParam("zone", awsRegionToEnergyRegion(region)),
        Headers(Header.Raw(CIString("auth-token"), "QeGjeYxqTVhXj"))
      )
      val o = C.expect[Emission](request).adaptError { case t =>
        emissionError(t)
      } // Prevent Client Json Decoding Failure Leaking
      o
    }
  }

  // sys.env.getOrElse("API_KEY", "")
}
