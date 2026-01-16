package de.htwg.Uno.model.builder.Impl

import de.htwg.Uno.model.*
import de.htwg.Uno.model.builder.*
import de.htwg.Uno.model.Enum.ActionState
import de.htwg.Uno.model.Enum.TurnState
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Model.*
import scalafx.concurrent.Worker.State.Failed
import scala.util.{Failure, Success}
import scala.util.Try
import de.htwg.Uno.model.state.*

object GameBuilder {
  def apply(gameStates: GameStates): GameBuilder = new GameBuilderImpl(
    gameStates = gameStates
  )
}

private[model] case class GameBuilderImpl(
    player: List[Player] = List.empty,
    index: Integer = 0,
    deck: List[Card] = List.empty,
    table: Option[Card] = None,
    ActionState: de.htwg.Uno.model.Enum.ActionState =
      de.htwg.Uno.model.Enum.ActionState.None,
    TurnState: de.htwg.Uno.model.Enum.TurnState =
      de.htwg.Uno.model.Enum.TurnState.None,
    gameStates: GameStates
) extends GameBuilder {
  def withPlayers(newPlayers: List[Player]): GameBuilder =
    copy(player = newPlayers)

  def withIndex(newIndex: Integer): GameBuilder =
    copy(index = newIndex)

  def withDeck(newDeck: List[Card]): GameBuilder =
    copy(deck = newDeck)

  def withTable(newTable: Option[Card]): GameBuilder =
    copy(table = Some(Card(Coulor.red, Symbol.One)))

  def withActionState(
      newActionState: de.htwg.Uno.model.Enum.ActionState
  ): GameBuilder =
    copy(ActionState = newActionState)

  def withTurnState(
      newTurnState: de.htwg.Uno.model.Enum.TurnState
  ): GameBuilder =
    copy(TurnState = newTurnState)

  override def build(): Try[Game] =
    Success(
      Game(
        player = player,
        index = index,
        deck = deck,
        table = table,
        ActionState = ActionState,
        TurnState = TurnState
      )
    )

}
