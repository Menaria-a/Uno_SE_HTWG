package de.htwg.Uno.controller

import de.htwg.Uno.model.Enum.ActionState
import de.htwg.Uno.model.state.InitState
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Player
import de.htwg.Uno.util.Observable
import de.htwg.Uno.controller.Command.*

case class Controller(var game: Game) extends Observable {

  /** Spielzustand aktualisieren und Observer benachrichtigen */
  def updateAll(g: Game): Game =
    game = g
    notifyObservers
    game

  /** Initialisierung der Spieler */
  def initloop(input: PlayerInput): Game =
    val p1 = Player(input.getInputs(), Nil, 0)
    val p2 = Player(input.getInputs(), Nil, 0)
    val initState = InitState(p1, p2)
    val game = initState.start(p1, p2)
    updateAll(game)
    game

  /** Spielzug ausführen */
  @scala.annotation.tailrec
  final def gameloop(input: PlayerInput): Unit =
    val index = game.index
    val inputs = input.getInput(game, input)

    // PlayCardCommand ausführen
    val cmd = PlayCardCommand(playerIdx = index, cardIdx = 0, inputs)
    val (newGame, hint) = cmd.execute(game)   // execute liefert neues Game + Hinweis
    updateAll(newGame)

    hint match
      case 1 =>
        val colourCmd = ChooseColourCommand(hand = newGame.table, input)
        val colourGame = colourCmd.execute(game)._1
        updateAll(colourGame.copy(ActionState = ActionState.ChooseCard))
      case 5 =>
        System.exit(0)
      case 6 =>
        val nextGame = newGame.copy(index = (index + 1) % 2, TurnState = game.TurnState)
        updateAll(nextGame)
      case 3 =>
        val drawCmd = DrawCardCommand(index)
        val drawGame = drawCmd.execute(newGame)._1
        updateAll(drawGame)
      case _ =>
        updateAll(newGame.copy(ActionState = ActionState.ChooseCard))

    gameloop(input)
}


































