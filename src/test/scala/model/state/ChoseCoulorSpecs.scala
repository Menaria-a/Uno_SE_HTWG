package de.htwg.Uno.model.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers._
import de.htwg.Uno.model.state.WishCardState
import de.htwg.Uno.model.Model.*
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Player
import de.htwg.Uno.controller.PlayerInput
import de.htwg.Uno.model.Enum.*


class ChoseCoulorSpecs extends AnyWordSpec with Matchers{


        "The wisher function" should {
        "return the correct wished colour" in {
            WishCardState.wisher(2) shouldBe Coulor.red
            WishCardState.wisher(3) shouldBe Coulor.green
            WishCardState.wisher(1) shouldBe Coulor.yellow
            WishCardState.wisher(4) shouldBe Coulor.blue
        }
    }

        "The chooseColourForCard function" should {
        "replace the wish card with the chosen colour" in {

            val input = new java.io.ByteArrayInputStream("r\n".getBytes)
            Console.withIn(input) {
                val game = Game(List(Player("X", Nil, 0)),0, Nil, Card(Coulor.blue, Symbol.One), ActionState.None, TurnState.None)


                val fakeInput = 2
                val (card, newGame) = WishCardState.chooseColourForCard(
                Card(Coulor.red, Symbol.Wish),
                game,
                fakeInput
                )


                card.colour shouldBe Coulor.red
                newGame.table.colour shouldBe Coulor.red
            }
        }   
    }

        "The start function" should {

    "create a new game with the expected default values" in {
        val p1 = Player("Alice", Nil, 0)
        val p2 = Player("Bob", Nil, 0)

        val game = WishCardState.start(p1, p2)

        game.player shouldBe empty
        game.index shouldBe 0
        game.table shouldBe Card(Coulor.red, Symbol.One)
        game.ActionState shouldBe ActionState.None
        game.TurnState shouldBe TurnState.None
    }
    }

        "The playCard function" should {

            "return a new game with the correct top card and return value 2" in {
            val game = Game(
            List(Player("X", Nil, 0)),
            0,
            Nil,
            Card(Coulor.blue, Symbol.One),
            ActionState.None,
            TurnState.None
        )

            val (newGame, resultValue) = WishCardState.playCard(game, playerIdx = 0, cardIdx = 0)

            newGame.table shouldBe Card(Coulor.red, Symbol.One)
            resultValue shouldBe 2
            }
        }
}
    









