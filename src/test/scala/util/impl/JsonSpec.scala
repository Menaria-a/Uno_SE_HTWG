package de.htwg.Uno.util.Impl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.Uno.model.*
import de.htwg.Uno.model.Model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.state.*
import de.htwg.Uno.model.state.Impl.*
import scala.util.{Try, Success, Failure}
import java.io.File

class FileIOJsonSpec extends AnyWordSpec with Matchers {

  val gameStates: GameStates = new GameStates {
    override val InitState: GameState = InitStateImpl
    override val PlayerTurnState: GameState = PlayCardStateImpl
    override val WishCardState: GameState = WishCardStateImpl
    override val drawCardState: GameState = DrawCardStateImpl
  }

  val testFilePath = "testGame.json"
  val fileIO = new FileIOJson(testFilePath, gameStates)

  val samplePlayer1 = Player("Alice", List(Card(Coulor.red, Symbol.One)), 0)
  val samplePlayer2 = Player("Bob", List(Card(Coulor.blue, Symbol.Two)), 1)
  val sampleGame = Game(
    player = List(samplePlayer1, samplePlayer2),
    index = 0,
    deck = List(Card(Coulor.green, Symbol.Three)),
    table = Some(Card(Coulor.yellow, Symbol.Four)),
    ActionState = ActionState.ChooseCard,
    TurnState = TurnState.PlayerTurn(samplePlayer1)
  )

  "FileIOJson" should {

    "save a game to JSON file successfully" in {
      val result: Try[Unit] = fileIO.save(sampleGame)
      result shouldBe a[Success[_]]
      new File(testFilePath).exists() shouldBe true
    }

    "load a game from JSON file successfully" in {
      fileIO.save(sampleGame)
      val loadedGameTry: Try[Game] = fileIO.load()
      loadedGameTry shouldBe a[Success[_]]
      val loadedGame = loadedGameTry.get
      loadedGame.player.map(_.name) should contain allElementsOf List("Alice", "Bob")
      loadedGame.deck.size shouldBe 1
      loadedGame.table.get.colour shouldBe Coulor.yellow
      loadedGame.ActionState shouldBe ActionState.ChooseCard
    }

    "throw an exception for invalid TurnState type" in {
      val invalidJson =
        """
          |{
          |  "player": [],
          |  "index": 0,
          |  "deck": [],
          |  "table": null,
          |  "actionState": "ChooseCard",
          |  "turnState": { "type": "INVALID" }
          |}
        """.stripMargin

      val tmpFile = new File("invalidGame.json")
      import java.nio.file.Files
      Files.write(tmpFile.toPath, invalidJson.getBytes)

      val badLoad = Try(fileIO.load())
      badLoad.isFailure shouldBe false

      tmpFile.delete()
    }
  }


}


