package de.htwg.Uno.controller
import de.htwg.Uno.model.Enum.ActionState
import de.htwg.Uno.model.state.InitState
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Player
import de.htwg.Uno.util.Observable
import de.htwg.Uno.controller.Command.PlayCardCommand
import de.htwg.Uno.controller.Command.Command
import de.htwg.Uno.controller.Command.ChooseColourCommand
import de.htwg.Uno.controller.Command.DrawCardCommand
import de.htwg.Uno.model.Enum.TurnState.PlayerTurn
import de.htwg.Uno.model.Enum.TurnState.GameWon
import de.htwg.Uno.util.Undo.CommandManager
import javax.swing.Action
import scala.collection.View.Updated
import scala.annotation.tailrec


case class Controller(
    var game: Game, var cmdManager: CommandManager
    ) extends Observable {


        def updateAll(g: Game): Game =
            game = g
            notifyObservers
            game

        def initloop(input: PlayerInput) : Game =
            val p1 = Player(input.getInputs(), Nil, 0)
            val p2 = Player(input.getInputs(), Nil, 0)
            val initState = InitState(p1, p2)
            val game = initState.start(p1, p2)
            updateAll(game)
            game

        @scala.annotation.tailrec
        final def gameloop(input: PlayerInput) : Unit = 
            val index = game.index
            val inputs = input.getInput(game, input)

            val Cmd = PlayCardCommand(playerIdx = index, cardIdx = 0, inputs)
            val (newManager, newGame, value) = cmdManager.executeCommand(Cmd,game)
            cmdManager = newManager
            val Hint = value
            print(Hint)
            if (Hint == 1){
                val com = ChooseColourCommand(hand = newGame.table, input)
                val new2Game = newGame.copy(ActionState = ActionState.ChooseColour, TurnState = PlayerTurn(game.player(index)))
                updateAll(new2Game)
                val (newerManager, newerGame, value) = newManager.executeCommand(com,new2Game)
                val newestGame = newerGame.copy(ActionState = ActionState.ChooseCard)
                cmdManager = newerManager
                updateAll(newestGame)}
            else if (Hint == 5) {
                System.exit(0)
            }
            else if (Hint == 6) {
                val newgame = newGame.copy(TurnState = PlayerTurn(newGame.player((index + 1) % 2)))
                updateAll(newgame)
            }
            else if (Hint == 3){
                val con = DrawCardCommand(index)
                val (newerManager , newerGame, value) = cmdManager.executeCommand(con,newGame)
                updateAll(newerGame)
            }
            else {
                val newerGame = newGame.copy(ActionState = ActionState.ChooseCard)
                updateAll(newerGame)
            }
                
                gameloop(input: PlayerInput)

        def undo(): Unit =
            cmdManager.undo(game) match
            case Some((newManager, prevGame)) =>
                cmdManager = newManager
                game = prevGame
                updateAll(game)
            case None =>
                    None

        def redo(): Unit =
            cmdManager.redo(game) match
            case Some((newManager, nextGame)) =>
                cmdManager = newManager
                game = nextGame
                updateAll(game)
            case None =>
                    None

}


































