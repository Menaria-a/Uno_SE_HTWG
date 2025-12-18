
package de.htwg.Uno
import de.htwg.Uno.aView.Tui
import de.htwg.Uno.aView.Gui.Gui
import de.htwg.Uno.controller.ControllerInterface.*
import de.htwg.Uno.model.ModelInterface.*
import de.htwg.Uno.util.Undo.CommandManager
import scalafx.application.Platform


object main:

    def main (args: Array[String]): Unit =
        val manager = CommandManager()
        val controller = (Controller( game = Game(Nil,0, Nil,Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None), manager))
        val tui = new Tui(controller)
        controller.add(tui) 

        Platform.startup(()=> {
            val gui = new Gui(controller)
            gui.start()

        }
        
        )
        //controller.initloop(tui)
        //controller.gameloop(tui)



