
package de.htwg.uno

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import Uno._

class UnoSpec extends AnyWordSpec with Matchers{

  "A Card" should {
    "be created correctly with a color and symbol" in {
      val card = Card(Coulor.red, Symbol.One)
      card.colour shouldBe Coulor.red
      card.symbol shouldBe Symbol.One
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
        Card(Coulor.blue, Symbol.Two)
      )
      val output = handrenderer(hand)
      output should include ("r")
      output should include ("b")
      output should include ("1")
      output should include ("2")
    }
  }

  "The tablerenderer" should {
    "return 'hilfe' when the table is empty" in {
      tablerenderer(Nil) shouldBe "hilfe"
    }

    "render cards correctly on the table" in {
      val table = List(Card(Coulor.yellow, Symbol.Reverse))
      val output = tablerenderer(table)
      output should include ("↺")
    }
  }

  "The gamerenderer" should {
    "render a game with two players and a table" in {
      val players = List(
        Player("Melissa", List(Card(Coulor.red, Symbol.One))),
        Player("Joud", List(Card(Coulor.blue, Symbol.Two)))
      )
      val table = List(Card(Coulor.green, Symbol.Five))
      val game = Game(players, Nil, table)
      val output = gamerenderer(game)

      output should include ("Melissa")
      output should include ("Joud")
      output should include ("Table:")
      output should include ("g")
      output should include ("5")
    }
  }
}
