package de.htwg.Uno.aView.handler

import de.htwg.Uno.controller.*
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Enum.ActionState
import de.htwg.Uno.model.Model.Coulor

class ChooseColourHandler(val next: Option[InputHandler] = None)
    extends InputHandler:

  override def handleRequest(
      input: String,
      game: Game
  ): (PlayerAction, Integer) =
    if game.ActionState != ActionState.ChooseColour then
      (nextHandler(input, game))
    else
      input match
        case "r" => (PlayCardAction(0, 0, 0), 2)
        case "g" => (PlayCardAction(0, 0, 0), 3)
        case "b" => (PlayCardAction(0, 0, 0), 4)
        case "y" => (PlayCardAction(0, 0, 0), 1)
        case _   => (InvalidAction, 0)

  override def setNext(handler: InputHandler): InputHandler =
    new ChooseColourHandler(Some(handler))
