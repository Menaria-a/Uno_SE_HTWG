package de.htwg.Uno.model.state
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.Player
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Model.*
import de.htwg.Uno.model.builder.GameBuilder
import de.htwg.Uno.model.Enum.TurnState.PlayerTurn

case class InitState(p1: Player, p2: Player) extends GameState {

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
        .withTurnState(PlayerTurn(p1)).build()
        finalState



    override def chooseColour(game: Game, colour: Coulor, hand: Card, input: Integer): (Card, Game) =
        val game = Game(Nil,0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)
        val card = Card(Coulor.red, Symbol.One)
        (card, game)

    override def playCard(game: Game, playerIdx: Int, cardIdx: Int): (Game, Integer) =
        val game = Game(Nil,0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)
        (game, 2)


}