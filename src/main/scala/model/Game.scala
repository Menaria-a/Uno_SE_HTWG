package de.htwg.Uno.model
import de.htwg.Uno.model.Player
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.Enum.*

    case class Game(
        player : List[Player],
        index : Integer,
        deck: List[Card],
        table: Card, 
        ActionState: ActionState,
        TurnState: TurnState
    )