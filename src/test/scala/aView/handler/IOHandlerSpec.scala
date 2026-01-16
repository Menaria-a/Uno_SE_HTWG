package de.htwg.Uno.aView.handler

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.Uno.controller.*
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Enum.*

class IOHandlerSpec extends AnyWordSpec with Matchers {

  "IOHandler" should {

    "return (IOAction, 40) when input is 'load'" in {
      val handler = new IOHandler()
      val game = Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
      val (action, value) = handler.handleRequest("load", game)
      action shouldBe IOAction
      value shouldBe 40
    }

    "return (IOAction, 50) when input is 'safe'" in {
      val handler = new IOHandler()
      val game = Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
      val (action, value) = handler.handleRequest("safe", game)
      action shouldBe IOAction
      value shouldBe 50
    }

    "delegate to next handler when input is unknown" in {
      // Create a dummy next handler
      val nextHandler = new InputHandler {
        override def next: Option[InputHandler] = None
        override def handleRequest(
            input: String,
            game: Game
        ): (PlayerAction, Integer) =
          (IOAction, 99) // dummy return
        override def setNext(handler: InputHandler): InputHandler = this
      }

      val handler = new IOHandler(Some(nextHandler))
      val game = Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
      val (action, value) = handler.handleRequest("unknown", game)
      action shouldBe IOAction
      value shouldBe 99
    }

  }

}
