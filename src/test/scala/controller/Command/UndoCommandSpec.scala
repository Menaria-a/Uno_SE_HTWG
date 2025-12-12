package de.htwg.Uno.controller.Command


import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.*
import de.htwg.Uno.controller.Command.UndoCommand

class UndoCommandSpec extends AnyWordSpec with Matchers:

  object DummyUndo extends UndoCommand:
    override def execute(game: Game): Game =
      game.copy(index = game.index)

  val game1 = Game(
    player = List(Player("Alice", Nil, 0)),
    index = 0,
    deck = Nil,
    table = Card(Coulor.red, Symbol.One),
    ActionState = ActionState.None,
    TurnState = TurnState.None
  )

  val game2 = game1.copy(index = 5)

  "An UndoCommand" should:

    "return the previous game when undo() is called" in:
      val result = DummyUndo.undo(game2, game1)
      result shouldBe game1

    "not modify the previous game when undo() is called" in:
      val result = DummyUndo.undo(game2, game1)
      result.index shouldBe 0

    "modify the game when execute() is called" in:
      val result = DummyUndo.execute(game1)
      result.index shouldBe 0