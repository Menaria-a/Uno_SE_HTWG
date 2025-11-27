package de.htwg.Uno.model.builder

import de.htwg.Uno.model.Player
import de.htwg.Uno.model.Enum.ActionState
import de.htwg.Uno.model.Enum.TurnState
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Model.*



object GameBuilder {
    def apply(): GameBuilder = new GameBuilder()
}

case class GameBuilder(
    player: List[Player] = List.empty,
    index : Integer = 0,
    deck: List[Card] = List.empty,
    table: Card = Card(Coulor.red, Symbol.One),
    ActionState: de.htwg.Uno.model.Enum.ActionState = de.htwg.Uno.model.Enum.ActionState.None,
    TurnState: de.htwg.Uno.model.Enum.TurnState = de.htwg.Uno.model.Enum.TurnState.None
) {
    def withPlayers(newPlayers: List[Player]): GameBuilder =
        copy(player = newPlayers)

    def withIndex(newIndex: Integer): GameBuilder =
        copy(index = newIndex )

    def withDeck(newDeck: List[Card]): GameBuilder =
        copy(deck = newDeck)

    def withTable(newTable: Card): GameBuilder =
        copy(table = newTable)

    def withActionState(newActionState: de.htwg.Uno.model.Enum.ActionState): GameBuilder =
        copy(ActionState = newActionState)

    def withTurnState(newTurnState: de.htwg.Uno.model.Enum.TurnState): GameBuilder =
        copy(TurnState = newTurnState)

    def build(): Game = {
        Game(
            player = player,
            index  = index,
            deck = deck,
            table = table,
            ActionState = ActionState,
            TurnState = TurnState
        )
    }

}