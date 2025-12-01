package de.htwg.Uno.model

object GameExtention:
    extension (game: Game)
        def currentPlayer: Player =
            game.player(game.index)




