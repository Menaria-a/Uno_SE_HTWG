package de.htwg.Uno.aView.Gui

import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.geometry._
import scalafx.stage.{Stage, Modality}
import scalafx.scene.paint.Color._

import scalafx.Includes._

import de.htwg.Uno.controller.Controller
import de.htwg.Uno.model.{Card, Game}
import de.htwg.Uno.model.Model.*
import de.htwg.Uno.model.Enum.*

class Gui(controller: Controller) {

  private val tableCardBox = new HBox {
    spacing = 10
    alignment = Pos.Center
  }

  private val playersBox = new VBox {
    spacing = 10
    alignment = Pos.TopCenter
  }

  /** GUI starten */
  def start(): Unit = {

    val tableLabel = new Label("Table:") {
      style = "-fx-font-size: 20px;"
    }

    val root = new VBox(20) {
      padding = Insets(20)
      children = Seq(tableLabel, tableCardBox, playersBox)
    }

    val stage = new Stage {
      title = "Uno ScalaFX"
      scene = new Scene(root, 800, 600)
    }

    updateUI()
    stage.show()
  }

  /** UI aktualisieren */
  def updateUI(): Unit = {
    tableCardBox.children.clear()
    tableCardBox.children.add(renderCard(controller.game.table, clickable = false))

    playersBox.children.clear()

    controller.game.player.zipWithIndex.foreach { case (p, idx) =>

      val labelBox = new Label(s"${p.name}'s Hand:") {
        style = "-fx-font-size: 16px;"
      }

      val handBox = new HBox(5) {
        alignment = Pos.Center
      }

      p.hand.zipWithIndex.foreach { case (c, cIdx) =>
        handBox.children.add(renderCard(c, clickable = true, idx, cIdx))
      }

      playersBox.children.addAll(labelBox, handBox)
    }
  }

  /** Karten als DSL-Button */
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

    val b = new Button(symbolStr) {
      style = s"-fx-background-color: $colorStr; -fx-font-size: 18px; -fx-text-fill: black;"
      prefWidth = 60
      prefHeight = 90
    }

    if clickable then
      b.onAction = _ => handleCardClick(playerIdx, cardIdx, card)

    b
  }

  /** Klick-Handler für Karten */
  private def handleCardClick(playerIdx: Int, cardIdx: Int, card: Card): Unit =
    if card.symbol == Symbol.Wish || card.symbol == Symbol.Plus_4 then

      val dialog = new Stage {
        initModality(Modality.ApplicationModal)
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

            de.htwg.Uno.controller.Command.PlayCardCommand(playerIdx, cardIdx, colInt)
              .execute(controller.game)

            dialog.close()
            updateUI()
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

      dialog.showAndWait()

    else
      de.htwg.Uno.controller.Command.PlayCardCommand(playerIdx, cardIdx, -1)
        .execute(controller.game)

      updateUI()
}
