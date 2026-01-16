package de.htwg.Uno.controller.Command

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.model.*
import de.htwg.Uno.model.Model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.state.*

class PlayCardCommandSpec extends AnyWordSpec with Matchers {

  // ---------- DummyPlayerTurnState implementing all required methods ----------
  object DummyPlayerTurnState extends GameState {

    // Required by WishCardState
    override def chooseColour(
        game: Game,
        colour: Coulor,
        hand: Card,
        input: Integer
    ): (Card, Game) =
      (hand, game)

    override def wisher(input: Integer): Coulor =
      Coulor.red

    // Implement playCard for PlayCardCommand
    override def playCard(
        game: Game,
        playerIdx: Int,
        cardIdx: Int
    ): (Game, Integer) =
      (game.copy(index = game.index + 1), 1) // increment index as dummy effect

    // Stub implementations for all other abstract methods
    override def drawCard(game: Game, playerIdx: Int): Game = game
    override def start(p1: Player, p2: Player, gameStates: GameStates): Game =
      game
    override def dealCardsToHand(
        player: Player,
        deck: List[Card],
        n: Int
    ): (Player, List[Card]) = (player, deck)
    override def nextPlayerIndex(
        currentIndex: Int,
        playerCount: Int,
        skipNext: Boolean
    ): Int = currentIndex
    override def parseCardIndex(
        index: Int,
        player: Player,
        game: Game,
        tableCard: Card,
        currentPlayerIndex: Int
    ): (Game, Integer) =
      (game, currentPlayerIndex)
    override def turn(
        card: Card,
        game: Game,
        currentPlayerIndex: Int
    ): (Game, Integer) = (game, currentPlayerIndex)
    override def isPlayable(table: Card, hand: Card): Boolean = true
    override def handleTurn(
        game: Game,
        currentPlayerIndex: Int,
        chosenCardIndex: Int
    ): (Game, Integer) =
      (game, chosenCardIndex)
    override def handleInvalidInput(
        game: Game,
        tableCard: Card,
        message: ActionState
    ): (Game, Integer) =
      (game, 1)
    override def plusN(
        game: Game,
        nextPlayerIndex: Int,
        card: Card,
        n: Int
    ): Game = game
    override def playCardIfValid(
        card: Card,
        game: Game,
        tableCard: Card,
        currentPlayerIndex: Int
    ): (Game, Integer) =
      (game, currentPlayerIndex)
  }

  // ---------- Dummy GameStates object ----------
  val testGameStates: GameStates = new GameStates {
    override val drawCardState: GameState = DummyPlayerTurnState
    override val InitState: GameState = DummyPlayerTurnState
    override val PlayerTurnState: GameState = DummyPlayerTurnState
    override val WishCardState: GameState = DummyPlayerTurnState
  }

  // ---------- Test Game ----------
  val game: Game = Game(
    player = List(Player("Alice", Nil, 0), Player("Bob", Nil, 0)),
    index = 0,
    deck = Nil,
    table = None,
    ActionState = ActionState.None,
    TurnState = TurnState.None
  )

  // ---------- Tests ----------
  "A PlayCardCommand" should {

    "execute and modify the game via PlayerTurnState" in {
      val command = PlayCardCommand(
        playerIdx = 0,
        cardIdx = 0,
        input = 0,
        gameStates = testGameStates
      )
      val (newGame, actionCode) = command.execute(game)

      // Dummy effect: index increments by 1
      newGame.index shouldBe game.index + 1

      // Dummy action code returned by playCard
      actionCode shouldBe 1

      // ActionState should still be ChooseCard (as in your command)
      newGame.ActionState shouldBe ActionState.ChooseCard
    }

    "undo should return the current game unchanged" in {
      val command = PlayCardCommand(
        playerIdx = 0,
        cardIdx = 0,
        input = 0,
        gameStates = testGameStates
      )
      val undoneGame = command.undo(game, game)

      // Undo does not modify the game
      undoneGame shouldBe game
    }
  }
}
