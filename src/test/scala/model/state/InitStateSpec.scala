package de.htwg.Uno.model.state.Impl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.*

class InitStateSpec extends AnyWordSpec with Matchers {

  // ---------- Type as GameState to access all methods ----------
  val state: de.htwg.Uno.model.state.GameState = InitStateImpl

  // Dummy players
  val player1 = Player("Alice", Nil, 0)
  val player2 = Player("Bob", Nil, 0)

  // Dummy deck and table
  val deck = List(Card(Coulor.red, Symbol.One), Card(Coulor.blue, Symbol.Two))
  val tableCard = Card(Coulor.red, Symbol.One)

  val game = Game(
    player = List(player1, player2),
    index = 0,
    deck = deck,
    table = Some(tableCard),
    ActionState = ActionState.None,
    TurnState = TurnState.None
  )

  "InitStateImpl" should {

    "start creates a new game with hands, deck, and table" in {
      val newGame = state.start(player1, player2, null)
      newGame shouldBe a[Game]
      newGame.player.size shouldBe 2
      newGame.player.head.hand.size shouldBe 5
      newGame.player(1).hand.size shouldBe 5
      newGame.deck.size shouldBe 50 // 52 cards - 10 hands - 1 table = 41?
      newGame.table should not be None
      newGame.ActionState shouldBe ActionState.ChooseCard
    }

    "deckmaker returns a valid game object" in {
      val newGame = InitStateImpl.deckmaker(player1, player2, null)
      newGame shouldBe a[Game]
      newGame.player.size shouldBe 2
    }

    "chooseColour returns a tuple (Card, Game)" in {
      val (card, newGame) =
        state.chooseColour(game, Coulor.red, Card(Coulor.yellow, Symbol.One), 2)
      card shouldBe a[Card]
      newGame shouldBe a[Game]
    }

    "playCard returns a tuple (Game, Integer)" in {
      val (newGame, code) = state.playCard(game, 0, 0)
      newGame shouldBe a[Game]
      code shouldBe 2
    }

    "wisher returns a Coulor" in {
      state.wisher(0) shouldBe Coulor.blue
    }

    "dealCardsToHand returns player and deck unchanged" in {
      val (newPlayer, newDeck) = state.dealCardsToHand(player1, deck, 1)
      newPlayer shouldBe player1
      newDeck shouldBe deck
    }

    "nextPlayerIndex returns current index" in {
      state.nextPlayerIndex(0, 2, skipNext = false) shouldBe 0
      state.nextPlayerIndex(1, 2, skipNext = true) shouldBe 1
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
      val (newGame, code) =
        state.handleInvalidInput(game, tableCard, ActionState.None)
      newGame shouldBe game
      code shouldBe 1
    }

    "plusN returns game unchanged" in {
      val newGame = state.plusN(game, 0, Card(Coulor.red, Symbol.One), 2)
      newGame shouldBe game
    }

    "playCardIfValid returns game and currentPlayerIndex" in {
      val (newGame, idx) =
        state.playCardIfValid(Card(Coulor.red, Symbol.One), game, tableCard, 0)
      newGame shouldBe game
      idx shouldBe 0
    }

    "draw correct" in {
      val (newgame) = state.drawCard(game, 0)
      newgame shouldBe game
    }
  }
}
