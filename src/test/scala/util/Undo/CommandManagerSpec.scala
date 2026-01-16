package de.htwg.Uno.util.Undo

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.Uno.model.*
import de.htwg.Uno.controller.Command.Command
import de.htwg.Uno.util.Undo.CommandManager

class CommandManagerSpec extends AnyWordSpec with Matchers {

  "CommandManager" should {

    "execute a command and update game" in {
      val initialGame = Game(Nil, 0, Nil, null, null, null)
      val cmd = new Command {
        override def execute(game: Game): (Game, Integer) =
          (game.copy(index = 1), 42)
        override def undo(game: Game, oldGame: Game): Game = oldGame
      }

      val manager = CommandManager()
      val (newManager, newGame, value) =
        manager.executeCommand(cmd, initialGame)

      newGame.index shouldBe 1
      value shouldBe 42
      newManager.undoStack.head._1 shouldBe cmd
    }

    "undo a command" in {
      val initialGame = Game(Nil, 0, Nil, null, null, null)
      val cmd = new Command {
        override def execute(game: Game): (Game, Integer) =
          (game.copy(index = 1), 42)
        override def undo(game: Game, oldGame: Game): Game = oldGame
      }

      val manager = CommandManager()
      val (m1, g1, _) = manager.executeCommand(cmd, initialGame)
      val undoResult = m1.undo(g1)
      undoResult.isDefined shouldBe true

      val (m2, g2) = undoResult.get
      g2.index shouldBe 0
    }

    "redo a command" in {
      val initialGame = Game(Nil, 0, Nil, null, null, null)
      val cmd = new Command {
        override def execute(game: Game): (Game, Integer) =
          (game.copy(index = 1), 42)
        override def undo(game: Game, oldGame: Game): Game = oldGame
      }

      val manager = CommandManager()
      val (m1, g1, _) = manager.executeCommand(cmd, initialGame)
      val (m2, g2) = m1.undo(g1).get
      val redoResult = m2.redo(g2)
      redoResult.isDefined shouldBe true

      val (m3, g3) = redoResult.get
      g3.index shouldBe 1
    }

    "return None when undo is called with empty undoStack" in {
      val initialGame = Game(Nil, 0, Nil, null, null, null)
      val manager = CommandManager()

      val result = manager.undo(initialGame)

      result shouldBe None
    }

    "return None when redo is called with empty redoStack" in {
      val initialGame = Game(Nil, 0, Nil, null, null, null)
      val manager = CommandManager()

      val result = manager.redo(initialGame)

      result shouldBe None
    }

    "return None for redo directly after execute (no undo happened)" in {
      val initialGame = Game(Nil, 0, Nil, null, null, null)

      val cmd = new Command {
        override def execute(game: Game): (Game, Integer) =
          (game.copy(index = 1), 42)
        override def undo(game: Game, oldGame: Game): Game = oldGame
      }

      val manager = CommandManager()
      val (m1, g1, _) = manager.executeCommand(cmd, initialGame)

      val redoResult = m1.redo(g1)

      redoResult shouldBe None
    }

  }
}
