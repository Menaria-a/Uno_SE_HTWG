
package de.htwg.Uno
import de.htwg.Uno.aView.Tui
import de.htwg.Uno.controller.Controler




object main:

    //def main(args: Array[String]): Unit =

        //val controller = Controler()
        //val tui = new Tui(controller)
        //controller.add(tui)

    
        //7val startGame = controller.initGame()
        //controller.gameLoop(startGame, startGame.table, 0)

    def main (args: Array[String]): Unit =
        val controller = Controler()
        val tui = new Tui(controller)
        controller.add(tui) 

        
    
        tui.fake("Ersten und Zweiten Namen eingeben: ")
        val startGame = controller.initGame(tui)
        controller.gameLoop(startGame,startGame.table,0,tui) 