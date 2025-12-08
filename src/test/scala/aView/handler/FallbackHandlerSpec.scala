package de.htwg.Uno.aView.handler

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.controller.*
import de.htwg.Uno.model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.*

class FallbackHandlerSpec extends AnyWordSpec with Matchers {

  val dummyCard = Card(Coulor.red, Symbol.One)
  val dummyPlayer = Player("P", List(dummyCard), 0)

  def mkGame(): Game =
    Game(
      player = List(dummyPlayer),
      index = 0,
      deck = List.empty,
      table = dummyCard,
      ActionState = ActionState.None,
      TurnState = TurnState.None
    )

  "A FallbackHandler" should {

    "always return InvalidAction and 0 for any input" in {
      val handler = new FallbackHandler()
      val game = mkGame()

      handler.handleRequest("anything", game) shouldBe (InvalidAction, 0)
      handler.handleRequest("42", game) shouldBe (InvalidAction, 0)
      handler.handleRequest("", game) shouldBe (InvalidAction, 0)
    }

    "ignore next handler and never delegate" in {
      val nextHandlerCalled = new InputHandler {
        override def next = None
        override def setNext(h: InputHandler) = this

        override def handleRequest(input: String, game: Game) =
          fail("FallbackHandler must never delegate to next handler")
      }

      val handler = new FallbackHandler(Some(nextHandlerCalled))
      val game = mkGame()

      handler.handleRequest("test", game) shouldBe (InvalidAction, 0)
    }

    "setNext should return itself (not a new handler)" in {
      val next = new FallbackHandler()
      val handler = new FallbackHandler()

      val updated = handler.setNext(next)

      updated shouldBe handler
      updated.next shouldBe None  // next wird ignoriert
    }
  }
}


