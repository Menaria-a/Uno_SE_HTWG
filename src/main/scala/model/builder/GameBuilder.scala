package de.htwg.Uno.model.builder

import de.htwg.Uno.model.Player
import de.htwg.Uno.model.Enum.ActionState
import de.htwg.Uno.model.Enum.TurnState
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.*
import de.htwg.Uno.model.Model.*
import scala.util.Try

trait GameBuilder {

  def withPlayers(newPlayers: List[Player]): GameBuilder

  def withIndex(newIndex: Integer): GameBuilder

  def withDeck(newDeck: List[Card]): GameBuilder

  def withTable(newTable: Option[Card]): GameBuilder

  def withActionState(
      newActionState: de.htwg.Uno.model.Enum.ActionState
  ): GameBuilder

  def withTurnState(newTurnState: de.htwg.Uno.model.Enum.TurnState): GameBuilder

  def build(): Try[Game]

}
