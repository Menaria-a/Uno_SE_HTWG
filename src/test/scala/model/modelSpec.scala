package de.htwg.Uno.model
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers._
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.Model.Coulor
import de.htwg.Uno.model.Model.Symbol
import de.htwg.Uno.model.Player
import de.htwg.Uno.model.Game

class ModelSpec extends AnyWordSpec with Matchers{

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