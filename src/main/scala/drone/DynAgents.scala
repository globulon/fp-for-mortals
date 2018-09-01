package drone

import scalaz.Scalaz._
import scalaz._

import scala.concurrent.duration._
import scala.language.higherKinds

trait DynAgents[F[_]] {
  def initial: F[WorldView]
  def update(old: WorldView): F[WorldView]
  def act(world: WorldView): F[WorldView]
}

class DynAgentsModule[F[_]: Monad](D: Drone[F], M: Machines[F])
    extends DynAgents[F] {
  override def initial: F[WorldView] =
    for {
      db ← D.getBacklog
      da ← D.getAgents
      mm ← M.getManaged
      ma ← M.getAlive
      mt ← M.getTime
    } yield WorldView(db, da, mm, ma, Map.empty, mt)

  override def update(old: WorldView): F[WorldView] =
    for {
      snap ← initial
      changed = symdiff(old.alive.keySet, snap.alive.keySet)
      pending = (old.pending -- changed).filterNot {
        case (_, started) ⇒ (snap.time - started) >= 10.minutes
      }
    } yield snap.copy(pending = pending)

  private def symdiff[A](as: Set[A], bs: Set[A]): Set[A] =
    (as union bs) -- (as intersect bs)

  override def act(world: WorldView): F[WorldView] = world match {
    case NeedsAgent(node) ⇒ M.start(node) map { addPending(_)(world) }
    case Stale(nodes)     ⇒ nodes.foldLeftM(world) { case (w, n) ⇒ M.stop(n) map { addPending(_)(w) } }
    case _                ⇒ world.pure[F]
  }

  private def addPending: MachineNode ⇒ WorldView ⇒ WorldView =
    stopped ⇒ world ⇒ world.copy(pending = world.pending + (stopped → world.time))

}