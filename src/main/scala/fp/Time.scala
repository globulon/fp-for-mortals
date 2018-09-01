package fp

sealed trait Time

case class Epoch(millis: Long) extends Time