package de.htwg.Uno.model
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers._
import de.htwg.Uno.model.Model.Card
import de.htwg.Uno.model.Model.Coulor
import de.htwg.Uno.model.Model.Symbol
import de.htwg.Uno.model.Model.Player
import de.htwg.Uno.model.Model.Game

class ModelSpec extends AnyWordSpec with Matchers{


    "A Card" should {
        "be created correctly with a color and symbol" in {
            val card = Card(Coulor.red, Symbol.One)
            card.colour shouldBe Coulor.red
            card.symbol shouldBe Symbol.One
        }
    }
    "A Player" should{
        "get an empty list" in {
            val player = Player("Melissa",index = 1)
            player.name shouldBe "Melissa"
            player.index shouldBe 1
        }
    }

    "A Symbol" should {
        "extend its value correctly" in {
            Symbol.Zero.value shouldBe 0 
            Symbol.One.value shouldBe 1
            Symbol.Two.value shouldBe 2
            Symbol.Three.value shouldBe 3
            Symbol.Four.value shouldBe 4
            Symbol.Five.value shouldBe 5
            Symbol.Six.value shouldBe 6
            Symbol.Seven.value shouldBe 7
            Symbol.Eight.value shouldBe 8
            Symbol.Nine.value shouldBe 9
        }
    }
}