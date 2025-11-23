package de.htwg.Uno.model
import de.htwg.Uno.model.Model.Player



object Enum:


    enum ActionState:
        case ChooseCard
        case DrawCard
        case ChooseColour
        case None
        case NotANumber
        case OutOfRange
        case CardNotPlayable


    enum TurnState:
        case PlayerTurn(player: Player)
        case GameWon(player: Player)
        case None
