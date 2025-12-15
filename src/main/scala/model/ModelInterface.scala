package de.htwg.Uno.model
import de.htwg.Uno.model.Model.*
import de.htwg.Uno.model.Enum.*



object  ModelInterface:
    type Card =  de.htwg.Uno.model.Card
    type Player = de.htwg.Uno.model.Player
    type Game = de.htwg.Uno.model.Game
    type Coulor = de.htwg.Uno.model.Model.Coulor
    type Symbol = de.htwg.Uno.model.Model.Symbol
    type ActionState = de.htwg.Uno.model.Enum.ActionState
    type TurnState = de.htwg.Uno.model.Enum.TurnState


    val Coulor = de.htwg.Uno.model.Model.Coulor
    val Symbol = de.htwg.Uno.model.Model.Symbol
    val ActionState = de.htwg.Uno.model.Enum.ActionState
    val TurnState = de.htwg.Uno.model.Enum.TurnState

    object GameBuilder:
        def apply(): de.htwg.Uno.model.builder.GameBuilder =
            de.htwg.Uno.model.builder.GameBuilder()

    object Card:
        def apply(
            coulor: Coulor,
            symbol: Symbol,
        ): de.htwg.Uno.model.Card =
            de.htwg.Uno.model.Card(coulor, symbol)

    object Player:
        def apply(
            name: String,
            hand : List[Card],
            index : Int
        ): de.htwg.Uno.model.Player =
            de.htwg.Uno.model.Player(name,hand,index)

    object Game:
        def apply(
            player: List[Player],
            index: Integer,
            deck: List[Card],
            table: Card,
            ActionState: ActionState,
            TurnState: TurnState
        ): de.htwg.Uno.model.Game =
            de.htwg.Uno.model.Game(
                player,index,deck,table,ActionState,TurnState
            )





