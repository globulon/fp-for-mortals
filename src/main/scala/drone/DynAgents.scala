package drone

import scalaz.Scalaz._
import scalaz._

import scala.concurrent.duration._

trait DynAgents[F[_]] {
  def initial: F[WorldView]
  def update(old: WorldView): F[WorldView]
  def act(world: WorldView): F[WorldView]
}

class DynAgentsModule[F[_]: Monad](D: Drone[F], M: Machines[F]) extends DynAgents[F] {
  override def initial: F[WorldView] = for {
    db ← D.getBacklog
    da ← D.getAgents
    mm ← M.getManaged
    ma ← M.getAlive
    mt ← M.getTime
  } yield WorldView(db, da, mm, ma, Map.empty, mt)

  override def update(old: WorldView): F[WorldView] = for {
    snap    ← initial
    changed = symdiff(old.alive.keySet, snap.alive.keySet)
    pending = (old.pending -- changed).filterNot {
      case (_, started) ⇒ (snap.time - started) >= (10 minutes)
    }
  } yield snap.copy(pending = pending)

  private def symdiff[A](as: Set[A], bs: Set[A]): Set[A] = (as union bs) -- (as intersect bs)

  override def act(world: WorldView): F[WorldView] = ???
}