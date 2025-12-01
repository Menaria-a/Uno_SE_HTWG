package de.htwg.Uno.aView.handler
import de.htwg.Uno.controller.*
import de.htwg.Uno.model.Game


trait InputHandler {
    var next: Option[InputHandler] = None

    def handleRequest(input: String, game: Game): (PlayerAction, Integer)


    def setNext(handler: InputHandler): InputHandler = {
        next = Some(handler)
        handler
    }


    protected def nextHandler(input: String, game: Game): (PlayerAction, Integer) =
        next.map(_.handleRequest(input, game)).getOrElse((InvalidAction,0))
}