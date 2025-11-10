
package de.htwg.Uno

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers._
import de.htwg.Uno.aView.Tui
import de.htwg.Uno.modell.Model.Card
import de.htwg.Uno.modell.Model.Coulor
import de.htwg.Uno.modell.Model.Symbol
import de.htwg.Uno.modell.Model.Player
import de.htwg.Uno.modell.Model.Game
import de.htwg.Uno.aView.Tui




class UnoSpec extends AnyWordSpec with Matchers{

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
      val lines = creator(card)
      lines.head shouldBe "+------+"
      lines(1) should include ("r")
      lines(2) should include ("1")
      lines.last shouldBe "+------+"
    }

    "render a green Plus_2 card correctly" in {
      val card = Card(Coulor.green, Symbol.Plus_2)
      val lines = creator(card)
      lines.mkString("\n") should include ("+2")
    }
  }

  "The handrenderer" should {
    "return 'UnoUno' when the hand is empty" in {
      handrenderer(Nil) shouldBe "UnoUno"
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
      val output = handrenderer(hand)
      output should include ("r")
      output should include ("b")
      output should include ("1")
      output should include ("2")
    }
  }

//  "The tablerenderer" should {
//    "return 'hilfe' when the table is empty" in {
//      tablerenderer(Nil) shouldBe "hilfe"
//    }

//    "render cards correctly on the table" in {
//      val table = List(Card(Coulor.yellow, Symbol.Reverse))
//      val output = tablerenderer(table)
//      output should include ("↺")
//    }
//  }

//  "The gamerenderer" should {
//    "render a game with two players and a table" in {
//      val players = List(
//        Player("Melissa"),
//        Player("Joud")
//      )
//      val table = List(Card(Coulor.green, Symbol.Five))
//      val game = Game(players, Nil, table)
//      val output = gamerenderer(game)

//      output should include ("Melissa")
//      output should include ("Joud")
//      output should include ("Table:")
//      output should include ("g")
//      output should include ("5")
//    }
//  }
}
