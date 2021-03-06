package net.drib.algetic

import com.twitter.algebird._

object CrossOver {
  implicit val str2Bytes = (x : String) => x.getBytes

  def MakeSketchMapMonoid(width : Long, depth : Long, count : Long) : SketchMapMonoid[String,Long] = {
    new SketchMapMonoid[String,Long](width.toInt,depth.toInt,123456,count.toInt)
  }

  // def MakeSketchMap(key : String, value : Long) : SketchMap[String,Long] = {
  //   new SketchMapMonoid[String,Long](100,5,123456,count.toInt)
  // }

}
