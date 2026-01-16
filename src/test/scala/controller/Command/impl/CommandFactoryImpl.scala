package de.htwg.Uno.controller.Command.Impl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.Uno.model.*
import de.htwg.Uno.controller.PlayerInput
import de.htwg.Uno.controller.Command.*
import de.htwg.Uno.model.state._
import de.htwg.Uno.model.state.Impl.GameStatesImpl
import de.htwg.Uno.model._
import de.htwg.Uno.model.Model._
import de.htwg.Uno.model.Enum._
import de.htwg.Uno.model.state._
import de.htwg.Uno.model.state.Impl.GameStatesImpl

class CommandFactoryImplSpec extends AnyWordSpec with Matchers {

  // ==============================
  // FULL STUB STATES IMPLEMENTATION
  // ==============================
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
          ) = (hand, game)
          def dealCardsToHand(player: Player, deck: List[Card], n: Int) =
            (player, deck)
          def handleInvalidInput(
              game: Game,
              tableCard: Card,
              message: ActionState
          ) = (game, 0)
          def handleTurn(
              game: Game,
              currentPlayerIndex: Int,
              chosenCardIndex: Int
          ) = (game, 0)
          def isPlayable(table: Card, hand: Card) = true
          def nextPlayerIndex(
              currentIndex: Int,
              playerCount: Int,
              skipNext: Boolean
          ) = currentIndex
          def parseCardIndex(
              index: Int,
              player: Player,
              game: Game,
              tableCard: Card,
              currentPlayerIndex: Int
          ) = (game, 0)
          def playCard(game: Game, playerIdx: Int, cardIdx: Int) = (game, 0)
          def playCardIfValid(
              card: Card,
              game: Game,
              tableCard: Card,
              currentPlayerIndex: Int
          ) = (game, 0)
          def plusN(game: Game, nextPlayerIndex: Int, card: Card, n: Int) = game
          def start(p1: Player, p2: Player, gameStates: GameStates) =
            Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
          def turn(card: Card, game: Game, currentPlayerIndex: Int) = (game, 0)
          def wisher(int: Integer) = Coulor.red
        },
        new PlayCardState {

          def drawCard(game: Game, playerIdx: Int): Game =
            (game)
          def chooseColour(
              game: Game,
              colour: Coulor,
              hand: Card,
              input: Integer
          ) = (hand, game)
          def dealCardsToHand(player: Player, deck: List[Card], n: Int) =
            (player, deck)
          def handleInvalidInput(
              game: Game,
              tableCard: Card,
              message: ActionState
          ) = (game, 0)
          def handleTurn(
              game: Game,
              currentPlayerIndex: Int,
              chosenCardIndex: Int
          ) = (game, 0)
          def isPlayable(table: Card, hand: Card) = true
          def nextPlayerIndex(
              currentIndex: Int,
              playerCount: Int,
              skipNext: Boolean
          ) = currentIndex
          def parseCardIndex(
              index: Int,
              player: Player,
              game: Game,
              tableCard: Card,
              currentPlayerIndex: Int
          ) = (game, 0)
          def playCard(game: Game, playerIdx: Int, cardIdx: Int) = (game, 0)
          def playCardIfValid(
              card: Card,
              game: Game,
              tableCard: Card,
              currentPlayerIndex: Int
          ) = (game, 0)
          def plusN(game: Game, nextPlayerIndex: Int, card: Card, n: Int) = game
          def start(p1: Player, p2: Player, gameStates: GameStates) =
            Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
          def turn(card: Card, game: Game, currentPlayerIndex: Int) = (game, 0)
          def wisher(int: Integer) = Coulor.red
        },
        new DrawCardState {

          def drawCard(game: Game, playerIdx: Int): Game =
            (game)
          def chooseColour(
              game: Game,
              colour: Coulor,
              hand: Card,
              input: Integer
          ) = (hand, game)
          def dealCardsToHand(player: Player, deck: List[Card], n: Int) =
            (player, deck)
          def handleInvalidInput(
              game: Game,
              tableCard: Card,
              message: ActionState
          ) = (game, 0)
          def handleTurn(
              game: Game,
              currentPlayerIndex: Int,
              chosenCardIndex: Int
          ) = (game, 0)
          def isPlayable(table: Card, hand: Card) = true
          def nextPlayerIndex(
              currentIndex: Int,
              playerCount: Int,
              skipNext: Boolean
          ) = currentIndex
          def parseCardIndex(
              index: Int,
              player: Player,
              game: Game,
              tableCard: Card,
              currentPlayerIndex: Int
          ) = (game, 0)
          def playCard(game: Game, playerIdx: Int, cardIdx: Int) = (game, 0)
          def playCardIfValid(
              card: Card,
              game: Game,
              tableCard: Card,
              currentPlayerIndex: Int
          ) = (game, 0)
          def plusN(game: Game, nextPlayerIndex: Int, card: Card, n: Int) = game
          def start(p1: Player, p2: Player, gameStates: GameStates) =
            Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
          def turn(card: Card, game: Game, currentPlayerIndex: Int) = (game, 0)
          def wisher(int: Integer) = Coulor.red
        },
        new WishCardState {

          def drawCard(game: Game, playerIdx: Int): Game =
            (game)
          def chooseColour(
              game: Game,
              colour: Coulor,
              hand: Card,
              input: Integer
          ) = (hand, game)
          def dealCardsToHand(player: Player, deck: List[Card], n: Int) =
            (player, deck)
          def handleInvalidInput(
              game: Game,
              tableCard: Card,
              message: ActionState
          ) = (game, 0)
          def handleTurn(
              game: Game,
              currentPlayerIndex: Int,
              chosenCardIndex: Int
          ) = (game, 0)
          def isPlayable(table: Card, hand: Card) = true
          def nextPlayerIndex(
              currentIndex: Int,
              playerCount: Int,
              skipNext: Boolean
          ) = currentIndex
          def parseCardIndex(
              index: Int,
              player: Player,
              game: Game,
              tableCard: Card,
              currentPlayerIndex: Int
          ) = (game, 0)
          def playCard(game: Game, playerIdx: Int, cardIdx: Int) = (game, 0)
          def playCardIfValid(
              card: Card,
              game: Game,
              tableCard: Card,
              currentPlayerIndex: Int
          ) = (game, 0)
          def plusN(game: Game, nextPlayerIndex: Int, card: Card, n: Int) = game
          def start(p1: Player, p2: Player, gameStates: GameStates) =
            Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
          def turn(card: Card, game: Game, currentPlayerIndex: Int) = (game, 0)
          def wisher(int: Integer) = Coulor.red
        }
      )

  val factory = new CommandFactoryImpl(StubGameStates)

  "CommandFactoryImpl" should {

    "create PlayCardCommand" in {
      val cmd = factory.playCard(0, 1, 2)
      cmd shouldBe a[PlayCardCommandT]
    }

    "create DrawCardCommand" in {
      val cmd = factory.draw(0)
      cmd shouldBe a[DrawCardCommandT]
    }

    "create ChooseColourCommand" in {
      val card = Card(Coulor.red, Symbol.One)
      val input = new PlayerInput {
        def getInputs() = "Alice"
        def getInput(game: Game, input: PlayerInput) = 0
      }
      val cmd = factory.chooseColour(card, input)
      cmd shouldBe a[ChooseColourCommandT]
    }

    "create UndoCommand" in {
      val g = Game(Nil, 0, Nil, None, ActionState.None, TurnState.None)
      val cmd = factory.undo(g)
      cmd shouldBe a[UndoCommandT]
    }

  }
}
