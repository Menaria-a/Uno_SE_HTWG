package de.htwg.Uno.controller.Command

import de.htwg.Uno.controller.PlayerAction
import de.htwg.Uno.model.*
import de.htwg.Uno.controller.PlayCardAction
import de.htwg.Uno.controller.Command.PlayCardCommand.*
import de.htwg.Uno.controller.DrawAction
import de.htwg.Uno.controller.ChooseColourAction
import de.htwg.Uno.controller.UndoAction
import de.htwg.Uno.model.Model.Coulor
import de.htwg.Uno.controller.PlayerInput





trait CommandFactory {
    def createCommand(
        action: PlayerAction,
        game: Game
    ) : Command = {
        action match {

            case PlayCardAction(playerIdx, cardIdx, input) => playCard(playerIdx, cardIdx, input)

            case DrawAction(playerIdx) => draw(playerIdx)

            case ChooseColourAction(hand, input) => chooseColour(hand, input)

            case UndoAction(game) => undo(game)


        }
    }


    def playCard(playerIdx: Int, cardIdx: Int, input: Int): PlayCardCommandT
    def draw(playerIdx: Int): DrawCardCommandT
    def chooseColour(hand: Card, input: PlayerInput): ChooseColourCommandT
    def undo(game: Game): UndoCommandT
}