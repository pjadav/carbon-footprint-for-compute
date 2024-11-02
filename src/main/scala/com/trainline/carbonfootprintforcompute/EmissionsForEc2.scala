package com.trainline.carbonfootprintforcompute

import cats.effect.Concurrent
import cats.implicits._
import com.trainline.carbonfootprintforcompute.EmissionsForEc2.EmissionEc2
import io.circe.Encoder
import io.circe.generic.semiauto._
import org.http4s._
import org.http4s.client.Client
import org.http4s.circe._

import java.time.Instant

trait EmissionsForEc2[F[_]]{
  def get(region:String): F[EmissionEc2]
}

object EmissionsForEc2 {
  def apply[F[_]](implicit ev: EmissionsForEc2[F]): EmissionsForEc2[F] = ev

  final case class EmissionEc2(
                              zone: String,
                              carbonEmissionPerHourGram:Map[String,Double],
                             datetime:Instant,
                             updatedAt:Instant,
                             createdAt:Instant)

  object Emission {
    implicit val emissionEC2coder: Encoder[EmissionsForEc2.EmissionEc2] = deriveEncoder[EmissionsForEc2.EmissionEc2]
    implicit def emissionEC2EntityEncoder[F[_]]: EntityEncoder[F, EmissionsForEc2.EmissionEc2] =
      jsonEncoderOf
  }

  final case class emissionError(e: Throwable) extends RuntimeException

  def impl[F[_]: Concurrent](C: Client[F]): EmissionsForEc2[F] = new EmissionsForEc2[F]{
    def get(region:String): F[EmissionsForEc2.EmissionEc2] = {
      CO2API.impl(C).get(region)
        .map{v =>
        EmissionEc2(
          carbonEmissionPerHourGram = nodeTypeWithPowerConsumptionPerHour.map(t => (t._1,t._2* v.carbonIntensity)), datetime = v.datetime, updatedAt = v.updatedAt, createdAt = v.createdAt,zone = v.zone
        )
      }
    }
  }
}
