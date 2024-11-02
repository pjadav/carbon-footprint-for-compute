package com.trainline.carbonfootprintforcompute

import cats.effect.Async
import cats.syntax.all._
import com.comcast.ip4s._
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

object CarbonfootprintforcomputeServer {

  def run[F[_]: Async]: F[Nothing] = {
    for {
      client <- EmberClientBuilder.default[F].build
      helloWorldAlg = EmissionsForLLM.impl[F](client)
      jokeAlg = EmissionsForEc2.impl[F](client)

      // Combine Service Routes into an HttpApp.
      // Can also be done via a Router if you
      // want to extract segments not checked
      // in the underlying routes.
      httpApp = (
        CarbonfootprintforcomputeRoutes.llmCarbonFootPrintRoutes[F](helloWorldAlg) <+>
        CarbonfootprintforcomputeRoutes.ec2CarbonFootPrintRoutes[F](jokeAlg) <+>
        CarbonfootprintforcomputeRoutes.availableRegionRoutes
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      _ <- 
        EmberServerBuilder.default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalHttpApp)
          .build
    } yield ()
  }.useForever
}
