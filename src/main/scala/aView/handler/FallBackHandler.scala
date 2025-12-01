package de.htwg.Uno.aView.handler

import de.htwg.Uno.controller.*
import de.htwg.Uno.model.Game

class FallbackHandler(val next: Option[InputHandler] = None) extends InputHandler:

    override def handleRequest(input: String, game: Game): (PlayerAction, Integer) =
        (InvalidAction, 0)


    override def setNext(handler: InputHandler): InputHandler =
    this // keine weitere Weitergabe nötig


