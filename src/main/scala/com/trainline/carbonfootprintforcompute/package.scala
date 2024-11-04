package com.trainline

package object carbonfootprintforcompute {
  private[carbonfootprintforcompute] val awsRegionToEnergyRegion: Map[String, String] = Map(
    "eu-central-1" -> "DE",
    "eu-west-1"    -> "IE",
    "eu-west-2"    -> "GB",
    "eu-south-1"   -> "IT",
    "eu-west-3"    -> "FR",
    "eu-south-2"   -> "ES",
    "eu-north-1"   -> "SE",
    "eu-central-2" -> "CH"
  )

  private[carbonfootprintforcompute] val nodeTypeWithPowerConsumptionPerHour: Map[String, Double] =
    Map("m" -> 0.044, "r" -> 0.068, "c" -> 0.032)

  private[carbonfootprintforcompute] val LLMModelPerRegion = AttoCsvReader.readCsvFile("/Users/prakash.jadav/LearningProject/carbon-footprint-for-compute/src/main/resources/LLM_availbility_per_region.csv")
}
