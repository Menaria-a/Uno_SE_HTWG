package de.htwg.Uno.model

import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.*
import de.htwg.Uno.model.state.*
import de.htwg.Uno.model.state.Impl.GameStatesImpl
import de.htwg.Uno.model.builder.Impl.GameBuilder
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class GameSpec extends AnyWordSpec with Matchers {

  object TestHelper {

    def player(name: String, hand: List[Card] = List(), index: Int) = Player(name, hand, index)
    def card(colour: Coulor, symbol: Symbol) = Card(colour, symbol)

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
    def start(p1: Player, p2: Player, gameStates: GameStates): Game = Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
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
    def start(p1: Player, p2: Player, gameStates: GameStates): Game = Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
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
    def start(p1: Player, p2: Player, gameStates: GameStates): Game = Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
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
    def start(p1: Player, p2: Player, gameStates: GameStates): Game = Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
    def turn(card: Card, game: Game, currentPlayerIndex: Int): (Game, Integer) = (game, 0)
    def wisher(int: Integer): Coulor = Coulor.red
  }
)
  }

  "A Game" should {

    "correctly convert to a GameBuilder with the same state" in {
      val player1 = TestHelper.player("Alice", List(TestHelper.card(Coulor.red, Symbol.Three)), 0)
      val player2 = TestHelper.player("Bob", List(TestHelper.card(Coulor.blue, Symbol.Reverse)), 1)
      val players = List(player1, player2)

      val deck = List(TestHelper.card(Coulor.green, Symbol.Seven), TestHelper.card(Coulor.yellow, Symbol.Reverse))
      val tableCard = Some(TestHelper.card(Coulor.red, Symbol.One))
      val actionState = ActionState.None
      val turnState = TurnState.None

      val game = Game(players, 0, deck, tableCard, actionState, turnState)

      val builder = game.toBuilder(GameBuilder(TestHelper.StubGameStates))
      val rebuiltGame = builder.build().get

      rebuiltGame.player shouldBe players
      rebuiltGame.index shouldBe 0
      rebuiltGame.deck shouldBe deck
      rebuiltGame.table shouldBe tableCard
      rebuiltGame.ActionState shouldBe actionState
      rebuiltGame.TurnState shouldBe turnState
    }
  }
}











