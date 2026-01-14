package de.htwg.Uno.model.state.Impl
import de.htwg.Uno.model.state.*
import de.htwg.Uno.model.*
import de.htwg.Uno.model.builder.Impl.GameBuilder
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.*

import de.htwg.Uno.model.state.GameState


private[state] case object  WishCardStateImpl extends WishCardState {
    override def chooseColour(game: Game, colour: Coulor, hand: Card, input: Integer ): (Card, Game)= 
        val news = chooseColourForCard(hand, game, input)
        news
    

    def chooseColourForCard(hand: Card, baseGame: Game, input: Integer): (Card, Game) =

        val newGame = baseGame.copy(ActionState = ActionState.ChooseColour)
        val wishedColour = wisher(input)
        val wishedCard = hand.copy(colour = wishedColour, symbol = hand.symbol)
        (wishedCard, newGame.copy(table = wishedCard))


    override def wisher(input: Integer): Coulor =
    input match
        case 2 => Coulor.red
        case 3 => Coulor.green
        case 1 => Coulor.yellow
        case 4   => Coulor.blue



    override def start(p1: Player, p2: Player): Game = 
        val game = Game(Nil,0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)
        game


    override def playCard(game: Game, playerIdx: Int, cardIdx: Int): (Game, Integer) =
        val game = Game(Nil,0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)
        (game, 2)


    override def dealCardsToHand(player: Player, deck: List[Card], n: Int): (Player, List[Card]) =
        (player, deck)
    
    override def nextPlayerIndex(currentIndex: Int, playerCount: Int, skipNext: Boolean): Int =
        currentIndex


    override def parseCardIndex(index: Int,player: Player,game: Game,tableCard: Card,currentPlayerIndex: Int): (Game, Integer) =
        (game, currentPlayerIndex)


    override def turn(card: Card,game: Game,currentPlayerIndex: Int): (Game, Integer) =
        (game, currentPlayerIndex)


    override def isPlayable(table: Card, hand: Card): Boolean =
        true


    override def handleTurn(game: Game,currentPlayerIndex: Int,chosenCardIndex: Int): (Game, Integer) =
        (game, chosenCardIndex)


    override def handleInvalidInput(game: Game,tableCard: Card,message: ActionState): (Game, Integer) =
    (game, 1)


    override def plusN(game: Game, nextPlayerIndex: Int, card: Card, n: Int): Game =
        (game)


    override def playCardIfValid(card: Card,game: Game,tableCard: Card,currentPlayerIndex: Int): (Game, Integer) =
        (game, currentPlayerIndex)
}
