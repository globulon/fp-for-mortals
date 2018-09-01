package drone

import scala.language.higherKinds

trait Drone[F[_]] {
  def getBacklog: F[Int]
  def getAgents: F[Int]
}
