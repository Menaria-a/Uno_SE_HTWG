package de.htwg.Uno.model.state.Impl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.*

class DrawCardStateSpec extends AnyWordSpec with Matchers {

  // ---------- Use GameState type to access all methods ----------
  val state: de.htwg.Uno.model.state.GameState = DrawCardStateImpl

  // Dummy game and players for testing
  val player1 = Player("Alice", Nil, 0)
  val player2 = Player("Bob", Nil, 0)
  val deck = List(Card(Coulor.red, Symbol.One), Card(Coulor.blue, Symbol.Two))
  val tableCard = Card(Coulor.green, Symbol.Three)
  val game = Game(
    player = List(player1, player2),
    index = 0,
    deck = deck,
    table = Some(tableCard),
    ActionState = ActionState.None,
    TurnState = TurnState.None
  )

  "DrawCardStateImpl" should {

    "draw a card and update the player's hand, deck, and index" in {
      val newGame = state.drawCard(game, 0)
      newGame.player.head.hand.size shouldBe 1
      newGame.deck.size shouldBe 1
      newGame.index shouldBe 1
    }

    "dealCardsToHand adds n cards to player's hand and removes them from deck" in {
      val (newPlayer, newDeck) = state.dealCardsToHand(player1, deck, 2)
      newPlayer.hand shouldBe deck
      newDeck shouldBe Nil
    }

    "nextPlayerIndex increments correctly with and without skip" in {
      state.nextPlayerIndex(0, 2, skipNext = false) shouldBe 1
      state.nextPlayerIndex(1, 2, skipNext = false) shouldBe 0
      state.nextPlayerIndex(0, 2, skipNext = true) shouldBe 0
      state.nextPlayerIndex(1, 2, skipNext = true) shouldBe 1
    }

    "start returns a new Game object" in {
      val startedGame = state.start(player1, player2, null)
      startedGame shouldBe a[Game]
      startedGame.table should not be None
    }

    "chooseColour returns a card and a game" in {
      val (card, newGame) = state.chooseColour(game, Coulor.red, Card(Coulor.yellow, Symbol.One), 2)
      card shouldBe a[Card]
      newGame shouldBe a[Game]
    }

    "wisher returns a Coulor" in {
      state.wisher(0) shouldBe Coulor.blue
    }

    "playCard returns a tuple (Game, Integer)" in {
      val (newGame, code) = state.playCard(game, 0, 0)
      newGame shouldBe a[Game]
      code shouldBe 2
    }

    "parseCardIndex returns correct tuple" in {
      val (newGame, idx) = state.parseCardIndex(0, player1, game, tableCard, 0)
      newGame shouldBe game
      idx shouldBe 0
    }

    "turn returns correct tuple" in {
      val (newGame, idx) = state.turn(tableCard, game, 0)
      newGame shouldBe game
      idx shouldBe 0
    }

    "isPlayable always returns true" in {
      state.isPlayable(tableCard, Card(Coulor.red, Symbol.One)) shouldBe true
    }

    "handleTurn returns correct tuple" in {
      val (newGame, idx) = state.handleTurn(game, 0, 1)
      newGame shouldBe game
      idx shouldBe 1
    }

    "handleInvalidInput returns correct tuple" in {
      val (newGame, code) = state.handleInvalidInput(game, tableCard, ActionState.None)
      newGame shouldBe game
      code shouldBe 1
    }

    "plusN returns the game unchanged" in {
      val newGame = state.plusN(game, 0, Card(Coulor.red, Symbol.One), 2)
      newGame shouldBe game
    }

    "playCardIfValid returns correct tuple" in {
      val (newGame, idx) = state.playCardIfValid(Card(Coulor.red, Symbol.One), game, tableCard, 0)
      newGame shouldBe game
      idx shouldBe 0
    }
  }
}


