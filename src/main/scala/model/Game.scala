package de.htwg.Uno.model
import de.htwg.Uno.model.Player
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.builder.GameBuilder

trait Game:
    def player: List[Player]
    def index: Integer
    def deck: List[Card]
    def table: Card
    def ActionState: ActionState
    def TurnState: TurnState
    def toBuilder: GameBuilder

    def copy(
        player: List[Player] = this.player,
        index: Integer = this.index,
        deck: List[Card] = this.deck,
        table: Card = this.table,
        ActionState: ActionState = this.ActionState,
        TurnState: TurnState = this.TurnState
    ): Game = Game(
        player,index,deck,table,ActionState,TurnState
    )


object Game:
    def apply(
        player: List[Player],
        index: Integer,
        deck: List[Card],
        table: Card,
        ActionState: ActionState,
        TurnState: TurnState 
    ): Game =
        impl.GameImpl(
            player,index,deck,table,ActionState,TurnState
        )