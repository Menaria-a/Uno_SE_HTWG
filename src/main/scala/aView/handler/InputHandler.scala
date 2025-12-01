package de.htwg.Uno.aView.handler
import de.htwg.Uno.controller.*
import de.htwg.Uno.model.Game


trait InputHandler {
    def next: Option[InputHandler]

    def handleRequest(input: String, game: Game): (PlayerAction, Integer)


    def setNext(handler: InputHandler): InputHandler 


    protected def nextHandler(input: String, game: Game): (PlayerAction, Integer) =
        next.map(_.handleRequest(input, game)).getOrElse((InvalidAction,0))
}




