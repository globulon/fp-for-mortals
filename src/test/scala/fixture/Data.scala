package fixture

import drone.{MachineNode, WorldView}
import fp._
import scalaz.NonEmptyList

trait Data {
  final protected def node1 = MachineNode("1243d1af-828f-4ba3-9fc0-a19d86852b5a")
  final protected def node2 = MachineNode("550c4943-229e-47b0-b6be-3d686c5f013f")
  final protected def managed = NonEmptyList(node1, node2)

  final protected def time1: Epoch = epoch"2017-03-03T18:07:00Z"
  final protected def time2: Epoch = epoch"2017-03-03T18:59:00Z" // +52 mins
  final protected def time3: Epoch = epoch"2017-03-03T19:06:00Z" // +59 mins
  final protected def time4: Epoch = epoch"2017-03-03T23:07:00Z" // +5 hours

  final protected def needsAgents = WorldView(5, 0, managed, Map.empty, Map.empty, time1)
}
