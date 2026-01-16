package de.htwg.Uno.controller.Command

import de.htwg.Uno.model.*
import de.htwg.Uno.model.state.GameState

case class DrawCardCommand(playerIdx: Int, gameState: GameState)
    extends DrawCardCommandT:
  override def execute(game: Game): (Game, Integer) =
    val games = gameState.drawCard(game, playerIdx)
    (games, 2)
