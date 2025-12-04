package de.htwg.Uno.controller.Command

import de.htwg.Uno.model.Game

trait Command {
    def execute(game: Game): (Game, Integer)
    def undo(currentGame: Game ,previousGame: Game): Game = previousGame
}