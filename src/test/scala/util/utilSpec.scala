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
import de.htwg.Uno.Module
import de.htwg.Uno.util.Undo.CommandManager
import com.google.inject.Guice

class UtilSpec extends AnyWordSpec with Matchers {

  val manager = CommandManager()
  val injector = Guice.createInjector(new Module)
  val controller = injector.getInstance(classOf[Controller])

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
      observable.subscribers should contain(obs2)
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
