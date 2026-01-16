package de.htwg.Uno.controller.Command

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.Uno.model.*
import de.htwg.Uno.model.Model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.state.*
import de.htwg.Uno.controller.PlayerInput
import de.htwg.Uno.model.state.Impl.WishCardStateImpl

class ChooseColourCommandSpec extends AnyWordSpec with Matchers {

  // ---------- PlayerInput stub ----------
  val testInput: PlayerInput = new PlayerInput {
    override def getInput(game: Game, input: PlayerInput): Integer =
      2 // maps to red
    override def getInputs(): String = "red"
  }

  // ---------- DummyState implementing all abstract methods ----------
  object DummyState extends WishCardState {
    // Methods required by WishCardState
    override def chooseColour(
        game: Game,
        colour: Coulor,
        hand: Card,
        input: Integer
    ): (Card, Game) =
      (hand, game)
    override def wisher(input: Integer): Coulor = Coulor.red

    override def drawCard(game: Game, playerIdx: Int): Game =
      (game)

    // Methods required by GameState
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
    ): Game = game
    override def playCardIfValid(
        card: Card,
        game: Game,
        tableCard: Card,
        currentPlayerIndex: Int
    ): (Game, Integer) =
      (game, currentPlayerIndex)
  }

  // ---------- GameStates stub ----------
  val testGameStates: GameStates = new GameStates {
    override val WishCardState: GameState = WishCardStateImpl
    override val drawCardState: GameState = DummyState
    override val InitState: GameState = DummyState
    override val PlayerTurnState: GameState = DummyState
  }

  // ---------- Test Card ----------
  val card: Card = Card(Coulor.red, Symbol.One)

  // ---------- Test Game ----------
  val game: Game = Game(
    player = List(Player("Alice", Nil, 0)),
    index = 0,
    deck = Nil,
    table = Some(card),
    ActionState = ActionState.None,
    TurnState = TurnState.None
  )

  // ---------- Command ----------
  val command = ChooseColourCommand(card, testInput, testGameStates)

  // ---------- Test ----------
  "A ChooseColourCommand" should {
    "execute and return a modified game and action code 2" in {
      val (newGame, actionCode) = command.execute(game)

      // Table card should have the wished colour (red)
      newGame.table.get.colour shouldBe Coulor.red

      // Game's ActionState should be ChooseColour
      newGame.ActionState shouldBe ActionState.ChooseColour

      // The action code returned by execute
      actionCode shouldBe 2
    }
  }
}
