package de.htwg.Uno.controller.Command

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.model.*
import de.htwg.Uno.model.Model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.state.*

class DrawCardCommandSpec extends AnyWordSpec with Matchers {

  // ---------- DummyGameState implementing all abstract methods ----------
  object DummyGameState extends WishCardState {

    // ---------- WishCardState methods ----------
    override def chooseColour(
        game: Game,
        colour: Coulor,
        hand: Card,
        input: Integer
    ): (Card, Game) =
      // Dummy: return the same card and game
      (hand, game)

    override def wisher(input: Integer): Coulor =
      Coulor.red

    // ---------- GameState methods ----------
    override def drawCard(game: Game, playerIdx: Int): Game =
      // Dummy: increment the index to simulate a draw
      game.copy(index = game.index + 1)

    override def start(p1: Player, p2: Player, gameStates: GameStates): Game =
      Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)

    override def playCard(
        game: Game,
        playerIdx: Int,
        cardIdx: Int
    ): (Game, Integer) =
      (game, 0)

    override def dealCardsToHand(
        player: Player,
        deck: List[Card],
        n: Int
    ): (Player, List[Card]) =
      (player, deck)

    override def nextPlayerIndex(
        currentIndex: Int,
        playerCount: Int,
        skipNext: Boolean
    ): Int =
      currentIndex

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
    ): (Game, Integer) =
      (game, currentPlayerIndex)

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
    ): Game =
      game

    override def playCardIfValid(
        card: Card,
        game: Game,
        tableCard: Card,
        currentPlayerIndex: Int
    ): (Game, Integer) =
      (game, currentPlayerIndex)
  }

  // ---------- Test Game ----------
  val game: Game = Game(
    player = List(Player("Alice", Nil, 0)),
    index = 0,
    deck = Nil,
    table = None,
    ActionState = ActionState.None,
    TurnState = TurnState.None
  )

  // ---------- Test ----------
  "A DrawCardCommand" should {

    "execute and modify the game correctly" in {
      val command = DrawCardCommand(playerIdx = 0, gameState = DummyGameState)
      val (newGame, actionCode) = command.execute(game)

      // Dummy effect: index increments by 1
      newGame.index shouldBe game.index + 1

      // Action code returned by execute
      actionCode shouldBe 2
    }
  }
}
