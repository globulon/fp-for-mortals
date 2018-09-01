package fixture

import drone._
import fp.Epoch
import scalaz._
import scalaz.Scalaz._

class Mutable(state: WorldView) {
  //TODO: in book use state machine
  var started, stopped: Int = 0

  private val D: Drone[Id] = new Drone[Id] {
    def getBacklog: Int = state.backlog
    def getAgents: Int = state.agents
  }

  private val M: Machines[Id] = new Machines[Id] {
    def getAlive: Map[MachineNode, Epoch] = state.alive
    def getManaged: NonEmptyList[MachineNode] = state.managed
    def getTime: Epoch = state.time
    def start(node: MachineNode): MachineNode = { started += 1 ; node }
    def stop(node: MachineNode): MachineNode = { stopped += 1 ; node }
  }

  val program = new DynAgentsModule[Id](D, M)
}