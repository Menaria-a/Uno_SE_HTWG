package de.htwg.Uno.controller

import de.htwg.Uno.model.ModelInterface.*
import de.htwg.Uno.util.{Observable, Observer}
import de.htwg.Uno.util.Undo.CommandManager

/** Controller trait for managing the Uno game flow and state transitions.
  *
  * The Controller is the central component of the application. It processes
  * player input, executes commands, manages undo/redo functionality, and
  * notifies all observers (views) about game state changes.
  *
  * The Controller follows the Command pattern for reversible actions and the
  * Observer pattern to keep views synchronized with the game state.
  */
trait Controller extends Observable:

  /** Initializes the game with player input.
    *
    * Creates players, initializes the game state and notifies observers.
    *
    * @param input
    *   The player input source
    * @return
    *   The initialized game
    */
  def initloop(input: PlayerInput): Game

  /** Main game loop.
    *
    * Processes player turns recursively until the game ends.
    *
    * @param input
    *   The player input source
    */
  def gameloop(input: PlayerInput): Unit

  /** Updates the game state and notifies all observers.
    *
    * @param g
    *   The new game state
    * @return
    *   The updated game
    */
  def updateAll(g: Game): Game

  /** Undoes the last executed command.
    *
    * Restores the previous game state if available.
    */
  def undo(): Unit

  /** Redoes the last undone command.
    *
    * Reapplies a previously undone command if available.
    */
  def redo(): Unit

  /** Returns the current game state.
    *
    * @return
    *   The current Game
    */
  def game: Game

object Controller:

  /** Creates a new Controller instance.
    *
    * @param game
    *   Initial game state
    * @param commandManager
    *   Command manager for undo/redo functionality
    * @return
    *   A Controller instance
    */
  def apply(
      game: Game,
      commandManager: CommandManager
  ): Controller =
    Impl.ControllerImpl(game, commandManager)




































