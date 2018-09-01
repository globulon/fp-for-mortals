package fp

import java.time.Instant

import contextual._

sealed trait Time

case class Epoch(millis: Long) extends Time

object EpochInterpolator extends Verifier[Epoch] {
  def check(s: String): Either[(Int, String), Epoch] =
    try Right(Epoch(Instant.parse(s).toEpochMilli))
    catch { case _: Throwable ⇒ Left((0, "not in ISO-8601 format")) }
}

trait Epochs {
  implicit class EpochMillisStringContext(sc: StringContext) {
    val epoch = Prefix(EpochInterpolator, sc)
  }
}