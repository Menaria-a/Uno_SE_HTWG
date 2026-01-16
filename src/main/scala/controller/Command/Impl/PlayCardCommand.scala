package de.htwg.Uno.controller.Command
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Enum.TurnState
import de.htwg.Uno.model.Enum.ActionState
import de.htwg.Uno.model.Enum.TurnState.PlayerTurn
import de.htwg.Uno.controller.PlayerInput
import de.htwg.Uno.controller.Command.PlayCardCommandT
import de.htwg.Uno.model.state.GameStates

case class PlayCardCommand(
    playerIdx: Int,
    cardIdx: Int,
    input: Int,
    gameStates: GameStates
) extends PlayCardCommandT:
  override def execute(game: Game): (Game, Integer) =
    val newGame = game.copy(
      TurnState = PlayerTurn(game.player((playerIdx + 1) % 2)),
      ActionState = ActionState.ChooseCard
    )
    val idx = input
    gameStates.PlayerTurnState.playCard(newGame, playerIdx, idx)

  override def undo(currentGame: Game, previousGame: Game): (Game) =
    currentGame
