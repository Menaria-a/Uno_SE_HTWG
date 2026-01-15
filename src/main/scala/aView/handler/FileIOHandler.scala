package de.htwg.Uno.aView.handler

import de.htwg.Uno.controller.*
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Enum.ActionState

class IOHandler(val next: Option[InputHandler] = None) extends InputHandler:

    override def handleRequest(input: String, game: Game): (PlayerAction, Integer) =
        if ( input == "load")
            (IOAction,40)
        else if (input == "save")
            (IOAction, 50)
        else
            (nextHandler(input, game))


    override def setNext(handler: InputHandler): InputHandler =
        new IOHandler(Some(handler))