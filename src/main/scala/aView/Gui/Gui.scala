package de.htwg.Uno.aView.Gui

import scalafx.application.Platform
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.*
import scalafx.scene.layout.*
import scalafx.stage.{Stage, Modality}

import scala.concurrent.{Future, Promise, Await}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

import java.util.concurrent.atomic.AtomicReference

import de.htwg.Uno.controller.{Controller, PlayerInput}
import de.htwg.Uno.model.{Card, Player, Game}
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.*

class Gui(controller: Controller):

  // ============================================================
  // Kein var: Callback + Promise als AtomicReference
  // ============================================================

  private val turnCallbackRef =
    new AtomicReference[Int => Unit](_ => ())

  private val promiseRef =
    new AtomicReference[Promise[Int]]()

  def setTurnCallback(cb: Int => Unit): Unit =
    turnCallbackRef.set(cb)

  // ============================================================
  // Start GUI
  // ============================================================

  def start(): Unit =
    showPlayerNameInput()

  private def showPlayerNameInput(): Unit =
    val name1 = new TextField:
      promptText = "Spieler 1"

    val name2 = new TextField:
      promptText = "Spieler 2"

    val startBtn = new Button("Start")

    val root = new VBox(10,
      new Label("Spielernamen:"),
      name1,
      name2,
      startBtn
    ):
      alignment = Pos.Center
      padding = Insets(20)

    val stage = new Stage:
      title = "Spieler Auswahl"
      scene = new Scene(root, 300, 200)

    startBtn.onAction = _ =>
      val players = Array(
        Player(name1.text.value, Nil, 0),
        Player(name2.text.value, Nil, 0)
      )

      val init = de.htwg.Uno.model.state.InitState(players(0), players(1))
      val game = init.start(players(0), players(1))
      controller.updateAll(game)

      stage.close()
      showMainGameGUI(players)

      Future {
        controller.gameloop(new GuiPlayerInput(this, players))
      }

    stage.show()

  // ============================================================
  // MAIN GAME GUI
  // ============================================================

  private def showMainGameGUI(players: Array[Player]): Unit =
    val tableBox = new HBox:
      alignment = Pos.Center
      spacing = 20

    val playersBox = new VBox:
      alignment = Pos.TopCenter
      spacing = 20

    val root = new VBox(20,
      new Label("Tisch:") { style = "-fx-font-size: 20px;" },
      tableBox,
      playersBox
    ):
      padding = Insets(20)

    val stage = new Stage:
      title = "ScalaFX UNO"
      scene = new Scene(root, 900, 600)

    // ======================================
    // UI UPDATE
    // ======================================

    def updateUI(): Unit =
      Platform.runLater {

        // Tischkarte
        tableBox.children.setAll(
          renderCard(controller.game.table, clickable = false)
        )

        // Spieler
        playersBox.children.clear()

        controller.game.player.zipWithIndex.foreach { (pl, idx) =>
          val label = new Label(s"${pl.name}'s Hand:"):
            style = "-fx-font-size: 16px;"

          val handBox = new HBox(10):
            alignment = Pos.Center

          // Handkarten
          pl.hand.zipWithIndex.foreach { (card, cIdx) =>
            handBox.children.add(
              renderCard(card, clickable = true, playerIdx = idx, cardIdx = cIdx)
            )
          }

          // Draw-Button nur beim aktiven Spieler
          if idx == controller.game.index then
            handBox.children.add(
              new Button("Karte ziehen"):
                onAction = _ =>
                  // -1 = Draw Signal (führt zu Hint=3 → DrawCardCommand)
                  turnCallbackRef.get().apply(-1)
            )

          playersBox.children.addAll(label, handBox)
        }
      }

    // Observer registrieren
    controller.add(new de.htwg.Uno.util.Observer {
      override def update: Unit = updateUI()
    })

    updateUI()
    stage.show()

  // ============================================================
  // Karte rendern
  // ============================================================

  private def renderCard(card: Card, clickable: Boolean,
                         playerIdx: Int = 0, cardIdx: Int = 0): Button =

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

    val btn = new Button(symbolStr):
      style = s"-fx-background-color: $colorStr; -fx-font-size: 18px;"
      prefWidth = 60
      prefHeight = 90

    if clickable then
      btn.onAction = _ =>
        handleCardClick(card, cardIdx)

    btn

  // ============================================================
  // Kartenklick
  // ============================================================

  private def handleCardClick(card: Card, cardIdx: Int): Unit =
    card.symbol match
      case Symbol.Plus_4 | Symbol.Wish =>
        showColorChooser(cardIdx)
      case _ =>
        turnCallbackRef.get().apply(cardIdx)

  // ============================================================
  // Farbwahl
  // ============================================================

  private def showColorChooser(cardIdx: Int): Unit =
    val dialog = new Stage:
      title = "Farbe auswählen"
      initModality(Modality.ApplicationModal)

    val colors = List("red", "yellow", "blue", "green")

    val buttons = colors.map { c =>
      new Button(c.capitalize):
        prefWidth = 120
        onAction = _ =>
          val col = c match
            case "red"    => 0
            case "yellow" => 1
            case "blue"   => 2
            case "green"  => 3

          turnCallbackRef.get().apply(col)
          dialog.close()
    }

    dialog.scene = new Scene(
      new VBox(10, buttons*):
        alignment = Pos.Center
        padding = Insets(20)
    )

    dialog.show()

  // ============================================================
  // PlayerInput (Controller erwartet dieses Verhalten!)
  // ============================================================

  class GuiPlayerInput(gui: Gui, players: Array[Player]) extends PlayerInput:

    override def getInput(game: Game, input: PlayerInput): Integer =
      val p = Promise[Int]()
      promiseRef.set(p)

      gui.setTurnCallback(value => p.success(value))

      // Future[Int] → Future[java.lang.Integer]
      Await.result(p.future.map(Int.box), Duration.Inf)

    override def getInputs(): String =
      val idx = controller.game.index
      players(idx).name



















