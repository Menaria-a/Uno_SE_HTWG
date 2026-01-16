package de.htwg.Uno.controller.Command

import de.htwg.Uno.model.Game

case class UndoCommand(game: Game) extends UndoCommandT {
  override def execute(game: Game): (Game, Integer) =
    (game, 0)
  override def undo(
      currentGame: Game,
      previousGame: Game
  ): Game = previousGame
}
