package de.htwg.Uno.model.state

trait GameStates {
  def drawCardState: GameState
  def InitState: GameState
  def PlayerTurnState: GameState
  def WishCardState: GameState

}
