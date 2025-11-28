package de.htwg.Uno.util
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers._
import de.htwg.Uno.controller.Controller
import de.htwg.Uno.model.Model.Coulor
import de.htwg.Uno.model.Model.Symbol
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Enum.ActionState
import de.htwg.Uno.model.Enum.TurnState
import de.htwg.Uno.aView.Tui

class UtilSpec extends AnyWordSpec with Matchers {


    val controller = new Controller(game = Game(Nil,0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None))
    val TuiInstance = new Tui(controller: Controller)

    "The remove function" should {
        "remove the observer from the subscribers list" in {
            val obs1 = new Observer { override def update: Unit = () }
            val obs2 = new Observer { override def update: Unit = () }

            val observable = new Observable()
            observable.add(obs1)
            observable.add(obs2)
            observable.subscribers should contain allOf (obs1, obs2)
            observable.remove(obs1)

            observable.subscribers should not contain obs1
            observable.subscribers should contain (obs2)
        }
    }

    class FakeController extends Controller(game = Game(Nil,0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)) {
        private var _game: Game = Game(Nil,0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)

    }
    

    "The update function" should {
        "clear the screen and print the game, person, and status" in {
            val controller = new FakeController()
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
            TuiInstance.update
            }

            val output = outputStream.toString
            output should include ("\u001b[2J\u001b[H")  
            output should include ("Table:")            
        }   
    }


    "notifyObservers" should {
        "call update on all subscribers" in {
            var obs1Updated = false
            var obs2Updated = false

            val obs1 = new Observer { def update: Unit = obs1Updated = true }
            val obs2 = new Observer { def update: Unit = obs2Updated = true }

            val observable = new Observable()
            observable.add(obs1)
            observable.add(obs2)

            observable.notifyObservers

            obs1Updated shouldBe true
            obs2Updated shouldBe true
        }

        "do nothing if there are no subscribers" in {
            val observable = new Observable()
            noException should be thrownBy observable.notifyObservers
        }
    }

}