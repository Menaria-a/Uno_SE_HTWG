
package de.htwg.Uno
import de.htwg.Uno.aView.Tui
import de.htwg.Uno.aView.Gui.Gui
import de.htwg.Uno.controller.Controller
import de.htwg.Uno.model.Model.{Coulor, Symbol}
import de.htwg.Uno.model.Player
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Enum.ActionState
import de.htwg.Uno.model.Enum.TurnState
import de.htwg.Uno.util.Undo.CommandManager
import scalafx.application.Platform


object main:

    def main (args: Array[String]): Unit =
        val manager = CommandManager()
        val controller = (Controller( game = Game(Nil,0, Nil,Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)))
        val tui = new Tui(controller)
        controller.add(tui) 

        //tui.fake("Ersten und Zweiten Namen eingeben: ")
        //controller.initloop(tui)
        //controller.gameloop(tui)


        Platform.startup(()=> {
            val gui = new Gui(controller)
            gui.start()

        })



