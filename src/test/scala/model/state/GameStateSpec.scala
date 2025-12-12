

package de.htwg.Uno.model.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.Uno.model.*
import de.htwg.Uno.model.Model.*
import de.htwg.Uno.controller.PlayerAction

class GameStateSpec extends AnyWordSpec with Matchers {

  // Dummy GameState Implementation for testing
  class DummyGameState extends GameState {
    override def start(p1: Player, p2: Player): Game =
      Game(List(p1, p2), 0, Nil, null, null, null)

    override def playCard(game: Game, playerIdx: Int, cardIdx: Int): (Game, Integer) =
      (game.copy(index = cardIdx), cardIdx)

    override def chooseColour(game: Game, colour: Model.Coulor, hand: Card, input: Integer): (Card, Game) =
      (hand.copy(colour = colour), game)
  }

  "A GameState" should {

    "start a game with two players" in {
      val p1 = Player("Alice", Nil,0)
      val p2 = Player("Bob", Nil,1)
      val state = new DummyGameState

      val game = state.start(p1, p2)
      game.player.size shouldBe 2
      game.player.head.name shouldBe "Alice"
      game.player(1).name shouldBe "Bob"
    }

    "draw a card and return same game by default" in {
      val state = new DummyGameState
      val game = Game(Nil, 0, Nil, null, null, null)

      val newGame = state.drawCard(game, 0)
      newGame shouldBe game
    }

    "play a card and return new game and index" in {
      val state = new DummyGameState
      val game = Game(Nil, 0, Nil, null, null, null)

      val (newGame, value) = state.playCard(game, 0, 5)
      newGame.index shouldBe 5
      value shouldBe 5
    }

    "choose a colour and return card with new colour" in {
      val state = new DummyGameState
      val game = Game(Nil, 0, Nil, null, null, null)
      val card = Card(Coulor.red, Symbol.One)

      val (newCard, newGame) = state.chooseColour(game, Coulor.blue, card, 0)
      newCard.colour shouldBe Coulor.blue
      newGame shouldBe game
    }
  }
}
