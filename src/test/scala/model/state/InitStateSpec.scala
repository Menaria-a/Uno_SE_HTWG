package de.htwg.Uno.model.state
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers._
import de.htwg.Uno.model.state.InitState
import de.htwg.Uno.model.Model.*
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Player
import de.htwg.Uno.controller.PlayerInput
import de.htwg.Uno.model.Enum.*


class InitStateSpecs extends AnyWordSpec with Matchers{

    val Init = new InitState(Player(name = "p1",Nil, 0 ),Player(name = "p2", Nil, 0))
    val name1 = Player("p1", Nil, 0)
    val name2 = Player("p2", Nil, 0)

    "The deckmaker function" should {
        "create a valid new game with two players" in {

                val g = Init.deckmaker(name1, name2)
                g.player.length shouldBe 2
                g.player.head.hand.length shouldBe 5
                g.deck.length should be > 0
            }
        }
    }



