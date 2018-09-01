package drone

import scalaz._
import scalaz.Scalaz._

sealed trait ActionDetected
case class NeedsAgent private (node: MachineNode) extends ActionDetected
case class Stale private (nodes: NonEmptyList[MachineNode])
  extends ActionDetected

object NeedsAgent {
  def unapply(arg: WorldView): Option[MachineNode] = arg match {
    case WorldView(backlog, 0, managed, alive, pending, _) if backlog > 0 && alive.isEmpty && pending.isEmpty ⇒
      Some(managed.head)
    case _ ⇒ None
  }
}

object Stale {
  def unapply(arg: WorldView): Option[NonEmptyList[MachineNode]] = arg match {
    case WorldView(backlog, _, _, alive, pending, time) if alive.nonEmpty ⇒
      (alive -- pending.keySet).collect {
        case (n, startTime) if backlog == 0 && (time - startTime).toMinutes % 60 >= 58 ⇒ n
        case (n, startTime) if (time -startTime).toHours >= 5                          ⇒ n
      }.toList.toNel
    case _                                                                ⇒ None
  }
}