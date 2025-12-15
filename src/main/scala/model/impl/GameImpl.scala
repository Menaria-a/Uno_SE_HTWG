package de.htwg.Uno.model.impl
import de.htwg.Uno.model.{Game,Card, Player}
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.builder.GameBuilder


private [model] case class GameImpl(
    player: List[Player],
    index: Integer,
    deck: List[Card],
    table: Card,
    ActionState: ActionState,
    TurnState: TurnState 
) extends Game {

    def toBuilder: GameBuilder = {
        GameBuilder()
        .withPlayers(player)
        .withIndex(index)
        .withDeck(deck)
        .withTable(table)
        .withActionState(ActionState)
        .withTurnState(TurnState)
    }
}