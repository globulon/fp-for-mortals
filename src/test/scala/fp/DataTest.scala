package fp

import org.scalatest.{MustMatchers, WordSpecLike}

final class DataTest extends WordSpecLike with MustMatchers {
  "sign of times" should {
    "send positive square to neg number" in {
      signOfTheTimes(-4) must be (16)
    }

    "send negative square to pos number" in {
      signOfTheTimes(7) must be(-49)
    }
  }
}
