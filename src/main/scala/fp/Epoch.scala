package fp

import java.util.concurrent.TimeUnit.MILLISECONDS

import scala.concurrent.duration._
import scala.language.{implicitConversions, postfixOps}

trait TimeShift[T <: Time] {
  def add(d: Duration): T ⇒ T
  def del(d: Duration): T ⇒ T
}

protected[fp] trait TimeOps {
  implicit def toDuration(e: Epoch): Duration = Duration(e millis, MILLISECONDS)

  implicit val epochShift: TimeShift[Epoch] = new TimeShift[Epoch] {
    override def add(d: Duration): Epoch ⇒ Epoch = e ⇒ Epoch(e.millis + d.toMillis)

    override def del(d: Duration): Epoch ⇒ Epoch = e ⇒ Epoch(e.millis + d.toMillis)
  }

  implicit class RichTime[T <: Time : TimeShift](t: T) {
    def +(d: Duration): T = implicitly[TimeShift[T]].add(d)(t)

    def -(d: Duration): T = implicitly[TimeShift[T]].del(d)(t)
  }
}