package de.htwg.Uno.model.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.model.Model._
import de.htwg.Uno.model.Enum._
import de.htwg.Uno.model.Player
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.Game

class DrawCardStateSpec extends AnyWordSpec with Matchers {

  "DrawCardState" should {

    "draw a card and update the player hand and deck" in {
      val player1 = Player("Alice", hand = List.empty,0)
      val player2 = Player("Bob", hand = List.empty,1)
      val deck = List(Card(Coulor.red, Symbol.One), Card(Coulor.blue, Symbol.Two))
      val game = Game(List(player1, player2), index = 0, deck = deck, table = deck.head, ActionState.None, TurnState.None)

      val newGame = DrawCardState.drawCard(game, playerIdx = 0)

      // Spieler 1 bekommt eine Karte
      newGame.player(0).hand.size shouldBe 1
      newGame.player(0).hand.head shouldBe deck.head

      // Deck wird um die gezogene Karte verkleinert
      newGame.deck shouldBe List(deck(1))

      // Index wechselt zum nächsten Spieler
      newGame.index shouldBe 1
    }

    "draw a card with skipNext = true" in {
      val player1 = Player("Alice", hand = List.empty,0)
      val player2 = Player("Bob", hand = List.empty,1)
      val deck = List(Card(Coulor.red, Symbol.One))
      val game = Game(List(player1, player2), index = 0, deck = deck, table = deck.head, ActionState.None, TurnState.None)

      val nextIndex = DrawCardState.nextPlayerIndex(currentIndex = 0, playerCount = 2, skipNext = true)
      nextIndex shouldBe 0  // (0 + 2) % 2 == 0
    }

    "dealCardsToHand correctly deals n cards and updates deck" in {
      val player = Player("Alice", hand = List(Card(Coulor.red, Symbol.One)),0)
      val deck = List(Card(Coulor.green, Symbol.Two), Card(Coulor.blue, Symbol.Three))

      val (newPlayer, newDeck) = DrawCardState.dealCardsToHand(player, deck, 2)

      newPlayer.hand.size shouldBe 3
      newPlayer.hand.last shouldBe Card(Coulor.blue, Symbol.Three)
      newDeck shouldBe empty
    }

    "start returns dummy game" in {
      val game = DrawCardState.start(Player("P1", hand = Nil, 0), Player("P2", hand = Nil, 1))
      game.player shouldBe Nil
      game.index shouldBe 0
    }

    "chooseColour returns dummy card and game" in {
      val card = Card(Coulor.red, Symbol.One)
      val dummyGame = Game(Nil,0, Nil, card, ActionState.None, TurnState.None)
      val (resultCard, resultGame) = DrawCardState.chooseColour(dummyGame, Coulor.blue, card, 1)

      resultCard shouldBe card
      resultGame.player shouldBe Nil
    }

    "playCard returns dummy game and index 2" in {
      val card = Card(Coulor.red, Symbol.One)
      val dummyGame = Game(Nil,0, Nil, card, ActionState.None, TurnState.None)
      val (resultGame, index) = DrawCardState.playCard(dummyGame, 0, 0)

      resultGame.player shouldBe Nil
      index shouldBe 2
    }
  }
}
