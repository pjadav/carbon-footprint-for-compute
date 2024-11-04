package com.trainline.carbonfootprintforcompute

import cats.effect.Concurrent
import cats.implicits.{toFunctorOps, toTraverseOps}
import io.circe.generic.semiauto.deriveEncoder
import io.circe.Encoder
import org.http4s.EntityEncoder
import org.http4s.circe._
import org.http4s.client.Client

import java.time.Instant

trait EmissionsForLLM[F[_]] {
  def get(region: String): F[EmissionsForLLM.EmissionLLM]
  def getForAllRegion: F[List[EmissionsForLLM.EmissionLLM]]
  def getLlmPerRegionWithCo2(modelName:String): F[List[EmissionsForLLM.EmissionLLM]]
}

object EmissionsForLLM {
  final case class EmissionLLM(
    zone: String,
    carbonEmissionPerMilQueryInKg: Int,
    datetime: Instant,
    updatedAt: Instant,
    createdAt: Instant
  )

  object EmissionLLM {
    implicit val emissionLLMcoder: Encoder[EmissionsForLLM.EmissionLLM] = deriveEncoder[EmissionsForLLM.EmissionLLM]
    implicit def emissionLLMEntityEncoder[F[_]]: EntityEncoder[F, EmissionsForLLM.EmissionLLM] =
      jsonEncoderOf

    implicit def emissionLLMListEntityEncoder[F[_]]: EntityEncoder[F, List[EmissionsForLLM.EmissionLLM]] =
      jsonEncoderOf
  }

  def impl[F[_]: Concurrent](C: Client[F]): EmissionsForLLM[F] = new EmissionsForLLM[F] {
    def get(region: String): F[EmissionsForLLM.EmissionLLM] =
      CO2API.impl(C).get(region).map { v =>
        EmissionLLM(
          carbonEmissionPerMilQueryInKg =  (1000 * v.carbonIntensity)/1000,
          datetime = v.datetime,
          updatedAt = v.updatedAt,
          createdAt = v.createdAt,
          zone = v.zone
        )
      }

    def getForAllRegion: F[List[EmissionsForLLM.EmissionLLM]] =
      awsRegionToEnergyRegion.map(v => get(v._1)).toList.sequence

    def getLlmPerRegionWithCo2(modelName:String): F[List[EmissionsForLLM.EmissionLLM]] =
      LLMModelPerRegion(modelName).filter(_._2).map(v => get(v._1)).toList.sequence

  }
}
