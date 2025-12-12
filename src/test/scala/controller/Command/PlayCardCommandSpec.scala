package de.htwg.Uno.controller.Command

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.*
import de.htwg.Uno.controller.Command.PlayCardCommand

class PlayCardCommandSpec extends AnyWordSpec with Matchers:

  val cardToPlay = Card(Coulor.red, Symbol.One)
  val remainingCard = Card(Coulor.blue, Symbol.Four)
  val bobCard = Card(Coulor.green, Symbol.Two)

  val player1 = Player("Alice", List(cardToPlay, remainingCard), 0)
  val player2 = Player("Bob", List(bobCard), 0)

  val initialGame = Game(
    player = List(player1, player2),
    index = 0,
    deck = Nil,
    table = Card(Coulor.red, Symbol.Three),
    ActionState = ActionState.ChooseCard,
    TurnState = TurnState.PlayerTurn(player1)
  )

  "A PlayCardCommand" should:

    "play a card and update the game correctly" in:
      val command = PlayCardCommand(0, 0, 0)
      val (newGame, value) = command.execute(initialGame)

      value shouldBe 0
      newGame.player(0).hand.size shouldBe 1
      newGame.table shouldBe cardToPlay

      newGame.TurnState match
        case TurnState.PlayerTurn(p) => p.name shouldBe "Bob"
        case other => fail(s"TurnState falsch: $other")

      newGame.ActionState shouldBe ActionState.ChooseCard

    "not modify unrelated parts of the game" in:
      val command = PlayCardCommand(0, 0, 0)
      val (newGame, _) = command.execute(initialGame)

      newGame.player.map(_.name) shouldEqual initialGame.player.map(_.name)
      newGame.deck shouldBe initialGame.deck
