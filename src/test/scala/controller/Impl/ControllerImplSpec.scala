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
import scala.util.{Try, Success, Failure}
import java.util.concurrent.TimeoutException
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import org.scalatest.time.SpanSugar._

class ControllerImplSpec extends AnyWordSpec with Matchers {

  object TestHelper {
    def card(c: Coulor, s: Symbol) = Card(c, s)
    def player(name: String, hand: List[Card] = Nil, idx: Int = 0) =
      Player(name, hand, idx)

    // stub all states
    object StubGameStates
        extends GameStatesImpl(
          new InitState {
            def drawCard(game: Game, playerIdx: Int): Game =
              (game)
            def chooseColour(
                game: Game,
                colour: Coulor,
                hand: Card,
                input: Integer
            ): (Card, Game) = (hand, game)
            def dealCardsToHand(
                player: Player,
                deck: List[Card],
                n: Int
            ): (Player, List[Card]) = (player, deck)
            def handleInvalidInput(
                game: Game,
                tableCard: Card,
                message: ActionState
            ): (Game, Integer) = (game, 0)
            def handleTurn(
                game: Game,
                currentPlayerIndex: Int,
                chosenCardIndex: Int
            ): (Game, Integer) = (game, 0)
            def isPlayable(table: Card, hand: Card): Boolean = true
            def nextPlayerIndex(
                currentIndex: Int,
                playerCount: Int,
                skipNext: Boolean
            ): Int = currentIndex
            def parseCardIndex(
                index: Int,
                player: Player,
                game: Game,
                tableCard: Card,
                currentPlayerIndex: Int
            ): (Game, Integer) = (game, 0)
            def playCard(
                game: Game,
                playerIdx: Int,
                cardIdx: Int
            ): (Game, Integer) = (game, 0)
            def playCardIfValid(
                card: Card,
                game: Game,
                tableCard: Card,
                currentPlayerIndex: Int
            ): (Game, Integer) = (game, 0)
            def plusN(game: Game, nextPlayerIndex: Int, card: Card, n: Int)
                : Game = game
            def start(p1: Player, p2: Player, gameStates: GameStates): Game =
              Game(List(p1, p2), 0, Nil, None, ActionState.None, TurnState.None)
            def turn(
                card: Card,
                game: Game,
                currentPlayerIndex: Int
            ): (Game, Integer) = (game, 0)
            def wisher(int: Integer): Coulor = Coulor.red
          },
          new PlayCardState {
            def drawCard(game: Game, playerIdx: Int): Game =
              (game)
            def chooseColour(
                game: Game,
                colour: Coulor,
                hand: Card,
                input: Integer
            ): (Card, Game) = (hand, game)
            def dealCardsToHand(
                player: Player,
                deck: List[Card],
                n: Int
            ): (Player, List[Card]) = (player, deck)
            def handleInvalidInput(
                game: Game,
                tableCard: Card,
                message: ActionState
            ): (Game, Integer) = (game, 0)
            def handleTurn(
                game: Game,
                currentPlayerIndex: Int,
                chosenCardIndex: Int
            ): (Game, Integer) = (game, 0)
            def isPlayable(table: Card, hand: Card): Boolean = true
            def nextPlayerIndex(
                currentIndex: Int,
                playerCount: Int,
                skipNext: Boolean
            ): Int = currentIndex
            def parseCardIndex(
                index: Int,
                player: Player,
                game: Game,
                tableCard: Card,
                currentPlayerIndex: Int
            ): (Game, Integer) = (game, 0)
            def playCard(
                game: Game,
                playerIdx: Int,
                cardIdx: Int
            ): (Game, Integer) = (game, 0)
            def playCardIfValid(
                card: Card,
                game: Game,
                tableCard: Card,
                currentPlayerIndex: Int
            ): (Game, Integer) = (game, 0)
            def plusN(game: Game, nextPlayerIndex: Int, card: Card, n: Int)
                : Game = game
            def start(p1: Player, p2: Player, gameStates: GameStates): Game =
              Game(List(p1, p2), 0, Nil, None, ActionState.None, TurnState.None)
            def turn(
                card: Card,
                game: Game,
                currentPlayerIndex: Int
            ): (Game, Integer) = (game, 0)
            def wisher(int: Integer): Coulor = Coulor.red
          },
          new DrawCardState {

            def drawCard(game: Game, playerIdx: Int): Game =
              (game)

            def chooseColour(
                game: Game,
                colour: Coulor,
                hand: Card,
                input: Integer
            ): (Card, Game) = (hand, game)
            def dealCardsToHand(
                player: Player,
                deck: List[Card],
                n: Int
            ): (Player, List[Card]) = (player, deck)
            def handleInvalidInput(
                game: Game,
                tableCard: Card,
                message: ActionState
            ): (Game, Integer) = (game, 0)
            def handleTurn(
                game: Game,
                currentPlayerIndex: Int,
                chosenCardIndex: Int
            ): (Game, Integer) = (game, 0)
            def isPlayable(table: Card, hand: Card): Boolean = true
            def nextPlayerIndex(
                currentIndex: Int,
                playerCount: Int,
                skipNext: Boolean
            ): Int = currentIndex
            def parseCardIndex(
                index: Int,
                player: Player,
                game: Game,
                tableCard: Card,
                currentPlayerIndex: Int
            ): (Game, Integer) = (game, 0)
            def playCard(
                game: Game,
                playerIdx: Int,
                cardIdx: Int
            ): (Game, Integer) = (game, 0)
            def playCardIfValid(
                card: Card,
                game: Game,
                tableCard: Card,
                currentPlayerIndex: Int
            ): (Game, Integer) = (game, 0)
            def plusN(game: Game, nextPlayerIndex: Int, card: Card, n: Int)
                : Game = game
            def start(p1: Player, p2: Player, gameStates: GameStates): Game =
              Game(List(p1, p2), 0, Nil, None, ActionState.None, TurnState.None)
            def turn(
                card: Card,
                game: Game,
                currentPlayerIndex: Int
            ): (Game, Integer) = (game, 0)
            def wisher(int: Integer): Coulor = Coulor.red

          },
          new WishCardState {
            def drawCard(game: Game, playerIdx: Int): Game =
              (game)
            def chooseColour(
                game: Game,
                colour: Coulor,
                hand: Card,
                input: Integer
            ): (Card, Game) = (hand, game)
            def dealCardsToHand(
                player: Player,
                deck: List[Card],
                n: Int
            ): (Player, List[Card]) = (player, deck)
            def handleInvalidInput(
                game: Game,
                tableCard: Card,
                message: ActionState
            ): (Game, Integer) = (game, 0)
            def handleTurn(
                game: Game,
                currentPlayerIndex: Int,
                chosenCardIndex: Int
            ): (Game, Integer) = (game, 0)
            def isPlayable(table: Card, hand: Card): Boolean = true
            def nextPlayerIndex(
                currentIndex: Int,
                playerCount: Int,
                skipNext: Boolean
            ): Int = currentIndex
            def parseCardIndex(
                index: Int,
                player: Player,
                game: Game,
                tableCard: Card,
                currentPlayerIndex: Int
            ): (Game, Integer) = (game, 0)
            def playCard(
                game: Game,
                playerIdx: Int,
                cardIdx: Int
            ): (Game, Integer) = (game, 0)
            def playCardIfValid(
                card: Card,
                game: Game,
                tableCard: Card,
                currentPlayerIndex: Int
            ): (Game, Integer) = (game, 0)
            def plusN(game: Game, nextPlayerIndex: Int, card: Card, n: Int)
                : Game = game
            def start(p1: Player, p2: Player, gameStates: GameStates): Game =
              Game(List(p1, p2), 0, Nil, None, ActionState.None, TurnState.None)
            def turn(
                card: Card,
                game: Game,
                currentPlayerIndex: Int
            ): (Game, Integer) = (game, 0)
            def wisher(int: Integer): Coulor = Coulor.red
          }
        )

