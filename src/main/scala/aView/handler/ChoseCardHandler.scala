package de.htwg.Uno.aView.handler

import de.htwg.Uno.controller.*
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.GameExtention.*

import de.htwg.Uno.model.Enum.ActionState

class ChooseCardHandler(val next: Option[InputHandler] = None)
    extends InputHandler:

  override def handleRequest(
      input: String,
      game: Game
  ): (PlayerAction, Integer) =
    if game.ActionState != ActionState.ChooseCard then
      (nextHandler(input, game))
    else if input.matches("\\d+") then
      val index = input.toInt
      val player = game.currentPlayer
      val hand = player.hand

      if index >= 0 && index < player.hand.size then
        val card = player.hand(index)
        val Action = PlayCardAction(0, 0, 0)
        (Action, index)
      else (InvalidAction, 0)
    else

      (nextHandler(input, game)
    )

  override def setNext(handler: InputHandler): InputHandler =
    new ChooseCardHandler(Some(handler))
