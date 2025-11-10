
package de.htwg.Uno
import de.htwg.Uno.controller.game.initGame
import de.htwg.Uno.controller.game.gameLoop



    def main(args: Array[String]): Unit =
        val startGame = initGame()
        gameLoop(startGame, startGame.table, 0)