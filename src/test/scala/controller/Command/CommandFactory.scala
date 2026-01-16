package de.htwg.Uno.controller.Command

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.Uno.model.*
import de.htwg.Uno.controller.*
import de.htwg.Uno.model._
import de.htwg.Uno.model.Model._
import de.htwg.Uno.model.Enum._
import de.htwg.Uno.model.state._
import de.htwg.Uno.model.state.Impl.GameStatesImpl

class CommandFactorySpec extends AnyWordSpec with Matchers {

  // Minimal stub commands with execute implemented
  class StubPlayCardCommandT extends PlayCardCommandT {
    override def execute(game: Game): (Game, Integer) = (game, 0)
  }
  class StubDrawCardCommandT extends DrawCardCommandT {
    override def execute(game: Game): (Game, Integer) = (game, 0)
  }
  class StubChooseColourCommandT extends ChooseColourCommandT {
    override def execute(game: Game): (Game, Integer) = (game, 0)
  }
  class StubUndoCommandT extends UndoCommandT {
    override def execute(game: Game): (Game, Integer) = (game, 0)
  }

  // Anonymous implementation of CommandFactory for testing
  val factory = new CommandFactory {
    override def playCard(playerIdx: Int, cardIdx: Int, input: Int) =
      new StubPlayCardCommandT
    override def draw(playerIdx: Int) = new StubDrawCardCommandT
    override def chooseColour(hand: Card, input: PlayerInput) =
      new StubChooseColourCommandT
    override def undo(game: Game) = new StubUndoCommandT
  }

  "CommandFactory trait" should {

    "create PlayCardCommand via createCommand" in {
      val action = PlayCardAction(0, 1, 2)
      val result = factory.createCommand(
        action,
        Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
      )
      result shouldBe a[PlayCardCommandT]
      val (g, value) = result.execute(
        Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
      )
      value shouldBe 0
    }

    "create DrawCardCommand via createCommand" in {
      val action = DrawAction(0)
      val result = factory.createCommand(
        action,
        Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
      )
      result shouldBe a[DrawCardCommandT]
    }

    "create ChooseColourCommand via createCommand" in {
      val card = Card(Coulor.red, Symbol.One)
      val input = new PlayerInput {
        override def getInput(game: Game, input: PlayerInput) = 0
        def getInputs() = "Alice"
      }
      val action = ChooseColourAction(card, input)
      val result = factory.createCommand(
        action,
        Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
      )
      result shouldBe a[ChooseColourCommandT]
    }

    "create UndoCommand via createCommand" in {
      val game = Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
      val action = UndoAction(game)
      val result = factory.createCommand(action, game)
      result shouldBe a[UndoCommandT]
    }

  }

}