    def createGameWithTable(idx: Int = 0): Game = {
      val testCard = card(Coulor.red, Symbol.One)
      val p1 = player("Alice", List(testCard), 0)
      val p2 = player("Bob", List(testCard), 1)
      Game(
        List(p1, p2),
        idx,
        List(testCard),
        Some(testCard),
        ActionState.ChooseCard,
        TurnState.PlayerTurn(p1)
      )
    }
  }

  "ControllerImpl" should {

    "test updateAll method" in {
      val game = Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
      val cmdManager = new CommandManager()
      val fileIO: FileIOInterface = new FileIOInterface {
        override def save(game: Game): Try[Unit] = Success(())
        override def load(): Try[Game] = Success(game)
      }

      val controller =
        new ControllerImpl(game, cmdManager, TestHelper.StubGameStates, fileIO)

      val newGame = Game(
        TestHelper.player("Bob", Nil, 0) :: Nil,
        0,
        Nil,
        None,
        ActionState.None,
        TurnState.None
      )
      val result = controller.updateAll(newGame)
      result shouldBe newGame
      controller.game shouldBe newGame
    }

    "test initloop method" in {
      val game = Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
      val cmdManager = new CommandManager()
      val fileIO: FileIOInterface = new FileIOInterface {
        override def save(game: Game): Try[Unit] = Success(())
        override def load(): Try[Game] = Success(game)
      }

      val controller =
        new ControllerImpl(game, cmdManager, TestHelper.StubGameStates, fileIO)

      val playerInput = new PlayerInput {
        def getInputs(): String = "Alice"
        def getInput(game: Game, input: PlayerInput): Integer = 0
      }
      val startedGame = controller.initloop(playerInput)
      startedGame.player.map(_.name) should contain("Alice")
      startedGame.player.size shouldBe 2
    }

    "test undo with empty stack" in {
      val game = Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
      val cmdManager = new CommandManager()
      val fileIO: FileIOInterface = new FileIOInterface {
        override def save(game: Game): Try[Unit] = Success(())
        override def load(): Try[Game] = Success(game)
      }

      val controller =
        new ControllerImpl(game, cmdManager, TestHelper.StubGameStates, fileIO)
      controller.undo()
      controller.game shouldBe game
    }

    "test undo with command in stack" in {
      val game = TestHelper.createGameWithTable()
      val cmdManager = new CommandManager()
      val fileIO: FileIOInterface = new FileIOInterface {
        override def save(game: Game): Try[Unit] = Success(())
        override def load(): Try[Game] = Success(game)
      }

      val controller =
        new ControllerImpl(game, cmdManager, TestHelper.StubGameStates, fileIO)

      import de.htwg.Uno.controller.Command._
      val cmd = new Command {
        override def execute(game: Game): (Game, Integer) =
          (game.copy(index = 1), 0)
        override def undo(currentGame: Game, previousGame: Game): Game =
          previousGame
      }

      val (newManager, newGame, _) =
        controller.cmdManager.executeCommand(cmd, game)
      controller.cmdManager = newManager
      controller.game = newGame

      controller.undo()
      controller.game.index shouldBe 0
    }

    "test redo with empty stack" in {
      val game = Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
      val cmdManager = new CommandManager()
      val fileIO: FileIOInterface = new FileIOInterface {
        override def save(game: Game): Try[Unit] = Success(())
        override def load(): Try[Game] = Success(game)
      }

      val controller =
        new ControllerImpl(game, cmdManager, TestHelper.StubGameStates, fileIO)
      controller.redo()
      controller.game shouldBe game
    }

    "test redo with command in stack" in {
      val game = TestHelper.createGameWithTable()
      val cmdManager = new CommandManager()
      val fileIO: FileIOInterface = new FileIOInterface {
        override def save(game: Game): Try[Unit] = Success(())
        override def load(): Try[Game] = Success(game)
      }

      val controller =
        new ControllerImpl(game, cmdManager, TestHelper.StubGameStates, fileIO)

      import de.htwg.Uno.controller.Command._
      val cmd = new Command {
        override def execute(game: Game): (Game, Integer) =
          (game.copy(index = 1), 0)
        override def undo(currentGame: Game, previousGame: Game): Game =
          previousGame
      }

      val (newManager, newGame, _) =
        controller.cmdManager.executeCommand(cmd, game)
      controller.cmdManager = newManager
      controller.game = newGame

      controller.undo()
      controller.redo()
      controller.game.index shouldBe 1
    }

    "test saveGame method" in {
      val game = Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
      val cmdManager = new CommandManager()
      var saveCalled = false
      val fileIO: FileIOInterface = new FileIOInterface {
        override def save(game: Game): Try[Unit] = {
          saveCalled = true
          Success(())
        }
        override def load(): Try[Game] = Success(game)
      }

      val controller =
        new ControllerImpl(game, cmdManager, TestHelper.StubGameStates, fileIO)
      val result = controller.saveGame()
      saveCalled shouldBe true
      result shouldBe game
    }

    "test saveGame with failure" in {
      val game = Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
      val cmdManager = new CommandManager()
      val fileIO: FileIOInterface = new FileIOInterface {
        override def save(game: Game): Try[Unit] =
          Failure(new Exception("Save failed"))
        override def load(): Try[Game] = Success(game)
      }

      val controller =
        new ControllerImpl(game, cmdManager, TestHelper.StubGameStates, fileIO)
      val result = controller.saveGame()
      result shouldBe game
    }

    "test loadGame method" in {
      val game = Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
      val loadedGame = Game(
        TestHelper.player("Loaded", Nil, 0) :: Nil,
        1,
        Nil,
        None,
        ActionState.None,
        TurnState.None
      )
      val cmdManager = new CommandManager()
      val fileIO: FileIOInterface = new FileIOInterface {
        override def save(game: Game): Try[Unit] = Success(())
        override def load(): Try[Game] = Success(loadedGame)
      }

      val controller =
        new ControllerImpl(game, cmdManager, TestHelper.StubGameStates, fileIO)
      val result = controller.loadGame()
      result shouldBe loadedGame
      controller.game shouldBe loadedGame
    }

    "test loadGame with failure" in {
      val game = Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
      val cmdManager = new CommandManager()
      val fileIO: FileIOInterface = new FileIOInterface {
        override def save(game: Game): Try[Unit] = Success(())
        override def load(): Try[Game] = Failure(new Exception("Load failed"))
      }

      val controller =
        new ControllerImpl(game, cmdManager, TestHelper.StubGameStates, fileIO)
      val result = controller.loadGame()
      result shouldBe game
      controller.game shouldBe game
    }

    "test gameloop with Hint=1 (ChooseColour)" in {
      val testCard = TestHelper.card(Coulor.red, Symbol.One)
      val game = TestHelper.createGameWithTable()

      val customGameStates = new GameStatesImpl(
        TestHelper.StubGameStates.InitState,
        new PlayCardState {
          def drawCard(game: Game, playerIdx: Int): Game =
            (game)
          def chooseColour(
              game: Game,
              colour: Coulor,
              hand: Card,
              input: Integer
          ): (Card, Game) = (hand, game)
          def dealCardsToHand(
              player: Player,
              deck: List[Card],
              n: Int
          ): (Player, List[Card]) = (player, deck)
          def handleInvalidInput(
              game: Game,
              tableCard: Card,
              message: ActionState
          ): (Game, Integer) = (game, 0)
          def handleTurn(
              game: Game,
              currentPlayerIndex: Int,
              chosenCardIndex: Int
          ): (Game, Integer) = (game, 0)
          def isPlayable(table: Card, hand: Card): Boolean = true
          def nextPlayerIndex(
              currentIndex: Int,
              playerCount: Int,
              skipNext: Boolean
          ): Int = currentIndex
          def parseCardIndex(
              index: Int,
              player: Player,
              game: Game,
              tableCard: Card,
              currentPlayerIndex: Int
          ): (Game, Integer) = (game, 0)
          def playCard(
              game: Game,
              playerIdx: Int,
              cardIdx: Int
          ): (Game, Integer) = (game, 1)
          def playCardIfValid(
              card: Card,
              game: Game,
              tableCard: Card,
              currentPlayerIndex: Int
          ): (Game, Integer) = (game, 0)
          def plusN(
              game: Game,
              nextPlayerIndex: Int,
              card: Card,
              n: Int
          ): Game = game
          def start(p1: Player, p2: Player, gameStates: GameStates): Game =
            Game(List(p1, p2), 0, Nil, None, ActionState.None, TurnState.None)
          def turn(
              card: Card,
              game: Game,
              currentPlayerIndex: Int
          ): (Game, Integer) = (game, 0)
          def wisher(int: Integer): Coulor = Coulor.red
        },
        TestHelper.StubGameStates.drawCardState,
        TestHelper.StubGameStates.WishCardState
      )

      val cmdManager = new CommandManager()
      val fileIO: FileIOInterface = new FileIOInterface {
        override def save(game: Game): Try[Unit] = Success(())
        override def load(): Try[Game] = Success(game)
      }

      val controller =
        new ControllerImpl(game, cmdManager, customGameStates, fileIO)

      var callCount = 0
      val playerInput = new PlayerInput {
        def getInputs(): String = "TestPlayer"
        def getInput(game: Game, input: PlayerInput): Integer = {
          callCount += 1
          if (callCount > 2) throw new RuntimeException("Stop recursion")
          0
        }
      }

      try {
        controller.gameloop(playerInput)
      } catch {
        case _: RuntimeException => // Expected to stop recursion
      }

      callCount should be > 1
    }

    "test gameloop with Hint=3 (DrawCard)" in {
      val game = TestHelper.createGameWithTable()

      val customGameStates = new GameStatesImpl(
        TestHelper.StubGameStates.InitState,
        new PlayCardState {
          def drawCard(game: Game, playerIdx: Int): Game =
            (game)
          def chooseColour(
              game: Game,
              colour: Coulor,
              hand: Card,
              input: Integer
          ): (Card, Game) = (hand, game)
          def dealCardsToHand(
              player: Player,
              deck: List[Card],
              n: Int
          ): (Player, List[Card]) = (player, deck)
          def handleInvalidInput(
              game: Game,
              tableCard: Card,
              message: ActionState
          ): (Game, Integer) = (game, 0)
          def handleTurn(
              game: Game,
              currentPlayerIndex: Int,
              chosenCardIndex: Int
          ): (Game, Integer) = (game, 0)
          def isPlayable(table: Card, hand: Card): Boolean = true
          def nextPlayerIndex(
              currentIndex: Int,
              playerCount: Int,
              skipNext: Boolean
          ): Int = currentIndex
          def parseCardIndex(
              index: Int,
              player: Player,
              game: Game,
              tableCard: Card,
              currentPlayerIndex: Int
          ): (Game, Integer) = (game, 0)
          def playCard(
              game: Game,
              playerIdx: Int,
              cardIdx: Int
          ): (Game, Integer) = (game, 3)
          def playCardIfValid(
              card: Card,
              game: Game,
              tableCard: Card,
              currentPlayerIndex: Int
          ): (Game, Integer) = (game, 0)
          def plusN(
              game: Game,
              nextPlayerIndex: Int,
              card: Card,
              n: Int
          ): Game = game
          def start(p1: Player, p2: Player, gameStates: GameStates): Game =
            Game(List(p1, p2), 0, Nil, None, ActionState.None, TurnState.None)
          def turn(
              card: Card,
              game: Game,
              currentPlayerIndex: Int
          ): (Game, Integer) = (game, 0)
          def wisher(int: Integer): Coulor = Coulor.red
        },
        TestHelper.StubGameStates.drawCardState,
        TestHelper.StubGameStates.WishCardState
      )

      val cmdManager = new CommandManager()
      val fileIO: FileIOInterface = new FileIOInterface {
        override def save(game: Game): Try[Unit] = Success(())
        override def load(): Try[Game] = Success(game)
      }

      val controller =
        new ControllerImpl(game, cmdManager, customGameStates, fileIO)

      var callCount = 0
      val playerInput = new PlayerInput {
        def getInputs(): String = "TestPlayer"
        def getInput(game: Game, input: PlayerInput): Integer = {
          callCount += 1
          if (callCount > 2) throw new RuntimeException("Stop recursion")
          0
        }
      }

      try {
        controller.gameloop(playerInput)
      } catch {
        case _: RuntimeException => // Expected
      }

      callCount should be > 1
    }

    "test gameloop with Hint=6 (NextPlayer)" in {
      val game = TestHelper.createGameWithTable()

      val customGameStates = new GameStatesImpl(
        TestHelper.StubGameStates.InitState,
        new PlayCardState {
          def drawCard(game: Game, playerIdx: Int): Game =
            (game)
          def chooseColour(
              game: Game,
              colour: Coulor,
              hand: Card,
              input: Integer
          ): (Card, Game) = (hand, game)
          def dealCardsToHand(
              player: Player,
              deck: List[Card],
              n: Int
          ): (Player, List[Card]) = (player, deck)
          def handleInvalidInput(
              game: Game,
              tableCard: Card,
              message: ActionState
          ): (Game, Integer) = (game, 0)
          def handleTurn(
              game: Game,
              currentPlayerIndex: Int,
              chosenCardIndex: Int
          ): (Game, Integer) = (game, 0)
          def isPlayable(table: Card, hand: Card): Boolean = true
          def nextPlayerIndex(
              currentIndex: Int,
              playerCount: Int,
              skipNext: Boolean
          ): Int = currentIndex
          def parseCardIndex(
              index: Int,
              player: Player,
              game: Game,
              tableCard: Card,
              currentPlayerIndex: Int
          ): (Game, Integer) = (game, 0)
          def playCard(
              game: Game,
              playerIdx: Int,
              cardIdx: Int
          ): (Game, Integer) = (game, 6)
          def playCardIfValid(
              card: Card,
              game: Game,
              tableCard: Card,
              currentPlayerIndex: Int
          ): (Game, Integer) = (game, 0)
          def plusN(
              game: Game,
              nextPlayerIndex: Int,
              card: Card,
              n: Int
          ): Game = game
          def start(p1: Player, p2: Player, gameStates: GameStates): Game =
            Game(List(p1, p2), 0, Nil, None, ActionState.None, TurnState.None)
          def turn(
              card: Card,
              game: Game,
              currentPlayerIndex: Int
          ): (Game, Integer) = (game, 0)
          def wisher(int: Integer): Coulor = Coulor.red
        },
        TestHelper.StubGameStates.drawCardState,
        TestHelper.StubGameStates.WishCardState
      )

      val cmdManager = new CommandManager()
      val fileIO: FileIOInterface = new FileIOInterface {
        override def save(game: Game): Try[Unit] = Success(())
        override def load(): Try[Game] = Success(game)
      }

      val controller =
        new ControllerImpl(game, cmdManager, customGameStates, fileIO)

      var callCount = 0
      val playerInput = new PlayerInput {
        def getInputs(): String = "TestPlayer"
        def getInput(game: Game, input: PlayerInput): Integer = {
          callCount += 1
          if (callCount > 2) throw new RuntimeException("Stop recursion")
          0
        }
      }

      try {
        controller.gameloop(playerInput)
      } catch {
        case _: RuntimeException => // Expected
      }

      callCount should be > 1
    }

    "test gameloop with default branch (else)" in {
      val game = TestHelper.createGameWithTable()

      val customGameStates = new GameStatesImpl(
        TestHelper.StubGameStates.InitState,
        new PlayCardState {
          def drawCard(game: Game, playerIdx: Int): Game =
            (game)
          def chooseColour(
              game: Game,
              colour: Coulor,
              hand: Card,
              input: Integer
          ): (Card, Game) = (hand, game)
          def dealCardsToHand(
              player: Player,
              deck: List[Card],
              n: Int
          ): (Player, List[Card]) = (player, deck)
          def handleInvalidInput(
              game: Game,
              tableCard: Card,
              message: ActionState
          ): (Game, Integer) = (game, 0)
          def handleTurn(
              game: Game,
              currentPlayerIndex: Int,
              chosenCardIndex: Int
          ): (Game, Integer) = (game, 0)
          def isPlayable(table: Card, hand: Card): Boolean = true
          def nextPlayerIndex(
              currentIndex: Int,
              playerCount: Int,
              skipNext: Boolean
          ): Int = currentIndex
          def parseCardIndex(
              index: Int,
              player: Player,
              game: Game,
              tableCard: Card,
              currentPlayerIndex: Int
          ): (Game, Integer) = (game, 0)
          def playCard(
              game: Game,
              playerIdx: Int,
              cardIdx: Int
          ): (Game, Integer) = (game, 2)
          def playCardIfValid(
              card: Card,
              game: Game,
              tableCard: Card,
              currentPlayerIndex: Int
          ): (Game, Integer) = (game, 0)
          def plusN(
              game: Game,
              nextPlayerIndex: Int,
              card: Card,
              n: Int
          ): Game = game
          def start(p1: Player, p2: Player, gameStates: GameStates): Game =
            Game(List(p1, p2), 0, Nil, None, ActionState.None, TurnState.None)
          def turn(
              card: Card,
              game: Game,
              currentPlayerIndex: Int
          ): (Game, Integer) = (game, 0)
          def wisher(int: Integer): Coulor = Coulor.red
        },
        TestHelper.StubGameStates.drawCardState,
        TestHelper.StubGameStates.WishCardState
      )

      val cmdManager = new CommandManager()
      val fileIO: FileIOInterface = new FileIOInterface {
        override def save(game: Game): Try[Unit] = Success(())
        override def load(): Try[Game] = Success(game)
      }

      val controller =
        new ControllerImpl(game, cmdManager, customGameStates, fileIO)

      var callCount = 0
      val playerInput = new PlayerInput {
        def getInputs(): String = "TestPlayer"
        def getInput(game: Game, input: PlayerInput): Integer = {
          callCount += 1
          if (callCount > 2) throw new RuntimeException("Stop recursion")
          0
        }
      }

      try {
        controller.gameloop(playerInput)
      } catch {
        case _: RuntimeException => // Expected
      }

      callCount should be > 1
    }

    "test all parameters and branches with various hint values" in {
      val game = TestHelper.createGameWithTable()

      var hintSequence = List(0, 4, 7, 8)
      var hintIndex = 0

      val varyingGameStates = new GameStatesImpl(
        TestHelper.StubGameStates.InitState,
        new PlayCardState {

          def drawCard(game: Game, playerIdx: Int): Game =
            (game)
          def chooseColour(
              game: Game,
              colour: Coulor,
              hand: Card,
              input: Integer
          ): (Card, Game) = (hand, game)
          def dealCardsToHand(
              player: Player,
              deck: List[Card],
              n: Int
          ): (Player, List[Card]) = (player, deck)
          def handleInvalidInput(
              game: Game,
              tableCard: Card,
              message: ActionState
          ): (Game, Integer) = (game, 0)
          def handleTurn(
              game: Game,
              currentPlayerIndex: Int,
              chosenCardIndex: Int
          ): (Game, Integer) = (game, 0)
          def isPlayable(table: Card, hand: Card): Boolean = true
          def nextPlayerIndex(
              currentIndex: Int,
              playerCount: Int,
              skipNext: Boolean
          ): Int = currentIndex
          def parseCardIndex(
              index: Int,
              player: Player,
              game: Game,
              tableCard: Card,
              currentPlayerIndex: Int
          ): (Game, Integer) = (game, 0)
          def playCard(
              game: Game,
              playerIdx: Int,
              cardIdx: Int
          ): (Game, Integer) = {
            val hint =
              if (hintIndex < hintSequence.length) hintSequence(hintIndex)
              else 0
            hintIndex += 1
            (game, hint)
          }
          def playCardIfValid(
              card: Card,
              game: Game,
              tableCard: Card,
              currentPlayerIndex: Int
          ): (Game, Integer) = (game, 0)
          def plusN(
              game: Game,
              nextPlayerIndex: Int,
              card: Card,
              n: Int
          ): Game = game
          def start(p1: Player, p2: Player, gameStates: GameStates): Game =
            Game(List(p1, p2), 0, Nil, None, ActionState.None, TurnState.None)
          def turn(
              card: Card,
              game: Game,
              currentPlayerIndex: Int
          ): (Game, Integer) = (game, 0)
          def wisher(int: Integer): Coulor = Coulor.red
        },
        TestHelper.StubGameStates.drawCardState,
        TestHelper.StubGameStates.WishCardState
      )

      val cmdManager = new CommandManager()
      val fileIO: FileIOInterface = new FileIOInterface {
        override def save(game: Game): Try[Unit] = Success(())
        override def load(): Try[Game] = Success(game)
      }

      val controller =
        new ControllerImpl(game, cmdManager, varyingGameStates, fileIO)

      var callCount = 0
      val playerInput = new PlayerInput {
        def getInputs(): String = "TestPlayer"
        def getInput(game: Game, input: PlayerInput): Integer = {
          callCount += 1
          if (callCount > 5) throw new RuntimeException("Stop recursion")
          0
        }
      }

      try {
        controller.gameloop(playerInput)
      } catch {
        case _: RuntimeException => // Expected
      }

      callCount should be > 1
    }

    "test different game states with varying index" in {
      val game1 = TestHelper.createGameWithTable(0)
      val game2 = TestHelper.createGameWithTable(1)

      game1.index shouldBe 0
      game2.index shouldBe 1

      val cmdManager = new CommandManager()
      val fileIO: FileIOInterface = new FileIOInterface {
        override def save(game: Game): Try[Unit] = Success(())
        override def load(): Try[Game] = Success(game1)
      }

      val controller1 =
        new ControllerImpl(game1, cmdManager, TestHelper.StubGameStates, fileIO)
      val controller2 =
        new ControllerImpl(game2, cmdManager, TestHelper.StubGameStates, fileIO)

      controller1.game.index shouldBe 0
      controller2.game.index shouldBe 1
    }

  }
}
