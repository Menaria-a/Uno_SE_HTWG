package de.htwg.Uno.controller.Command

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.*
import de.htwg.Uno.controller.Command.DrawCardCommand
import de.htwg.Uno.model.state.DrawCardState

class DrawCardCommandSpec extends AnyWordSpec with Matchers {

  // Dummy Karten
    val dummyCard1 = Card(Coulor.red, Symbol.One)
    val dummyCard2 = Card(Coulor.green, Symbol.Two)

  // Dummy Spieler
    val dummyPlayer = Player("Alice", List(dummyCard1), 0)

  // Dummy Game
    val dummyGame = Game(
    player = List(dummyPlayer),
    index = 0,
    deck = List(dummyCard2),
    table = Card(Coulor.red, Symbol.Three),
    ActionState = ActionState.DrawCard,
    TurnState = TurnState.None
)

    "A DrawCardCommand" should {

    "return a modified game and value = 2" in {
    val command = DrawCardCommand(0)
    val (newGame, value) = command.execute(dummyGame)

    // Rückgabewert prüfen
    value shouldBe 2

    // Das Game muss geändert sein
    newGame.deck shouldBe empty
    newGame.player(0).hand.size shouldBe (dummyGame.player(0).hand.size + 1)
    newGame.player(0).hand.last shouldBe dummyCard2
    }

    "not modify unrelated parts of the game" in {
    val command = DrawCardCommand(0)
    val (newGame,_ ) = command.execute(dummyGame)

    // Name bleibt gleich
    newGame.player.map(_.name) shouldEqual dummyGame.player.map(_.name)

    // Tischkarte bleibt gleich
    newGame.table shouldBe dummyGame.table
    }
}
}