package de.htwg.Uno.aView.Gui

import scalafx.application.Platform
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.stage.Stage
import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global

import de.htwg.Uno.controller.{Controller, PlayerInput}
import de.htwg.Uno.model.{Card, Player, Game}
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.*

class Gui(controller: Controller) {

  /** Startet die GUI */
  def start(): Unit = showPlayerNameInput()

  /** Spielername-Abfrage */
  private def showPlayerNameInput(): Unit = {
    val nameField1 = new TextField() { promptText = "Spieler 1 Name" }
    val nameField2 = new TextField() { promptText = "Spieler 2 Name" }
    val startButton = new Button("Spiel starten")

    val root = new VBox(10) {
      padding = Insets(20)
      alignment = Pos.Center
      children = Seq(
        new Label("Gib die Spielernamen ein:"),
        nameField1,
        nameField2,
        startButton
      )
    }

    val stage = new Stage {
      title = "Uno Spieler Auswahl"
      scene = new Scene(root, 300, 200)
    }

    startButton.onAction = _ => {
      val players = Array(
        Player(nameField1.text.value, Nil, 0),
        Player(nameField2.text.value, Nil, 0)
      )

      val initState = de.htwg.Uno.model.state.InitState(players(0), players(1))
      val game: Game = initState.start(players(0), players(1))
      controller.updateAll(game)

      stage.close()
      showMainGameGUI(players)

      // GameLoop asynchron starten
      Future { controller.gameloop(new GuiPlayerInput(this, players)) }
    }

    stage.show()
  }

  /** Haupt-GUI */
  private def showMainGameGUI(players: Array[Player]): Unit = {
    val tableCardBox = new HBox { spacing = 10; alignment = Pos.Center }
    val playersBox = new VBox { spacing = 15; alignment = Pos.TopCenter }

    val tableLabel = new Label("Tisch:") { style = "-fx-font-size: 20px;" }

    val root = new VBox(20) {
      padding = Insets(20)
      children = Seq(tableLabel, tableCardBox, playersBox)
    }

    val stage = new Stage {
      title = "Uno ScalaFX"
      scene = new Scene(root, 800, 600)
    }

    /** UI aktualisieren */
    def updateUI(): Unit = Platform.runLater {
      tableCardBox.children.clear()
      tableCardBox.children.add(renderCard(controller.game.table, clickable = false))

      playersBox.children.clear()
      controller.game.player.zipWithIndex.foreach { case (p, idx) =>
        val labelBox = new Label(s"${p.name}'s Hand:") { style = "-fx-font-size: 16px;" }
        val handBox = new HBox(5) { alignment = Pos.Center }

        p.hand.zipWithIndex.foreach { case (c, cIdx) =>
          handBox.children.add(renderCard(c, clickable = true, idx, cIdx))
        }

        if idx == controller.game.index then
          val drawButton = new Button("Draw Card") {
            style = "-fx-font-size: 16px; -fx-background-color: lightgray;"
            onAction = _ =>
              Future {
                val drawCmd = de.htwg.Uno.controller.Command.DrawCardCommand(idx)
                val (newManager, newGame, _) = controller.cmdManager.executeCommand(drawCmd, controller.game)
                controller.cmdManager = newManager
                controller.updateAll(newGame)
              }
          }
          handBox.children.add(drawButton)

        playersBox.children.addAll(labelBox, handBox)
      }
    }

    /** Controller Observer */
    controller.add(new de.htwg.Uno.util.Observer {
      override def update: Unit = updateUI()
    })

    updateUI()
    stage.show()
  }

  /** Karte rendern */
  private def renderCard(card: Card, clickable: Boolean, playerIdx: Int = 0, cardIdx: Int = 0): Button = {
    val colorStr = card.colour match
      case Coulor.red    => "red"
      case Coulor.yellow => "yellow"
      case Coulor.blue   => "blue"
      case Coulor.green  => "green"

    val symbolStr = card.symbol match
      case Symbol.Zero    => "0"
      case Symbol.One     => "1"
      case Symbol.Two     => "2"
      case Symbol.Three   => "3"
      case Symbol.Four    => "4"
      case Symbol.Five    => "5"
      case Symbol.Six     => "6"
      case Symbol.Seven   => "7"
      case Symbol.Eight   => "8"
      case Symbol.Nine    => "9"
      case Symbol.Plus_2  => "+2"
      case Symbol.Plus_4  => "+4"
      case Symbol.Reverse => "↺"
      case Symbol.Block   => "⯃"
      case Symbol.Wish    => "?"

    val button = new Button(symbolStr) {
      style = s"-fx-background-color: $colorStr; -fx-font-size: 18px; -fx-text-fill: black;"
      prefWidth = 60
      prefHeight = 90
    }

    if clickable then
      button.onAction = _ => handleCardClick(playerIdx, cardIdx, card)

    button
  }

  /** Klick auf Karte */
  private def handleCardClick(playerIdx: Int, cardIdx: Int, card: Card): Unit = {
    if card.symbol == Symbol.Wish || card.symbol == Symbol.Plus_4 then
      showColorChooser(playerIdx, cardIdx)
    else
      Future {
        val cmd = de.htwg.Uno.controller.Command.PlayCardCommand(playerIdx, cardIdx, -1)
        val (newManager, newGame, _) = controller.cmdManager.executeCommand(cmd, controller.game)
        controller.cmdManager = newManager
        controller.updateAll(newGame)
      }
  }

  /** Farbwahl für Plus_4 / Wish */
  private def showColorChooser(playerIdx: Int, cardIdx: Int): Unit = {
    val dialog = new Stage {
      initModality(scalafx.stage.Modality.ApplicationModal)
      title = "Choose Color"
    }

    val colors = List("red", "yellow", "blue", "green")
    val colorButtons = colors.map { col =>
      new Button(col.capitalize) {
        onAction = _ => {
          val colInt = col match
            case "red"    => 0
            case "yellow" => 1
            case "blue"   => 2
            case "green"  => 3

          Future {
            val cmd = de.htwg.Uno.controller.Command.PlayCardCommand(playerIdx, cardIdx, colInt)
            val (newManager, newGame, _) = controller.cmdManager.executeCommand(cmd, controller.game)
            controller.cmdManager = newManager
            controller.updateAll(newGame)
          }

          dialog.close()
        }
      }
    }

    dialog.scene = new Scene(
      new VBox(10) {
        alignment = Pos.Center
        padding = Insets(20)
        children = colorButtons
      }
    )

    dialog.show()
  }

  /** GUI PlayerInput für GameLoop */
  class GuiPlayerInput(gui: Gui, players: Array[Player]) extends PlayerInput {
    override def getInput(game: Game, input: PlayerInput): Integer = {
      // Hier kann man in Zukunft noch Promise/Future für echte Klicks verwenden
      -1 // Platzhalter: GameLoop reagiert asynchron auf Klicks
    }

    override def getInputs(): String = players(controller.game.index).name
  }
}
















