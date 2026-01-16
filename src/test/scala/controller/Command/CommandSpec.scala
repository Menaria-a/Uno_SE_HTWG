package de.htwg.Uno.controller.Command

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.Uno.model.Game

class CommandSpec extends AnyWordSpec with Matchers {

  "A Command" should {

    "execute and return new game and integer" in {
      val initialGame = Game(Nil, 0, Nil, null, null, null)
      val cmd = new Command {
        override def execute(game: Game): (Game, Integer) =
          (game.copy(index = 5), 100)
      }

      val (newGame, value) = cmd.execute(initialGame)
      newGame.index shouldBe 5
      value shouldBe 100
    }

    "undo and return previous game by default" in {
      val oldGame = Game(Nil, 0, Nil, null, null, null)
      val currentGame = Game(Nil, 10, Nil, null, null, null)
      val cmd = new Command {
        override def execute(game: Game): (Game, Integer) =
          (game.copy(index = 10), 42)
      }

      val revertedGame = cmd.undo(currentGame, oldGame)
      revertedGame shouldBe oldGame
    }

    "allow custom undo behavior if overridden" in {
      val oldGame = Game(Nil, 0, Nil, null, null, null)
      val currentGame = Game(Nil, 10, Nil, null, null, null)

      val cmd = new Command {
        override def execute(game: Game): (Game, Integer) =
          (game.copy(index = 10), 42)
        override def undo(currentGame: Game, previousGame: Game): Game =
          currentGame.copy(index = previousGame.index)
      }

      val revertedGame = cmd.undo(currentGame, oldGame)
      revertedGame.index shouldBe 0
    }
  }
}
