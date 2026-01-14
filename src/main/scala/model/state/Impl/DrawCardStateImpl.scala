package de.htwg.Uno.model.state.Impl

import de.htwg.Uno.model.state.*
import de.htwg.Uno.model.*
import de.htwg.Uno.model.builder.Impl.GameBuilder
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Model.*

private[state] case object DrawCardStateImpl extends DrawCardState {
    override def drawCard(game: Game, playerIdx: Int): Game =
        val player = game.player(playerIdx)
        val (newPlayer, newDeck) = dealCardsToHand(player, game.deck, 1)
        val newDex = nextPlayerIndex(playerIdx, 2, false)
        val updatedPlayers = game.player.updated(playerIdx, newPlayer)
        game.copy(player = updatedPlayers, deck = newDeck, index = newDex)


    def dealCardsToHand(player: Player, deck: List[Card], n: Int): (Player, List[Card]) =
        val (dealtCards, remainingDeck) = deck.splitAt(n)
        (player.copy(hand = player.hand ::: dealtCards), remainingDeck)


    def nextPlayerIndex(currentIndex: Int, playerCount: Int, skipNext: Boolean): Int =
        if (skipNext) {
            val mau =(currentIndex + 2) % 2
            mau} else {val mau = (currentIndex + 1) % 2
            mau}
    

    override def start(p1: Player, p2: Player): Game = 
        val game = Game(Nil,0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)
        game

    override def chooseColour(game: Game, colour: Coulor, hand: Card, input: Integer): (Card, Game) =
        val game = Game(Nil,0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)
        val card = Card(Coulor.red, Symbol.One)
        (card, game)

    override def playCard(game: Game, playerIdx: Int, cardIdx: Int): (Game, Integer) =
        val game = Game(Nil,0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)
        (game, 2)


    override def wisher(int: Integer)=
        Coulor.blue


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

