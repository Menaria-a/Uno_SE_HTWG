package de.htwg.Uno.model.state.Impl

import de.htwg.Uno.model.state.GameState
import de.htwg.Uno.model.ModelInterface.*



private [state] case object InitStateImpl extends GameState {

    override def start(p1: Player, p2: Player): Game = deckmaker(p1, p2)


    def deckmaker(p1: Player, p2: Player): Game =
        val deck = for {
        coulor <- Coulor.values
        symbol <- Symbol.values
        } yield Card(coulor, symbol)
        val shuffledDeck = scala.util.Random.shuffle(deck.toList)

        val ersteHand = shuffledDeck.take(5)
        val zweiteHand = shuffledDeck.slice(5, 10)
        val remainingDeck = shuffledDeck.drop(10)


        val players = List(
        p1.copy( hand = ersteHand, index = 0),
        p2.copy(hand =  zweiteHand, index = 1)
        )
        val tableCards = shuffledDeck(10)
        val finalState = GameBuilder().withPlayers(players)
        .withIndex(0)
        .withDeck(remainingDeck)
        .withTable(tableCards)
        .withActionState(ActionState.ChooseCard)
        .withTurnState(TurnState.PlayerTurn(p1)).build()
        finalState



    override def chooseColour(game: Game, colour: Coulor, hand: Card, input: Integer): (Card, Game) =
        val game = Game(Nil,0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)
        val card = Card(Coulor.red, Symbol.One)
        (card, game)

    override def playCard(game: Game, playerIdx: Int, cardIdx: Int): (Game, Integer) =
        val game = Game(Nil,0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)
        (game, 2)

    override def wisher(int: Integer)=
        Coulor.blue


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