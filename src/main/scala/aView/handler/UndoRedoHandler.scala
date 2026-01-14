package de.htwg.Uno.aView.handler

import de.htwg.Uno.controller.*
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Enum.ActionState

class UndoRedoHandler(val next: Option[InputHandler] = None) extends InputHandler:

    override def handleRequest(input: String, game: Game): (PlayerAction, Integer) =
        if ( input == "undo")
            (UndoAction(game),20)
        else if (input == "redo")
            (RedoAction, 30)
        else
            (nextHandler(input, game))


    override def setNext(handler: InputHandler): InputHandler =
        new UndoRedoHandler(Some(handler))
