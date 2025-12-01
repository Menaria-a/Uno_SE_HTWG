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
import javax.swing.Action


case class Controller(
    var game: Game
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

        def gameloop(input: PlayerInput) : Unit = 
            val index = game.index
            val Cmd = PlayCardCommand(playerIdx = index, cardIdx = 0, input)
            val (newGame, int) = Cmd.execute(game)
            if (int == 1){
                val com = ChooseColourCommand(hand = newGame.table, input)
                val new2Game = newGame.copy(ActionState = ActionState.ChooseColour, TurnState = PlayerTurn(game.player(index)))
                updateAll(new2Game)
                val (newerGame, notUsed) = com.execute(new2Game)
                val newestGame = newerGame.copy(ActionState = ActionState.ChooseCard)
                updateAll(newestGame)}
            else if (int == 5) {
                System.exit(0)
            }
            else if (int == 3){
                val con = DrawCardCommand(index)
                val (newerGame , notUsed) = con.execute(newGame)
                updateAll(newerGame)
            }
            else {
                val newerGame = newGame.copy(ActionState = ActionState.ChooseCard)
                updateAll(newerGame)
            }
                
                gameloop(input: PlayerInput)

}


































