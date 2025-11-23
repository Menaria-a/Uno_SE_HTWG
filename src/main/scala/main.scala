
package de.htwg.Uno
import de.htwg.Uno.aView.Tui
import de.htwg.Uno.controller.Controller

object main:

    def main (args: Array[String]): Unit =
        val controller = Controller()
        val tui = new Tui(controller)
        controller.add(tui) 

        
    
        tui.fake("Ersten und Zweiten Namen eingeben: ")
        val startGame = controller.initGame(tui)
        controller.gameLoop(startGame,startGame.table,0,tui) 