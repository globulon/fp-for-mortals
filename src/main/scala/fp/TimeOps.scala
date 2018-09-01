package fp

import java.util.concurrent.TimeUnit.MILLISECONDS

import scala.concurrent.duration._
import scala.language.{implicitConversions, postfixOps}

protected[fp] trait TimeOps {
  implicit def toDuration(e: Epoch): Duration = Duration(e millis, MILLISECONDS)
}