package de.htwg.Uno.util.Undo
import de.htwg.Uno.model.Game
import de.htwg.Uno.controller.Command.*

case class CommandManager(
    undoStack: List[(Command, Game)] = Nil,
    redoStack: List[(Command, Game)] = Nil
):

    def executeCommand(cmd: Command, game: Game): (CommandManager, Game) =
        val newGame = cmd.execute(game)
        val (games, int) = newGame
        val newManager = this.copy(
        undoStack = (cmd, game) :: undoStack,
        redoStack = Nil
        )
        (newManager, games)

    def undo(game: Game): Option[(CommandManager, Game)] =
        undoStack match
        case (cmd, oldGame) :: rest =>
            val reverted = cmd.undo(game)
            val newManager = this.copy(
            undoStack = rest,
            redoStack = (cmd, game) :: redoStack
            )
            Some(newManager, reverted)
        case Nil => None

    def redo(game: Game): Option[(CommandManager, Game)] =
        redoStack match
        case (cmd, lastGame) :: rest =>
            val redone = cmd.execute(game)
            val (games, int) = redone
            val newManager = this.copy(
            redoStack = rest,
            undoStack = (cmd, game) :: undoStack
            )
            Some(newManager, games)
        case Nil => None


