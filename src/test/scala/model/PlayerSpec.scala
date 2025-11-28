package de.htwg.Uno.model
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers._





class PlayerSpec extends AnyWordSpec with Matchers{

    "A Player" should{
        "get an empty list" in {
            val player = Player("Melissa",index = 1)
            player.name shouldBe "Melissa"
            player.index shouldBe 1
        }
    }

}