package de.htwg.Uno.controller

import de.htwg.Uno.model.ModelInterface.Game
import de.htwg.Uno.controller.{
  Controller as InternalController,
  PlayerInput as InternalPlayerInput
}
import de.htwg.Uno.controller.Command.{
  Command as InternalCommand,
  PlayCardCommand as InternalPlayCardCommand,
  ChooseColourCommand as InternalChooseColourCommand,
  DrawCardCommand as InternalDrawCardCommand
}
import de.htwg.Uno.util.Undo.CommandManager

/** Controller Component Interface
  *
  * Public port for the Uno Controller component.
  * All external access to controller functionality must go through this
  * interface. Internal implementation details are hidden in the impl package.
  *
  * The Controller manages the game flow, executes commands, handles undo/redo
  * operations, and notifies observers about state changes.
  */
object ControllerInterface:

  // ---------------------------------------------------------------------------
  // Type Aliases
  // ---------------------------------------------------------------------------

  /** Main Controller interface for managing the Uno game flow. */
  type Controller = InternalController

  /** Base trait for all commands used by the controller. */
  type Command = InternalCommand

  /** Command to play a card. */
  type PlayCardCommand = InternalPlayCardCommand

  /** Command to choose a colour (wild cards). */
  type ChooseColourCommand = InternalChooseColourCommand

  /** Command to draw a card. */
  type DrawCardCommand = InternalDrawCardCommand

  /** Player input abstraction (UI / CLI / Test). */
  type PlayerInput = InternalPlayerInput

  // ---------------------------------------------------------------------------
  // Command Factories
  // ---------------------------------------------------------------------------

  /** Factory for creating PlayCardCommand instances. */
  val PlayCardCommand = InternalPlayCardCommand

  /** Factory for creating ChooseColourCommand instances. */
  val ChooseColourCommand = InternalChooseColourCommand

  /** Factory for creating DrawCardCommand instances. */
  val DrawCardCommand = InternalDrawCardCommand

  // ---------------------------------------------------------------------------
  // Controller Factory
  // ---------------------------------------------------------------------------

  /** Factory for creating Controller instances.
    *
    * @param game
    *   Initial game state
    * @param commandManager
    *   Command manager for undo/redo functionality
    * @return
    *   A new Controller instance
    */
  object Controller:
    def apply(
        game: Game,
        commandManager: CommandManager
    ): InternalController =
      InternalController(game, commandManager)



