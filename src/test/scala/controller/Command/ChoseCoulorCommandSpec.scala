package de.htwg.Uno.controller.Command

import de.htwg.Uno.model.Model.Symbol
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.Coulor
import de.htwg.Uno.controller.PlayerInput
import de.htwg.Uno.controller.Command.ChooseColourCommand
import de.htwg.Uno.model.state.WishCardState

class ChooseColourCommandSpec extends AnyWordSpec with Matchers {

  // Dummy Karte
val dummyCard = Card(Coulor.red, Symbol.Wish)

  // Dummy Input
    val dummyInput: PlayerInput = new PlayerInput {
    override def getInput(game: Game, who: PlayerInput): Integer = 2
    override def getInputs(): String = "g"
}


  // Dummy Game
    val dummyGame = Game(
    player = List(Player("Alice", List(dummyCard), 0)),
    index = 0,
    deck = Nil,
    table = Card(Coulor.red, Symbol.One),
    ActionState = ActionState.ChooseColour,
    TurnState = TurnState.None
    )

    "A ChooseColourCommand" should {

    "update the game with the chosen colour and return correct value" in {
    val command = ChooseColourCommand(dummyCard, dummyInput)
    val (newGame, resultValue) = command.execute(dummyGame)

    newGame.table.colour shouldEqual Coulor.red
    resultValue shouldEqual 2
    }

    "not modify other parts of the game" in {
    val command = ChooseColourCommand(dummyCard, dummyInput)
    val (newGame,_ ) = command.execute(dummyGame)

    newGame.player.map( _.name) shouldEqual dummyGame.player.map( _.name)
    newGame.player.map(_ .hand.size) shouldEqual dummyGame.player.map(_ .hand.size)
    newGame.deck shouldEqual dummyGame.deck
    }
    }
}