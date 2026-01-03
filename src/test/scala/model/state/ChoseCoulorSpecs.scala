package de.htwg.Uno.model.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.model.ModelInterface.*

class WishCardStateSpec extends AnyWordSpec with Matchers {

  "WishCardState" should {

    "choose the correct colour and update the table" in {
      val dummyCard = Card(Coulor.red, Symbol.One)
      val dummyGame = Game(Nil, 0, Nil, dummyCard, ActionState.None, TurnState.None)

      val testCases = Seq(
        (1, Coulor.yellow),
        (2, Coulor.red),
        (3, Coulor.green),
        (4, Coulor.blue)
      )

      testCases.foreach { case (input, expectedColour) =>
        val (newCard, newGame) = WishCardState.chooseColour(dummyGame, expectedColour, dummyCard, input)

        newCard.coulor shouldBe expectedColour
        newGame.table.coulor shouldBe expectedColour
        newGame.ActionState shouldBe ActionState.ChooseColour
      }
    }
  }

  "wisher function" should {

    "map inputs 1-4 to the correct colours" in {
      WishCardState.wisher(1) shouldBe Coulor.yellow
      WishCardState.wisher(2) shouldBe Coulor.red
      WishCardState.wisher(3) shouldBe Coulor.green
      WishCardState.wisher(4) shouldBe Coulor.blue
    }
  }



    "playCard always returns fixed game and index" in {
      val dummyGame = Game(Nil, 0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)

      val (resultGame, resultIndex) = WishCardState.playCard(dummyGame, playerIdx = 0, cardIdx = 0)

      resultGame.player shouldBe Nil
      resultGame.index shouldBe 0
      resultGame.deck shouldBe Nil
      resultGame.table.coulor shouldBe Coulor.red
      resultGame.table.symbol shouldBe Symbol.One
      resultGame.ActionState shouldBe ActionState.None
      resultGame.TurnState shouldBe TurnState.None

      resultIndex shouldBe 2
    }
  }













