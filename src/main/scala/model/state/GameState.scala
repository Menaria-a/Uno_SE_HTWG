package de.htwg.Uno.model.state
import de.htwg.Uno.model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.*


trait GameState:
    def start( p1: Player, p2: Player, gameStates: GameStates): Game 
    def drawCard(game: Game, playerIdx: Int): Game 
    def playCard(game: Game, playerIdx: Int, cardIdx: Int): (Game, Integer)
    def chooseColour(game: Game, colour: Coulor, hand: Card, input: Integer ): (Card, Game)
    def wisher(int: Integer): Coulor
    def dealCardsToHand(player: Player, deck: List[Card], n: Int): (Player, List[Card])
    def nextPlayerIndex(currentIndex: Int, playerCount: Int, skipNext: Boolean): Int 
    def parseCardIndex(index: Int,player: Player,game: Game,tableCard: Card,currentPlayerIndex: Int): (Game, Integer) 
    def turn(card: Card,game: Game,currentPlayerIndex: Int): (Game, Integer) 
    def isPlayable(table: Card, hand: Card): Boolean 
    def handleTurn(game: Game,currentPlayerIndex: Int,chosenCardIndex: Int): (Game, Integer) 
    def handleInvalidInput(game: Game,tableCard: Card,message: ActionState): (Game, Integer) 
    def plusN(game: Game, nextPlayerIndex: Int, card: Card, n: Int): Game
    def playCardIfValid(card: Card,game: Game,tableCard: Card,currentPlayerIndex: Int): (Game, Integer) 


trait InitState extends GameState

trait PlayCardState extends GameState

trait WishCardState extends GameState

trait DrawCardState extends GameState



