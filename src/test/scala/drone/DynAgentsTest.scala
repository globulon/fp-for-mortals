package drone

import fixture._
import org.scalatest.{MustMatchers, WordSpecLike}

final class DynAgentsTest extends WordSpecLike with MustMatchers with Data {
  "Business Logic" should {
    "generate an initial world view" in {
      val mutable = new Mutable(needsAgents)
      import mutable._
      program.initial must be (needsAgents)
    }

    "remove changed nodes from pending" in {
      val world = WorldView(0, 0, managed, Map(node1 -> time3), Map.empty, time3)
      val mutable = new Mutable(world)
      import mutable._
      val old = world.copy(alive = Map.empty,
        pending = Map(node1 -> time2),
        time = time2)
      program.update(old) must be (world)
    }

    "request agents when needed" in {
      val mutable = new Mutable(needsAgents)
      import mutable._

      program.act(needsAgents) must be (needsAgents.copy(pending = Map(node1 -> time1)))

      mutable.stopped must be (0)
      mutable.started must be(1)
    }

    "add node to pending" in {
      val world = WorldView(0, 0, managed, Map(node1 -> time1), Map.empty, time3)
      val mutable = new Mutable(world)
      import mutable._

      program.act(world) must be (WorldView(0, 0, managed, Map(node1 -> time1), Map(node1 -> time3), time3))
    }

    "add node to pending when backlog is not empty" in {
      val world = WorldView(1, 0, managed, Map(node1 -> time1), Map.empty, time4)
      val mutable = new Mutable(world)
      import mutable._

      program.act(world) must be (WorldView(1, 0, managed, Map(node1 -> time1), Map(node1 -> time4), time4))
    }

    "leave world untouched" in {
      val world = WorldView(1, 0, managed, Map(node1 → time1), Map.empty, time1)
      val mutable = new Mutable(world)
      import mutable._

      program.act(world) must be (world)
    }

  }
}
