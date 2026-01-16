package de.htwg.Uno.controller.Command

import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.state.WishCardState
import de.htwg.Uno.model.Model.Coulor
import de.htwg.Uno.controller.PlayerInput
import de.htwg.Uno.model.Enum.ActionState
import de.htwg.Uno.model.Enum.TurnState.PlayerTurn
import de.htwg.Uno.model.state.GameStates

case class ChooseColourCommand(
    hand: Card,
    input: PlayerInput,
    gameStates: GameStates
) extends ChooseColourCommandT:
  override def execute(game: Game): (Game, Integer) =
    val chooseColour = input.getInput(game, input)
    val (_, newGame) = gameStates.WishCardState.chooseColour(
      game,
      hand.colour,
      hand,
      chooseColour
    )
    (newGame, 2)
