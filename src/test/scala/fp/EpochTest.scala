package fp

import java.util.concurrent.TimeUnit.{MILLISECONDS, SECONDS}

import org.scalatest.{MustMatchers, WordSpecLike}

import scala.concurrent.duration.Duration

final class EpochTest extends WordSpecLike with MustMatchers {
  "epoch" should {
    "be incremented" in {
      Epoch(500) + Duration(500, MILLISECONDS) must be (Duration(1, SECONDS))
    }

    "be decremented" in {
      Epoch(500) - Duration(200, MILLISECONDS) must be (Duration(300, MILLISECONDS))
    }
  }
}
