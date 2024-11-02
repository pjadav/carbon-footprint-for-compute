package com.trainline.carbonfootprintforcompute

import cats.effect.Sync
import cats.implicits._
import com.trainline.carbonfootprintforcompute.EmissionsForEc2.Emission.emissionEC2EntityEncoder
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.circe.jsonEncoderOf
import org.http4s.{EntityEncoder, HttpRoutes}
import org.http4s.dsl.Http4sDsl

object CarbonfootprintforcomputeRoutes {

  def ec2CarbonFootPrintRoutes[F[_]: Sync](E: EmissionsForEc2[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "carbonCalculationForEc2" / region =>
        for {
          ec2Emission <- E.get(region)
          resp <- Ok(ec2Emission)
        } yield resp
    }
  }

  def llmCarbonFootPrintRoutes[F[_]: Sync](E: EmissionsForLLM[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "carbonCalculationForLLM" / region =>
        for {
          greeting <- E.get(region)
          resp <- Ok(greeting)
        } yield resp
    }
  }


  def availableRegionRoutes[F[_]: Sync]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    implicit val listCoder: Encoder[List[String]] = deriveEncoder[List[String]]
    implicit def listEntityEncoder[E[_]]: EntityEncoder[E, List[String]] =
      jsonEncoderOf

    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "availableRegions"  =>
        for {
          resp <- Ok(awsRegionToEnergyRegion.keys.toList)
        } yield resp
    }
  }
}