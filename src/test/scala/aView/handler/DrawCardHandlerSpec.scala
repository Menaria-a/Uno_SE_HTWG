package de.htwg.Uno.aView.handler

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.controller.*
import de.htwg.Uno.model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.*
import de.htwg.Uno.model.GameExtention.*

class DrawCardHandlerSpec extends AnyWordSpec with Matchers {

  // Dummy Card + Player wie zuvor
  val dummyCard = Card(Coulor.red, Symbol.One)
  val dummyPlayer = Player("P", hand = List(dummyCard), 0)

  // Game-Erzeuger
  def mkGame(actionState: ActionState): Game =
    Game(
      player = List(dummyPlayer),
      index = 0,
      deck = List.empty,
      table = dummyCard,
      ActionState = actionState,
      TurnState = TurnState.None
    )

  "A DrawCardHandler" should {

    "return DrawAction when state is ChooseCard and input is empty" in {
      val game = mkGame(ActionState.ChooseCard)
      val handler = new DrawCardHandler()

      handler.handleRequest("", game) shouldBe (DrawAction, 500)
      handler.handleRequest("   ", game) shouldBe (DrawAction, 500)
    }

    "delegate to next handler when input is NOT empty" in {
      val game = mkGame(ActionState.ChooseCard)

      val nextHandler = new InputHandler {
        override def next = None
        override def setNext(h: InputHandler) = this
        override def handleRequest(input: String, game: Game) =
          (InvalidAction, 77)
      }

      val handler = new DrawCardHandler(Some(nextHandler))

      handler.handleRequest("not empty", game) shouldBe (InvalidAction, 77)
    }

    "delegate when ActionState is NOT ChooseCard" in {
      val game = mkGame(ActionState.DrawCard)

      val nextHandler = new InputHandler {
        override def next = None
        override def setNext(h: InputHandler) = this
        override def handleRequest(input: String, game: Game) =
          (InvalidAction, 88)
      }

      val handler = new DrawCardHandler(Some(nextHandler))

      handler.handleRequest("", game) shouldBe (InvalidAction, 88)
    }

    "setNext should return a new handler with updated next" in {
      val next = new DrawCardHandler()
      val handler = new DrawCardHandler()

      val updated = handler.setNext(next)

      updated.next shouldBe Some(next)
    }
  }
}


