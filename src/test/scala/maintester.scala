package de.htwg.Uno

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers._
import game._

class MainSpec extends AnyWordSpec with Matchers{

    "THE MAIN" should {
        "run without errors" in {
        game.main(Array())
        }
    }
}
