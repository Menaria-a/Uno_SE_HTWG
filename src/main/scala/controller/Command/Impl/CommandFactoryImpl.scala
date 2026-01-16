package de.htwg.Uno.controller.Command.Impl

import de.htwg.Uno.model.*
import de.htwg.Uno.controller.PlayerInput
import de.htwg.Uno.controller.Command.*
import com.google.inject.Inject
import de.htwg.Uno.model.state.GameStates

class CommandFactoryImpl @Inject (gameStates: GameStates)
    extends CommandFactory:

  override def playCard(
      playerIdx: Int,
      cardIdx: Int,
      input: Int
  ): PlayCardCommandT =
    PlayCardCommand(playerIdx, cardIdx, input, gameStates)

  override def draw(playerIdx: Int): DrawCardCommandT =
    DrawCardCommand(playerIdx, gameStates.drawCardState)

  override def chooseColour(
      hand: Card,
      input: PlayerInput
  ): ChooseColourCommandT =
    ChooseColourCommand(hand, input, gameStates)

  override def undo(game: Game): UndoCommandT =
    UndoCommand(game)
