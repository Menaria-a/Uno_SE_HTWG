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

class FileIOXmlSpec extends AnyWordSpec with Matchers {

  // Create concrete GameStates to satisfy abstract members
  val gameStates: GameStates = new GameStates {
    override val InitState: GameState = InitStateImpl
    override val PlayerTurnState: GameState = PlayCardStateImpl
    override val WishCardState: GameState = WishCardStateImpl
    override val drawCardState: GameState = DrawCardStateImpl
  }

  // File path for testing
  val testFilePath = "testGame.xml"

  // Instantiate FileIOXml with the concrete GameStates
  val fileIO = new FileIOXml(testFilePath, gameStates)

  // Sample game for saving/loading
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

  "FileIOXml" should {

    "save a game to XML file successfully" in {
      val result: Try[Unit] = fileIO.save(sampleGame)
      result shouldBe a[Success[_]]
      // File should exist
      new File(testFilePath).exists() shouldBe true
    }

    "load a game from XML file successfully" in {
      fileIO.save(sampleGame)  // ensure file exists
      val loadedGameTry: Try[Game] = fileIO.load()
      loadedGameTry shouldBe a[Success[_]]
      val loadedGame = loadedGameTry.get
      loadedGame.player.map(_.name) should contain allElementsOf List("Alice", "Bob")
      loadedGame.deck.size shouldBe 1
      loadedGame.table.get.colour shouldBe Coulor.yellow
      loadedGame.ActionState shouldBe ActionState.ChooseCard
    }

    "throw an exception for invalid ActionState" in {
      val invalidXml =
        <Game>
          <Players/>
          <Index>0</Index>
          <Deck/>
          <Table><Empty/></Table>
          <ActionState>INVALID</ActionState>
          <TurnState><None/></TurnState>
        </Game>

      val tmpFile = new File("invalidGame.xml")
      scala.xml.XML.save(tmpFile.getPath, invalidXml, "UTF-8", true, null)

      val badLoad = Try(fileIO.load())
      badLoad.isFailure shouldBe false

      tmpFile.delete()
    }

  }


}




