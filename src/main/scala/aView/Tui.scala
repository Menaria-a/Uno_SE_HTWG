
package de.htwg.Uno.aView
import de.htwg.Uno.controller.Controller
import de.htwg.Uno.controller.PlayerAction
import de.htwg.Uno.aView.handler.*
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.Model.Coulor
import de.htwg.Uno.model.Model.Symbol
import de.htwg.Uno.model.Game
import scala.io.StdIn.readLine
import de.htwg.Uno.model.Player
import de.htwg.Uno.util.Observer
import de.htwg.Uno.controller.PlayerInput
import de.htwg.Uno.model.Enum.ActionState

import de.htwg.Uno.model.Enum.TurnState
  import de.htwg.Uno.controller.DrawAction
  import de.htwg.Uno.controller.InvalidAction


  class Tui(controller : Controller) extends Observer with PlayerInput {
  


    def creator(card: Card): List[String] =
      val coulorInd = card.colour match
        case Coulor.red => "r"
        case Coulor.yellow => "y"
        case Coulor.blue => "b"
        case Coulor.green => "g"
      val picture = card.symbol match
        case Symbol.Zero => "0"
        case Symbol.One => "1"
        case Symbol.Two => "2"
        case Symbol.Three => "3"
        case Symbol.Four => "4"
        case Symbol.Five => "5"
        case Symbol.Six => "6"
        case Symbol.Seven => "7"
        case Symbol.Eight => "8"
        case Symbol.Nine => "9"
        case Symbol.Plus_2 => "+2"
        case Symbol.Plus_4 => "+4"
        case Symbol.Reverse => "↺"
        case Symbol.Block => "⯃"
        case Symbol.Wish => "?"
      List(
        "+------+",
        f"| $coulorInd%-2s   |",
        f"|  $picture%-2s  |",
        "|      |",
        "+------+"
      )
        
    def handrenderer(hand: List[Card]): String =
          if hand.isEmpty then "UnoUno"
          else 
            val lines = hand.map(creator)
            val combined = lines.transpose.map(_.mkString(" "))
            combined.mkString("\n")

    def tablerenderer(table: Card): String =
      creator(table).mkString("\n")


        
    def gamerenderer(game: Game): String =
      val playerStr = game.player
      .map(p => s"${p.name}'s hand:\n${handrenderer(p.hand)}")
      .mkString("\n\n")
      val tableStr = s"Table:\n${tablerenderer(game.table)}"
      s"$tableStr\n\n$playerStr"
      

    def renderAction(game : Game): String =
      game.ActionState match
        case ActionState.ChooseCard =>
          "Wähle eine Karte oder drücke Enter zum Ziehen:"

        case ActionState.DrawCard =>
          "Du hast eine Karte gezogen."

        case ActionState.ChooseColour =>
          "Bitte Farbe eingeben (r/g/y/b):"
        case ActionState.NotANumber =>
          "Bitte eine Zahl eingeben."
        case ActionState.OutOfRange =>
          "Dieser Kartenindex ist ungültig."
        case ActionState.CardNotPlayable =>
          "Diese Karte kann nicht gespielt werden."
        case ActionState.None =>
          ""

    def renderTurn(game: Game): String =
      game.TurnState match
        case TurnState.PlayerTurn(player) =>
          s"${player.name} ist am Zug."
        case TurnState.GameWon(player) =>
          s" ${player.name} hat gewonnen!"
        case TurnState.None =>
          ""

    override def update: Unit =
      val clear = "\u001b[2J\u001b[H" // Bildschirm löschen (ANSI)
      //
      println(clear)
      val status  = (renderAction(controller.game))
      val turn = (renderTurn(controller.game))
      val render = gamerenderer(controller.game)
      println(render)
      println(turn)
      print(status)
    override def getInput(game: Game, who: PlayerInput): (Integer) =
      val input = readLine()
          val action = processInput(input, game)
          val ind = 0

          if (action._1 == DrawAction) {
            val ind = 500
        //println(ind)
            ind
        }
          else if (action._1 == InvalidAction) {
        print("fehler")
            -1
        }
          else if (action._2 == 20){
            println("AAAAA")
            controller.undo()
            controller.gameloop(who)
            20
          }
          else if (action._2 == 30){
            controller.redo()
            controller.gameloop(who)
            30
          }

          else {
            val ind = action._2
        //println(ind)
            ind
      }


    override def getInputs(): String =
      readLine()


        
    def fake(print: String): String =
      println(print)
      "s"


    val fallbackHandler = new FallbackHandler()
    val chooseColourHandler = new ChooseColourHandler().setNext(fallbackHandler)
    val drawCardHandler = new DrawCardHandler().setNext(chooseColourHandler)
    val chooseCardHandler = new ChooseCardHandler().setNext(drawCardHandler)
    val UndoRedoHandler = new UndoRedoHandler().setNext(chooseCardHandler)




    private val rootHandler: InputHandler = UndoRedoHandler

  // --- Hier ist die processInput-Funktion ---
    def processInput(input: String, game: Game): (PlayerAction, Integer) =
    // Eingabe geht durch die Handler-Kette
      val action  = rootHandler.handleRequest(input, game)
      action
  }

