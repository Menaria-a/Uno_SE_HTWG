package de.htwg.Uno.model.builder.Impl

import de.htwg.Uno.model.*
import de.htwg.Uno.model.builder.*
import de.htwg.Uno.model.Enum.ActionState
import de.htwg.Uno.model.Enum.TurnState
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Model.*
import scalafx.concurrent.Worker.State.Failed
import scala.util.Failure
import scala.util.Try



object GameBuilder {
    def apply(
        cardFactory: CardFactory
    ): GameBuilder = new GameBuilderImpl(
        cardFactory = cardFactory
    )
}

private[model] case class GameBuilderImpl(
    player: List[Player] = List.empty,
    index : Integer = 0,
    deck: List[Card] = List.empty,
    table: Card = Card(Coulor.red, Symbol.One),
    ActionState: de.htwg.Uno.model.Enum.ActionState = de.htwg.Uno.model.Enum.ActionState.None,
    TurnState: de.htwg.Uno.model.Enum.TurnState = de.htwg.Uno.model.Enum.TurnState.None,
    gameFactory: Option[GameFactory] = None,
    cardFactory: CardFactory
) extends GameBuilder {
    def withPlayers(newPlayers: List[Player]): GameBuilder =
        copy(player = newPlayers)

    def withIndex(newIndex: Integer): GameBuilder =
        copy(index = newIndex )

    def withDeck(newDeck: List[Card]): GameBuilder =
        copy(deck = newDeck)

    def withTable(newTable: Card = Card(Coulor.red, Symbol.One)): GameBuilder =
        copy(table = newTable)

    def withActionState(newActionState: de.htwg.Uno.model.Enum.ActionState): GameBuilder =
        copy(ActionState = newActionState)

    def withTurnState(newTurnState: de.htwg.Uno.model.Enum.TurnState): GameBuilder =
        copy(TurnState = newTurnState)

    def withGameFactory(factory: GameFactory): GameBuilder = 
        copy(gameFactory = Some(factory))

    def build(): Try[Game] = {
        gameFactory match {
            case None =>
                Failure(
                    new IllegalStateException(
                        "GameFactory must be set before building"
                    )
                )
            case Some(factory) =>
                    factory(
                        player = player,
                        index  = index,
                        deck = deck,
                        table = table,
                        ActionState = ActionState,
                        TurnState = TurnState
                    )
                }
        }
    }

