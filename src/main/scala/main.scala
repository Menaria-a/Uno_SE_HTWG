
package de.htwg.Uno
import de.htwg.Uno.aView.Tui
import de.htwg.Uno.controller.Controller
import de.htwg.Uno.model.Model.{Game, Card, Coulor, Symbol}
import de.htwg.Uno.model.Enum.ActionState
import de.htwg.Uno.model.Enum.TurnState


object main:

    def main (args: Array[String]): Unit =
        val controller = Controller( game = Game(Nil, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None))
        val tui = new Tui(controller)
        controller.add(tui) 

        
    
        tui.fake("Ersten und Zweiten Namen eingeben: ")
        val startGame = controller.initGame(tui)
        controller.gameLoop(startGame,startGame.table,0,tui) 