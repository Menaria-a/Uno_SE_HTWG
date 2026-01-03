package de.htwg.Uno.model
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers._
import de.htwg.Uno.model.Model.Coulor
import de.htwg.Uno.model.Model.Symbol





class CardSpec extends AnyWordSpec with Matchers{

    "A Card" should {
        "be created correctly with a color and symbol" in {
            val card = Card(Coulor.red, Symbol.One)
            card.coulor shouldBe Coulor.red
            card.symbol shouldBe Symbol.One
        }
    }

}