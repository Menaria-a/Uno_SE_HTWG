package de.htwg.Uno.aView.handler

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.controller.*
import de.htwg.Uno.model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.*
import de.htwg.Uno.model.GameExtention.*

class ChooseCardHandlerSpec extends AnyWordSpec with Matchers {

  // Dummy Card
  val card1 = Card(Coulor.red, Symbol.Five)
  val card2 = Card(Coulor.green, Symbol.Seven)

  // Dummy Player
  val player = Player("Test", hand = List(card1, card2), 0)

  // Dummy Game constructor function
  def mkGame(actionState: ActionState, index: Int = 0): Game =
    Game(
      player = List(player),
      index = index,
      deck = List(),
      table = Some(card1),
      ActionState = actionState,
      TurnState = TurnState.None
    )

  "A ChooseCardHandler" should {

    "play a card when input is a valid index and state is ChooseCard" in {
      val game = mkGame(ActionState.ChooseCard)
      val handler = new ChooseCardHandler()

      val result = handler.handleRequest("1", game)

      result._1 shouldBe PlayCardAction(0, 0, 0)
      result._2 shouldBe 1
    }

    "return InvalidAction when index is out of bounds" in {
      val game = mkGame(ActionState.ChooseCard)
      val handler = new ChooseCardHandler()

      handler.handleRequest("5", game) shouldBe (InvalidAction, 0)
      handler.handleRequest("-1", game) shouldBe (InvalidAction, 0)
    }

    "delegate when ActionState is NOT ChooseCard" in {
      val game = mkGame(ActionState.DrawCard)

      val nextHandler = new InputHandler {
        override def next = None
        override def setNext(h: InputHandler) = this
        override def handleRequest(input: String, game: Game) =
          (InvalidAction, 99)
      }

      val handler = new ChooseCardHandler(Some(nextHandler))

      handler.handleRequest("0", game) shouldBe (InvalidAction, 99)
    }

    "delegate when input is NOT a number" in {
      val game = mkGame(ActionState.ChooseCard)

      val nextHandler = new InputHandler {
        override def next = None
        override def setNext(h: InputHandler) = this
        override def handleRequest(input: String, game: Game) =
          (InvalidAction, 77)
      }

      val handler = new ChooseCardHandler(Some(nextHandler))

      handler.handleRequest("x", game) shouldBe (InvalidAction, 77)
    }

    "setNext should return a new ChooseCardHandler with updated next" in {
      val next = new ChooseCardHandler()
      val handler = new ChooseCardHandler()

      val updated = handler.setNext(next)

      updated.next shouldBe Some(next)
    }
  }
}
