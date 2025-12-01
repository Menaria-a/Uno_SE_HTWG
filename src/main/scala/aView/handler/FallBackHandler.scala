package de.htwg.Uno.aView.handler

import de.htwg.Uno.controller.*
import de.htwg.Uno.model.Game

class FallbackHandler extends InputHandler:

    override def handleRequest(input: String, game: Game): (PlayerAction, Integer) =
        (InvalidAction, 0)


