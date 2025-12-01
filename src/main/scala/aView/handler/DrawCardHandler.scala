package de.htwg.Uno.aView.handler

import de.htwg.Uno.controller.*
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Enum.ActionState

class DrawCardHandler(val next: Option[InputHandler] = None) extends InputHandler:

    override def handleRequest(input: String, game: Game): (PlayerAction, Integer) =
        if game.ActionState == ActionState.ChooseCard && input.trim.isEmpty then
            (DrawAction, 500)
        else
            (nextHandler(input, game))


    override def setNext(handler: InputHandler): InputHandler =
        new DrawCardHandler(Some(handler))


