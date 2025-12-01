
package de.htwg.Uno.controller.Command

import de.htwg.Uno.model.Game
import de.htwg.Uno.model.state.DrawCardState

case class DrawCardCommand(playerIdx: Int) extends Command:
    override def execute(game: Game): (Game, Integer) =
        val games = DrawCardState.drawCard(game, playerIdx)
        (games, 2)


