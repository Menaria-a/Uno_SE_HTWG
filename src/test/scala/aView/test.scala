
package de.htwg.Uno.aView

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers._
import de.htwg.Uno.modell.Model.Card
import de.htwg.Uno.modell.Model.Coulor
import de.htwg.Uno.modell.Model.Symbol
import de.htwg.Uno.modell.Model.Player
import de.htwg.Uno.modell.Model.Game
import de.htwg.Uno.aView.Tui
import de.htwg.Uno.controller.Controler




class UnoSpec extends AnyWordSpec with Matchers{

  val controller = new Controler()

  val TuiInstance = new Tui(controller: Controler)

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

  "The creator function" should {
    "render a red One card correctly" in {
      val card = Card(Coulor.red, Symbol.One)
      val lines = TuiInstance.creator(card)
      lines.head shouldBe "+------+"
      lines(1) should include ("r")
      lines(2) should include ("1")
      lines.last shouldBe "+------+"
    }

    "render a green Plus_2 card correctly" in {
      val card = Card(Coulor.green, Symbol.Plus_2)
      val lines = TuiInstance.creator(card)
      lines.mkString("\n") should include ("+2")
    }
  }

  "The handrenderer" should {
    "return 'UnoUno' when the hand is empty" in {
      TuiInstance.handrenderer(Nil) shouldBe "UnoUno"
    }

    "render multiple cards correctly" in {
      val hand = List(
        Card(Coulor.red, Symbol.One),
        Card(Coulor.blue, Symbol.Two),
        Card(Coulor.yellow, Symbol.Six),
        Card(Coulor.yellow, Symbol.Nine),
        Card(Coulor.yellow, Symbol.Plus_4),
        Card(Coulor.yellow, Symbol.Block),
        Card(Coulor.yellow, Symbol.Wish),
        Card(Coulor.yellow, Symbol.Three),
        Card(Coulor.yellow, Symbol.Four),
        Card(Coulor.yellow, Symbol.Eight),
        Card(Coulor.yellow, Symbol.Zero),
        Card(Coulor.yellow, Symbol.Seven),
        Card(Coulor.yellow, Symbol.Reverse),
        Card(Coulor.yellow, Symbol.Five)
        

      )
      val output = TuiInstance.handrenderer(hand)
      output should include ("r")
      output should include ("b")
      output should include ("1")
      output should include ("2")
    }
  }



  "The tablerenderer function" should {
    "render a card as a multi-line string" in {
      val card = Card(Coulor.red, Symbol.One)
      val result = TuiInstance.tablerenderer(card)

      result should include ("+")
      result should include ("r")
      result should include ("1")
      result.linesIterator.size should be > 1
    }
  }

  "The gamerenderer function" should {
    "render the table and player hands" in {
      val p1 = Player("Alice", List(Card(Coulor.green, Symbol.Five)), 0)
      val p2 = Player("Bob", List(Card(Coulor.blue, Symbol.Two)), 1)
      val game = Game(List(p1, p2), Nil, Card(Coulor.red, Symbol.One))

      val result = TuiInstance.gamerenderer(game)

      result should include ("Table:")
      result should include ("Alice")
      result should include ("Bob")
      result should include ("r")
      result should include ("b")
    }
  }

  "The fake function" should {
    "print the given string and return 's'" in {
      val printed = new java.io.ByteArrayOutputStream()
      Console.withOut(printed) {
        val result = TuiInstance.fake("Hello Test")
        result shouldBe "s"
      }
      printed.toString should include ("Hello Test")
    }
  }

  
}

