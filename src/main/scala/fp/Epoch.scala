package fp

import java.util.concurrent.TimeUnit.MILLISECONDS

import scala.concurrent.duration._
import scala.language.{implicitConversions, postfixOps}

case class Epoch(millis: Long) {
  def +(d: Duration): Epoch = Epoch(millis + d.toMillis)
  def -(d: Duration): Epoch = Epoch(millis - d.toMillis)
}

trait Epochs {
  implicit def toDuration(e: Epoch): Duration = Duration(e millis, MILLISECONDS)
}
