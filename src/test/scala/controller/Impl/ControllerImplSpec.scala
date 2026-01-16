package de.htwg.Uno.controller.Impl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.Uno.model._
import de.htwg.Uno.model.Model._
import de.htwg.Uno.model.Enum._
import de.htwg.Uno.model.state._
import de.htwg.Uno.model.state.Impl.GameStatesImpl
import de.htwg.Uno.util.Undo.CommandManager
import de.htwg.Uno.util.FileIOInterface
import de.htwg.Uno.controller.PlayerInput
import scala.util.Try

class ControllerImplSpec extends AnyWordSpec with Matchers {

  object TestHelper {
    def card(c: Coulor, s: Symbol) = Card(c, s)
    def player(name: String, hand: List[Card] = Nil, idx: Int = 0) = Player(name, hand, idx)

    // stub all states
    object StubGameStates extends GameStatesImpl(
      new InitState {
        def chooseColour(game: Game, colour: Coulor, hand: Card, input: Integer): (Card, Game) = (hand, game)
        def dealCardsToHand(player: Player, deck: List[Card], n: Int): (Player, List[Card]) = (player, deck)
        def handleInvalidInput(game: Game, tableCard: Card, message: ActionState): (Game, Integer) = (game, 0)
        def handleTurn(game: Game, currentPlayerIndex: Int, chosenCardIndex: Int): (Game, Integer) = (game, 0)
        def isPlayable(table: Card, hand: Card): Boolean = true
        def nextPlayerIndex(currentIndex: Int, playerCount: Int, skipNext: Boolean): Int = currentIndex
        def parseCardIndex(index: Int, player: Player, game: Game, tableCard: Card, currentPlayerIndex: Int): (Game, Integer) = (game, 0)
        def playCard(game: Game, playerIdx: Int, cardIdx: Int): (Game, Integer) = (game, 0)
        def playCardIfValid(card: Card, game: Game, tableCard: Card, currentPlayerIndex: Int): (Game, Integer) = (game, 0)
        def plusN(game: Game, nextPlayerIndex: Int, card: Card, n: Int): Game = game
        def start(p1: Player, p2: Player, gameStates: GameStates): Game = 
          Game(List(p1, p2), 0, Nil, None, ActionState.None, TurnState.None)
        def turn(card: Card, game: Game, currentPlayerIndex: Int): (Game, Integer) = (game, 0)
        def wisher(int: Integer): Coulor = Coulor.red
      },
      new PlayCardState {
        def chooseColour(game: Game, colour: Coulor, hand: Card, input: Integer): (Card, Game) = (hand, game)
        def dealCardsToHand(player: Player, deck: List[Card], n: Int): (Player, List[Card]) = (player, deck)
        def handleInvalidInput(game: Game, tableCard: Card, message: ActionState): (Game, Integer) = (game, 0)
        def handleTurn(game: Game, currentPlayerIndex: Int, chosenCardIndex: Int): (Game, Integer) = (game, 0)
        def isPlayable(table: Card, hand: Card): Boolean = true
        def nextPlayerIndex(currentIndex: Int, playerCount: Int, skipNext: Boolean): Int = currentIndex
        def parseCardIndex(index: Int, player: Player, game: Game, tableCard: Card, currentPlayerIndex: Int): (Game, Integer) = (game, 0)
        def playCard(game: Game, playerIdx: Int, cardIdx: Int): (Game, Integer) = (game, 0)
        def playCardIfValid(card: Card, game: Game, tableCard: Card, currentPlayerIndex: Int): (Game, Integer) = (game, 0)
        def plusN(game: Game, nextPlayerIndex: Int, card: Card, n: Int): Game = game
        def start(p1: Player, p2: Player, gameStates: GameStates): Game = Game(List(p1, p2), 0, Nil, None, ActionState.None, TurnState.None)
        def turn(card: Card, game: Game, currentPlayerIndex: Int): (Game, Integer) = (game, 0)
        def wisher(int: Integer): Coulor = Coulor.red
      },
      new DrawCardState {
        def chooseColour(game: Game, colour: Coulor, hand: Card, input: Integer): (Card, Game) = (hand, game)
        def dealCardsToHand(player: Player, deck: List[Card], n: Int): (Player, List[Card]) = (player, deck)
        def handleInvalidInput(game: Game, tableCard: Card, message: ActionState): (Game, Integer) = (game, 0)
        def handleTurn(game: Game, currentPlayerIndex: Int, chosenCardIndex: Int): (Game, Integer) = (game, 0)
        def isPlayable(table: Card, hand: Card): Boolean = true
        def nextPlayerIndex(currentIndex: Int, playerCount: Int, skipNext: Boolean): Int = currentIndex
        def parseCardIndex(index: Int, player: Player, game: Game, tableCard: Card, currentPlayerIndex: Int): (Game, Integer) = (game, 0)
        def playCard(game: Game, playerIdx: Int, cardIdx: Int): (Game, Integer) = (game, 0)
        def playCardIfValid(card: Card, game: Game, tableCard: Card, currentPlayerIndex: Int): (Game, Integer) = (game, 0)
        def plusN(game: Game, nextPlayerIndex: Int, card: Card, n: Int): Game = game
        def start(p1: Player, p2: Player, gameStates: GameStates): Game = Game(List(p1, p2), 0, Nil, None, ActionState.None, TurnState.None)
        def turn(card: Card, game: Game, currentPlayerIndex: Int): (Game, Integer) = (game, 0)
        def wisher(int: Integer): Coulor = Coulor.red
      },
      new WishCardState {
        def chooseColour(game: Game, colour: Coulor, hand: Card, input: Integer): (Card, Game) = (hand, game)
        def dealCardsToHand(player: Player, deck: List[Card], n: Int): (Player, List[Card]) = (player, deck)
        def handleInvalidInput(game: Game, tableCard: Card, message: ActionState): (Game, Integer) = (game, 0)
        def handleTurn(game: Game, currentPlayerIndex: Int, chosenCardIndex: Int): (Game, Integer) = (game, 0)
        def isPlayable(table: Card, hand: Card): Boolean = true
        def nextPlayerIndex(currentIndex: Int, playerCount: Int, skipNext: Boolean): Int = currentIndex
        def parseCardIndex(index: Int, player: Player, game: Game, tableCard: Card, currentPlayerIndex: Int): (Game, Integer) = (game, 0)
        def playCard(game: Game, playerIdx: Int, cardIdx: Int): (Game, Integer) = (game, 0)
        def playCardIfValid(card: Card, game: Game, tableCard: Card, currentPlayerIndex: Int): (Game, Integer) = (game, 0)
        def plusN(game: Game, nextPlayerIndex: Int, card: Card, n: Int): Game = game
        def start(p1: Player, p2: Player, gameStates: GameStates): Game = Game(List(p1, p2), 0, Nil, None, ActionState.None, TurnState.None)
        def turn(card: Card, game: Game, currentPlayerIndex: Int): (Game, Integer) = (game, 0)
        def wisher(int: Integer): Coulor = Coulor.red
      }
    )
  }

  "ControllerImpl" should {

    "update, undo, redo, save, and load correctly" in {
      val game = Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
      val cmdManager = new CommandManager()
val fileIO: FileIOInterface = new FileIOInterface {
  override def save(game: Game): Try[Unit] = Try(()) // always succeeds
  override def load(): Try[Game] = Try(game)         // returns the given game
}

      val controller = new ControllerImpl(game, cmdManager, TestHelper.StubGameStates, fileIO)

      // test updateAll
      val newGame = Game(TestHelper.player("Bob", Nil, 0) :: Nil, 0, Nil, None, ActionState.None, TurnState.None)
      controller.updateAll(newGame) shouldBe newGame

      // test initloop
      val playerInput = new PlayerInput {
        def getInputs(): String = "Alice"
        def getInput(game: Game, input: PlayerInput): Integer = 0
      }
      val startedGame = controller.initloop(playerInput)
      startedGame.player.map(_.name) should contain ("Alice")

      // test undo/redo (stubbed, manager empty)
      controller.undo()  // should not throw
      controller.redo()  // should not throw

      // test save/load
      controller.saveGame() shouldBe controller.game
      controller.loadGame() shouldBe controller.game
    }
    
  }
}






