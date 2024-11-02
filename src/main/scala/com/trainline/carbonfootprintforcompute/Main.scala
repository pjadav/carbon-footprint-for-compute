package com.trainline.carbonfootprintforcompute

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple {
  val run = CarbonfootprintforcomputeServer.run[IO]
}