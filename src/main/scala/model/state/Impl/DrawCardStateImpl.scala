package de.htwg.Uno.model.state.Impl

import de.htwg.Uno.model.ModelInterface.*

private[state] case object DrawCardStateImpl extends GameState {
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
}