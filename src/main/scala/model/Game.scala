package de.htwg.Uno.model
import de.htwg.Uno.model.Player
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.builder.GameBuilder

    case class Game(
        player : List[Player],
        index : Integer,
        deck: List[Card],
        table: Option[Card], 
        ActionState: ActionState,
        TurnState: TurnState
    ):


        def toBuilder (builder: GameBuilder): GameBuilder =
            builder
            .withPlayers(player)
            .withIndex(index)
            .withDeck(deck)
            .withTable(table)
            .withActionState(ActionState)
            .withTurnState(TurnState)