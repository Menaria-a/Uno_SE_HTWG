package de.htwg.Uno.model.state.Impl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.*

class PlayCardStateSpec extends AnyWordSpec with Matchers {

  val state: PlayCardStateImpl.type = PlayCardStateImpl

  // Dummy players
  val player1 = Player(
    "Alice",
    List(Card(Coulor.red, Symbol.One), Card(Coulor.green, Symbol.Plus_2)),
    0
  )
  val player2 = Player("Bob", List(Card(Coulor.blue, Symbol.Two)), 1)

  val tableCard = Card(Coulor.red, Symbol.One)

  val game = Game(
    player = List(player1, player2),
    index = 0,
    deck =
      List(Card(Coulor.yellow, Symbol.Three), Card(Coulor.blue, Symbol.Four)),
    table = Some(tableCard),
    ActionState = ActionState.None,
    TurnState = TurnState.None
  )

  "PlayCardStateImpl" should {

    "playCard delegates to handleTurn" in {
      val (newGame, code) = state.playCard(game, 0, 0)
      newGame shouldBe a[Game]
      code shouldBe 0
    }

    "handleTurn returns OutOfRange for invalid index" in {
      val (_, code) = state.handleTurn(game, 0, 500)
      code shouldBe 3
    }

    "parseCardIndex calls handleInvalidInput for invalid index" in {
      val (newGame, code) =
        state.parseCardIndex(-1, player1, game, tableCard, 0)
      newGame.ActionState shouldBe ActionState.OutOfRange
      code shouldBe 0
    }

    "playCardIfValid returns correct tuple for playable card" in {
      val card = player1.hand.head
      val (newGame, code) = state.playCardIfValid(card, game, tableCard, 0)
      newGame shouldBe a[Game]
    }

    "isPlayable returns true for matching color, symbol, Wish, or Plus_4" in {
      val card1 = Card(Coulor.red, Symbol.Two)
      val card2 = Card(Coulor.green, Symbol.One)
      val card3 = Card(Coulor.yellow, Symbol.Wish)
      val card4 = Card(Coulor.blue, Symbol.Plus_4)
      state.isPlayable(tableCard, card1) shouldBe true
      state.isPlayable(tableCard, card2) shouldBe true
      state.isPlayable(tableCard, card3) shouldBe true
      state.isPlayable(tableCard, card4) shouldBe true
    }

    "turn updates hand and table correctly" in {
      val card = player1.hand.head
      val (newGame, code) = state.turn(card, game, 0)
      newGame.player(0).hand.size shouldBe player1.hand.size - 1
      newGame.table shouldBe Some(card)
    }

    "nextPlayerIndex calculates correctly" in {
      state.nextPlayerIndex(0, 2, false) shouldBe 1
      state.nextPlayerIndex(0, 2, true) shouldBe 0
    }

    "plusN deals correct number of cards" in {
      val newGame = state.plusN(game, 1, Card(Coulor.red, Symbol.One), 1)
      newGame.player(1).hand.size shouldBe player2.hand.size + 1
    }

    "dealCardsToHand adds cards to player's hand" in {
      val (newPlayer, newDeck) = state.dealCardsToHand(player1, game.deck, 1)
      newPlayer.hand.size shouldBe player1.hand.size + 1
      newDeck.size shouldBe game.deck.size - 1
    }

    "handleInvalidInput sets correct ActionState" in {
      val (newGame, code) =
        state.handleInvalidInput(game, tableCard, ActionState.CardNotPlayable)
      newGame.ActionState shouldBe ActionState.CardNotPlayable
      code shouldBe 0
    }

    "start returns a basic game" in {
      val newGame = state.start(player1, player2, null)
      newGame shouldBe a[Game]
    }

    "chooseColour returns a dummy card and game" in {
      val (card, newGame) =
        state.chooseColour(game, Coulor.red, player1.hand.head, 1)
      card shouldBe a[Card]
      newGame shouldBe a[Game]
    }

    "wisher always returns blue" in {
      state.wisher(0) shouldBe Coulor.blue
    }
  }

  "turn" should {

    "declare a winner when player has no more cards" in {
      val oneCardPlayer =
        Player("Winner", List(Card(Coulor.red, Symbol.One)), 0)
      val otherPlayer = Player("Bob", List(Card(Coulor.blue, Symbol.Two)), 1)
      val g = game.copy(player = List(oneCardPlayer, otherPlayer))

      val cardToPlay = oneCardPlayer.hand.head
      val (newGame, code) = state.turn(cardToPlay, g, 0)

      newGame.TurnState shouldBe TurnState.GameWon(
        oneCardPlayer.copy(hand = List())
      )
      code shouldBe 5
    }

    "handle Plus_2 card correctly" in {
      val plus2Card = Card(Coulor.red, Symbol.Plus_2)
      val g = game.copy(player = List(player1, player2))

      val (newGame, code) = state.turn(plus2Card, g, 0)
      code shouldBe 6
      newGame.player(1).hand.size shouldBe player2.hand.size + 2
    }

    "handle Plus_4 card correctly" in {
      val plus4Card = Card(Coulor.red, Symbol.Plus_4)
      val g = game.copy(player = List(player1, player2))

      val (newGame, code) = state.turn(plus4Card, g, 0)
      code shouldBe 1
      newGame.player(1).hand.size shouldBe player2.hand.size + 2
    }

    "handle Block card correctly" in {
      val blockCard = Card(Coulor.red, Symbol.Block)
      val g = game.copy(player = List(player1, player2))

      val (newGame, code) = state.turn(blockCard, g, 0)
      code shouldBe 6
    }

    "handle Reverse card correctly" in {
      val reverseCard = Card(Coulor.red, Symbol.Reverse)
      val g = game.copy(player = List(player1, player2))

      val (newGame, code) = state.turn(reverseCard, g, 0)
      code shouldBe 6
    }

    "handle Wish card correctly" in {
      val wishCard = Card(Coulor.red, Symbol.Wish)
      val g = game.copy(player = List(player1, player2))

      val (newGame, code) = state.turn(wishCard, g, 0)
      code shouldBe 1
    }

    "handle normal card correctly" in {
      val normalCard = Card(Coulor.red, Symbol.Two)
      val g = game.copy(player = List(player1, player2))

      val (newGame, code) = state.turn(normalCard, g, 0)
      code shouldBe 0
      newGame.table shouldBe Some(normalCard)
    }

    "draw correct" in {
      val (newgame) = state.drawCard(game, 0)
      newgame shouldBe game
    }
  }

}
