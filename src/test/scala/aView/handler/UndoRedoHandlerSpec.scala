package de.htwg.Uno.aView.handler

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.controller.*
import de.htwg.Uno.model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.*

class UndoRedoHandlerSpec extends AnyWordSpec with Matchers {

  val dummyCard = Card(Coulor.red, Symbol.One)
  val dummyPlayer = Player("P", List(dummyCard), 0)

  def mkGame(): Game =
    Game(
      player = List(dummyPlayer),
      index = 0,
      deck = List.empty,
      table = Some(dummyCard),
      ActionState = ActionState.None,
      TurnState = TurnState.None
    )

  "An UndoRedoHandler" should {

    "return UndoAction when input is 'undo'" in {
      val handler = new UndoRedoHandler()
      val game = mkGame()

      handler.handleRequest("undo", game) shouldBe (UndoAction(
        Game(
          List(Player("P", List(Card(Coulor.red, Symbol.One)), 0)),
          0,
          List(),
          Some(Card(Coulor.red, Symbol.One)),
          ActionState.None,
          TurnState.None
        )
      ), 20)
    }

    "return RedoAction when input is 'redo'" in {
      val handler = new UndoRedoHandler()
      val game = mkGame()

      handler.handleRequest("redo", game) shouldBe (RedoAction, 30)
    }

    "delegate to next handler for other inputs" in {
      val nextHandler = new InputHandler {
        override def next = None
        override def setNext(h: InputHandler) = this
        override def handleRequest(input: String, game: Game) =
          (InvalidAction, 99)
      }

      val handler = new UndoRedoHandler(Some(nextHandler))
      val game = mkGame()

      handler.handleRequest("xyz", game) shouldBe (InvalidAction, 99)
    }

    "setNext should return a new handler with updated next" in {
      val next = new UndoRedoHandler()
      val handler = new UndoRedoHandler()

      val updated = handler.setNext(next)

      updated.next shouldBe Some(next)
    }
  }
}
