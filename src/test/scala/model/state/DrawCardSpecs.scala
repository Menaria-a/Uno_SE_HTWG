package de.htwg.Uno.model.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers._
import de.htwg.Uno.model.state.DrawCardState
import de.htwg.Uno.model.Model.*
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Player
import de.htwg.Uno.controller.PlayerInput
import de.htwg.Uno.model.Enum.*


class DrawCardSpecsSpecs extends AnyWordSpec with Matchers{



    "The nextPlayerIndex function" should {
        "return the correct next player index" in {
            DrawCardState.nextPlayerIndex(0, 2, false) shouldBe 1
            DrawCardState.nextPlayerIndex(1, 2, false) shouldBe 0
            DrawCardState.nextPlayerIndex(0, 3, true) shouldBe 0
        }
    }


        "The dealCardsToHand function" should {
        "add the correct number of cards to a player's hand" in {
            val p = Player("Tester", Nil, 0)
            val deck = List(Card(Coulor.red, Symbol.One), Card(Coulor.green, Symbol.Two))
            val (newPlayer, newDeck) = DrawCardState.dealCardsToHand(p, deck, 1)
            newPlayer.hand.length shouldBe 1
            newDeck.length shouldBe 1
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



}