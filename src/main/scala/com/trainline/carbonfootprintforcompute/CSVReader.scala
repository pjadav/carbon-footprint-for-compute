package com.trainline.carbonfootprintforcompute

import scala.io.Source

object AttoCsvReader{

  private def stringToBoolean(s:String) :Boolean = if (s == "Yes") true else false
  def readCsvFile(filePath: String): Map[String,Map[String,Boolean]] = {
    val source = Source.fromFile(filePath)
    try {
      source.getLines().drop(1).map { line =>
        val Array(modelName, eu_central_1, eu_west_1,eu_west_2,eu_west_3) = line.split(",").map(_.trim)
         (modelName , Map("eu-central-1" -> stringToBoolean(eu_central_1),
          "eu-west-1" -> stringToBoolean(eu_west_1),"eu-west-2" -> stringToBoolean(eu_west_2), "eu-west-3" -> stringToBoolean(eu_west_3)))
      }.toMap
    } finally {
      source.close()
    }
  }
}




