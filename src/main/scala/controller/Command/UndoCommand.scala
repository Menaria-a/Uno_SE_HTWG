package de.htwg.Uno.controller.Command

import de.htwg.Uno.model.Game

trait UndoCommand {
    def execute (game: Game): Game
    def undo(
        currentGame: Game,
        previousGame: Game
    ): Game = previousGame
}