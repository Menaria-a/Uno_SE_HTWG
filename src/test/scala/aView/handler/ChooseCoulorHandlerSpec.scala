package de.htwg.Uno.aView.handler

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.controller.*
import de.htwg.Uno.model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.*

class ChooseColourHandlerSpec extends AnyWordSpec with Matchers {

  // Hilfswerte für Game
  val dummyPlayerList = List.empty[Player]
  val dummyDeck = List.empty[Card]
  val dummyTableCard =
    Card(Coulor.red, Symbol.One) // an deine Card-Klasse anpassen
  val dummyTurnState = TurnState.None // oder irgendein gültiger Wert

  def mkGame(actionState: ActionState): Game =
    Game(
      player = dummyPlayerList,
      index = 0,
      deck = dummyDeck,
      table = Some(dummyTableCard),
      ActionState = actionState,
      TurnState = dummyTurnState
    )

  "A ChooseColourHandler" should {

    "return correct colour actions when ActionState is ChooseColour" in {
      val game = mkGame(ActionState.ChooseColour)
      val handler = new ChooseColourHandler()

      handler.handleRequest("r", game) shouldBe (PlayCardAction(0, 0, 0), 2)
      handler.handleRequest("g", game) shouldBe (PlayCardAction(0, 0, 0), 3)
      handler.handleRequest("b", game) shouldBe (PlayCardAction(0, 0, 0), 4)
      handler.handleRequest("y", game) shouldBe (PlayCardAction(0, 0, 0), 1)
    }

    "return InvalidAction for unknown input in ChooseColour state" in {
      val game = mkGame(ActionState.ChooseColour)
      val handler = new ChooseColourHandler()

      handler.handleRequest("xyz", game) shouldBe (InvalidAction, 0)
    }

    "delegate to next handler when ActionState is NOT ChooseColour" in {
      val game = mkGame(ActionState.ChooseCard)

      val nextHandler = new InputHandler {
        override def next = None
        override def setNext(h: InputHandler) = this
        override def handleRequest(input: String, game: Game) =
          (InvalidAction, 99)
      }

      val handler = new ChooseColourHandler(Some(nextHandler))

      handler.handleRequest("r", game) shouldBe (InvalidAction, 99)
    }

    "setNext should return a new handler with updated next option" in {
      val next = new ChooseColourHandler()
      val handler = new ChooseColourHandler()

      val updated = handler.setNext(next)

      updated.next shouldBe Some(next)
    }
  }
}
