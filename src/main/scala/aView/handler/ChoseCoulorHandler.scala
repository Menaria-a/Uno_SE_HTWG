package de.htwg.Uno.aView.handler

import de.htwg.Uno.controller.*
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Enum.ActionState
import de.htwg.Uno.model.Model.Coulor

class ChooseColourHandler(val next: Option[InputHandler] = None) extends InputHandler:

    override def handleRequest(input: String, game: Game): (PlayerAction, Integer) =
        if game.ActionState != ActionState.ChooseColour then
            (nextHandler(input, game))
        else
            input match
            case "r" => (ChooseColourAction (Coulor.red),2)    
            case "g" => (ChooseColourAction (Coulor.green),3)
            case "b" => (ChooseColourAction (Coulor.blue),4)
            case "y" => (ChooseColourAction (Coulor.yellow),1)
            case _   => (InvalidAction,0)

    override def setNext(handler: InputHandler): InputHandler =
        new ChooseColourHandler(Some(handler))
    


