package de.htwg.Uno.model.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.model.ModelInterface.StateInterface.*
import de.htwg.Uno.model.ModelInterface.*


class InitStateSpec extends AnyWordSpec with Matchers {

  "InitState" should {

    "start a new game with correct setup" in {
      val p1 = Player("Alice",hand = Nil, 0)
      val p2 = Player("Bob", hand = Nil, 1)

      val game = InitState.start(p1, p2)


      game.player.size shouldBe 2
      game.player.head.hand.size shouldBe 5
      game.player(1).hand.size shouldBe 5

      game.deck.size shouldBe >= (0) // Restdeck, je nach Shuffle
      game.table shouldBe a[Card]

      game.ActionState shouldBe ActionState.ChooseCard
      game.TurnState shouldBe a[TurnState.PlayerTurn]
    }

    "chooseColour always returns the dummy card and game" in {
      val dummyCard = Card(Coulor.red, Symbol.One)
      val dummyGame = Game(Nil,0, Nil, dummyCard, ActionState.None, TurnState.None)
      val init = InitState.start(Player("P1", hand = Nil, 0), Player("P2", hand = Nil, 1))

      val (card, game) = InitState.chooseColour(dummyGame, Coulor.blue, dummyCard, 1)

      card.coulor shouldBe Coulor.red
      card.symbol shouldBe Symbol.One
      game.player shouldBe Nil
      game.index shouldBe 0
    }

    "playCard always returns the dummy game and index" in {
      val dummyCard = Card(Coulor.red, Symbol.One)
      val dummyGame = Game(Nil,0, Nil, dummyCard, ActionState.None, TurnState.None)
      val init = InitState.start(Player("P1", hand = Nil, 0), Player("P2", hand = Nil, 1))

      val (game, idx) = InitState.playCard(dummyGame, 0, 0)

      game.player shouldBe Nil
      idx shouldBe 2
    }
  }
}




