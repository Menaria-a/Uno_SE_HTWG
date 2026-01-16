package de.htwg.Uno.controller.Command

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.*

class UndoCommandSpec extends AnyWordSpec with Matchers {

  val game1: Game = Game(
    player = List(Player("Alice", Nil, 0)),
    index = 0,
    deck = Nil,
    table = Some(Card(Coulor.red, Symbol.One)),
    ActionState = ActionState.None,
    TurnState = TurnState.None
  )

  val game2: Game = game1.copy(index = 5)

  // ⬅ UndoCommand REQUIRES a Game
  val undoCommand = UndoCommand(game1)

  "An UndoCommand" should {

    "return the previous game when undo is called" in {
      val result = undoCommand.undo(game2, game1)
      result shouldBe game1
    }

    "not modify the previous game when undo is called" in {
      val result = undoCommand.undo(game2, game1)
      result.index shouldBe 0
    }

    "not change the game when execute is called" in {
      val result = undoCommand.execute(game1)
      result shouldBe (game1, 0)
    }
  }
}
