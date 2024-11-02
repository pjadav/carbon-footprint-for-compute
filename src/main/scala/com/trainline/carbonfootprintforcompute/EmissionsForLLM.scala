package com.trainline.carbonfootprintforcompute

import cats.effect.Concurrent
import cats.implicits.toFunctorOps
import io.circe.generic.semiauto.deriveEncoder
import io.circe.Encoder
import org.http4s.EntityEncoder
import org.http4s.circe._
import org.http4s.client.Client

import java.time.Instant

trait EmissionsForLLM[F[_]]{
  def get(region:String): F[EmissionsForLLM.EmissionLLM]
}

object EmissionsForLLM {
  final case class EmissionLLM(
                                zone: String,
                                carbonEmissionPerMilQueryInKg:  Int,
                                datetime: Instant,
                                updatedAt: Instant,
                                createdAt: Instant)

  final case class EmissionLLMForAllRegion(
                                zone: String,
                                carbonEmissionPerMilQueryInKg:  Map[String,Int],
                                datetime: Instant,
                                updatedAt: Instant,
                                createdAt: Instant)

  object EmissionLLM {
    implicit val emissionLLMcoder: Encoder[EmissionsForLLM.EmissionLLM] = deriveEncoder[EmissionsForLLM.EmissionLLM]
    implicit def emissionLLMEntityEncoder[F[_]]: EntityEncoder[F, EmissionsForLLM.EmissionLLM] =
      jsonEncoderOf
  }

  def impl[F[_] : Concurrent](C: Client[F]): EmissionsForLLM[F] = new EmissionsForLLM[F] {
    def get(region: String): F[EmissionsForLLM.EmissionLLM] = {
      val o = CO2API.impl(C).get(region)
        o.map { v =>
          EmissionLLM(
            carbonEmissionPerMilQueryInKg = 2900 * v.carbonIntensity, datetime = v.datetime, updatedAt = v.updatedAt, createdAt = v.createdAt, zone = v.zone
          )
        }
    }
  }
}
