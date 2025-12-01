
package de.htwg.Uno.controller.Command

import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Enum.TurnState
import de.htwg.Uno.model.Enum.ActionState
import de.htwg.Uno.model.state.PlayCardState
import de.htwg.Uno.model.Enum.TurnState.PlayerTurn
import de.htwg.Uno.controller.PlayerInput


case class PlayCardCommand(playerIdx: Int, cardIdx: Int, input: PlayerInput) extends Command:
    override def execute(game: Game): (Game, Integer) = 
        val newGame = game.copy(TurnState = PlayerTurn(game.player((playerIdx +1) % 2)), ActionState = ActionState.ChooseCard)
            val inputs = input.getInput(game)
            val idx = inputs
            PlayCardState.playCard(newGame, playerIdx, idx)
