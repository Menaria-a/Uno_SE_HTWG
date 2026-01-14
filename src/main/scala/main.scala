
package de.htwg.Uno
import de.htwg.Uno.aView.Tui
import de.htwg.Uno.aView.Gui.Gui
import de.htwg.Uno.controller.*
import de.htwg.Uno.model.*
import de.htwg.Uno.model.Model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.util.Undo.CommandManager
import scalafx.application.Platform
import de.htwg.Uno.model.state.GameStates
import com.google.inject.Guice


object main:

    def main (args: Array[String]): Unit =
        val manager = CommandManager()
        val injector = Guice.createInjector(new Module)
        val con = injector.getInstance(classOf[Controller])
        val tui = new Tui(con)
        con.add(tui) 

        Platform.startup(()=> {
            val gui = injector.getInstance(classOf[Gui])
            gui.start()
        }
        
        )
        
        tui.fake("Ersten und Zweiten Namen eingeben: ")
        con.initloop(tui)
        con.gameloop(tui)



