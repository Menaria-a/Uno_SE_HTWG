package de.htwg.Uno.aView.handler

import de.htwg.Uno.controller.*
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.GameExtention.*
import de.htwg.Uno.model.Enum.ActionState
import scala.util.Try
import scala.util.Success
import scala.util.Failure

class ChooseCardHandler(val next: Option[InputHandler] = None) extends InputHandler:

    override def handleRequest(input: String, game: Game): (PlayerAction, Integer) =
        if game.ActionState != ActionState.ChooseCard then
            nextHandler(input, game)
        else
            Try(input.toInt) match
                case Success(index) =>
                    val player = game.currentPlayer
                    val hand   = player.hand

                    hand.lift(index) match
                        case Some(card) =>
                            (PlayCardAction(card), index)
                        case None =>
                            (InvalidAction, 0)

                case Failure(_)=>
                    nextHandler(input, game)

    override def setNext(handler: InputHandler): InputHandler =
        new ChooseCardHandler(Some(handler))
