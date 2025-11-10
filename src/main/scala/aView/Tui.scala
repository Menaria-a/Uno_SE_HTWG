
package de.htwg.Uno.aView
import de.htwg.Uno.controller.game
import de.htwg.Uno.controller.game.initGame
import de.htwg.Uno.controller.game.gameLoop
import de.htwg.Uno.modell.Model.Card
import de.htwg.Uno.modell.Model.Coulor
import de.htwg.Uno.modell.Model.Symbol
import de.htwg.Uno.modell.Model.Game
import scala.io.StdIn.readLine
import de.htwg.Uno.controller.game.deckmacher
import de.htwg.Uno.modell.Model.Player
import de.htwg.Uno.controller.game.currentPlayerFinder

object Tui:


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


    def inputManager (input: String, game : Game, message : String) : String = {
        
      input match {
        case "a" =>
          println("Ersten Namen eingeben, dann den Zweiten:")
          "a"

        case "b" =>
          println(message)
          "b"
          
        case "c" =>
          print("Wähle Kartenindex oder drücke Enter zum Ziehen: ")
          "c"
        case "d" =>
          val updatedGame = game
          println(Tui.gamerenderer(updatedGame))
          "d"
        case "e" =>
          println("Nächster Spieler wird übersprungen!")
          "e"
        case "f" =>
          println("Bitte gewünschte Farbe eingeben: ")
          "f"
      }
    }
    def inputManagerG(input : String) : Game = {
      input match {
        case "c" =>
          println("Ersten Namen eingeben, dann den Zweiten:")
          val game = deckmacher()
          println(Tui.gamerenderer(game))
          game
      }
    }
    def inputManagerD (input : String, currentPlayer : Player) : String = {
      input match {
        case "a" =>
          println(s"\n${currentPlayer.name} ist am Zug.")
          "a"
        case "b" =>
          println(s"${currentPlayer.name} hat gewonnen!")
          println("Spiel beendet.")
          "b"
        case "c" =>
          println(s"${currentPlayer.name} zieht eine Karte.")
          "c"
      }
    }
    def inputManagerR (input : String) : String = {
      input match {
          case "b" =>
          val n1 = readLine()
          n1
      }
    }

