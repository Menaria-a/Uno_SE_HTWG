package de.htwg.Uno.model.state.Impl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.*

class WishCardStateSpec extends AnyWordSpec with Matchers {

  // ---------- Type as GameState to access all methods ----------
  val state: de.htwg.Uno.model.state.GameState = WishCardStateImpl

  // Dummy game and players
  val player1 = Player("Alice", Nil, 0)
  val player2 = Player("Bob", Nil, 0)
  val tableCard = Card(Coulor.red, Symbol.One)
  val deck = List(Card(Coulor.green, Symbol.Two))

  val game = Game(
    player = List(player1, player2),
    index = 0,
    deck = deck,
    table = Some(tableCard),
    ActionState = ActionState.None,
    TurnState = TurnState.None
  )

  "WishCardStateImpl" should {

    "chooseColour returns a card and updates the game table" in {
      val hand = Card(Coulor.yellow, Symbol.Five)
      val (card, newGame) = state.chooseColour(game, Coulor.red, hand, 2)

      card shouldBe a[Card]
      card.colour shouldBe Coulor.red  // because input 2 => red
      newGame.table shouldBe Some(card)
      newGame.ActionState shouldBe ActionState.ChooseColour
    }

    "chooseColourForCard helper updates table and action state" in {
      val hand = Card(Coulor.blue, Symbol.Seven)
      val (card, newGame) = WishCardStateImpl.chooseColourForCard(hand, game, 3)
      card.colour shouldBe Coulor.green  // input 3 => green
      newGame.table shouldBe Some(card)
      newGame.ActionState shouldBe ActionState.ChooseColour
    }

    "wisher returns the correct colour for input" in {
      WishCardStateImpl.wisher(1) shouldBe Coulor.yellow
      WishCardStateImpl.wisher(2) shouldBe Coulor.red
      WishCardStateImpl.wisher(3) shouldBe Coulor.green
      WishCardStateImpl.wisher(4) shouldBe Coulor.blue
    }

    "start returns a new Game object" in {
      val startedGame = state.start(player1, player2, null)
      startedGame shouldBe a[Game]
      startedGame.table should not be None
    }

    "playCard returns a tuple (Game, Integer)" in {
      val (newGame, code) = state.playCard(game, 0, 0)
      newGame shouldBe a[Game]
      code shouldBe 2
    }

    "dealCardsToHand returns player and deck unchanged" in {
      val (newPlayer, newDeck) = state.dealCardsToHand(player1, deck, 1)
      newPlayer shouldBe player1
      newDeck shouldBe deck
    }

    "nextPlayerIndex returns current index" in {
      state.nextPlayerIndex(0, 2, false) shouldBe 0
      state.nextPlayerIndex(1, 2, true) shouldBe 1
    }

    "parseCardIndex returns game and currentPlayerIndex" in {
      val (newGame, idx) = state.parseCardIndex(0, player1, game, tableCard, 0)
      newGame shouldBe game
      idx shouldBe 0
    }

    "turn returns game and currentPlayerIndex" in {
      val (newGame, idx) = state.turn(tableCard, game, 0)
      newGame shouldBe game
      idx shouldBe 0
    }

    "isPlayable always returns true" in {
      state.isPlayable(tableCard, Card(Coulor.red, Symbol.One)) shouldBe true
    }

    "handleTurn returns game and chosenCardIndex" in {
      val (newGame, idx) = state.handleTurn(game, 0, 1)
      newGame shouldBe game
      idx shouldBe 1
    }

    "handleInvalidInput returns game and code 1" in {
      val (newGame, code) = state.handleInvalidInput(game, tableCard, ActionState.None)
      newGame shouldBe game
      code shouldBe 1
    }

    "plusN returns game unchanged" in {
      val newGame = state.plusN(game, 0, Card(Coulor.red, Symbol.One), 2)
      newGame shouldBe game
    }

    "playCardIfValid returns game and currentPlayerIndex" in {
      val (newGame, idx) = state.playCardIfValid(Card(Coulor.red, Symbol.One), game, tableCard, 0)
      newGame shouldBe game
      idx shouldBe 0
    }

    "draw correct" in {
      val (newgame) = state.drawCard(game, 0 )
      newgame shouldBe game
    }
  }
}














