package de.htwg.Uno.controller
import de.htwg.Uno.model.Model
import de.htwg.Uno.model.Model.Card
import de.htwg.Uno.model.Model.Coulor
import de.htwg.Uno.model.Model.Symbol
import de.htwg.Uno.controller.PlayerInput
import de.htwg.Uno.model.Model.Game
import de.htwg.Uno.model.Model.Player
import de.htwg.Uno.util.Observable
import scala.util.Try

case class Controller() extends Observable {

    private var _status = ""
    private var _person = ""
    private var _game = Game(Nil, Nil, Card(Coulor.red, Symbol.One))

    def game: Game = _game
    def status: String = _status
    def person: String = _person

    def setGameAndNotify(gs: Game, st: String, pe: String): Unit =
        _game = gs
        _status = st
        _person = pe
        notifyObservers
    def safeToInt(s: String): Option[Int] =
        Try(s.trim.toInt).toOption

    def initGame(inputs: PlayerInput): Game =

        val newGame = deckmaker(inputs)
        _game = newGame              
        notifyObservers
        newGame

    def gameLoop(game: Game, tableCard: Card, currentPlayerIndex: Int, inputs: PlayerInput): Unit =
        val currentPlayer = game.player(currentPlayerIndex)
        _person = s"\n${currentPlayer.name} ist am Zug."
        notifyObservers
        _status = "Wähle Kartenindex oder drücke Enter zum Ziehen: "
        notifyObservers

        val input = inputs.getInput()
        val (updatedGame, newTable, skipNext, playerWon) = handleTurn(game, tableCard, currentPlayerIndex, input, inputs)

        _game = updatedGame
        notifyObservers

        if (playerWon) {
            val winner = updatedGame.player(currentPlayerIndex)
            _status = s"${winner.name} hat gewonnen!"
            notifyObservers

        } else {
            val nextIndex = nextPlayerIndex(currentPlayerIndex, updatedGame.player.length, skipNext)
            gameLoop(updatedGame, newTable, nextIndex, inputs) // <== Rekursion statt while/var
        }

    def handleTurn(
        game: Game,
        tableCard: Card,
        currentPlayerIndex: Int,
        input: String,
        inputs: PlayerInput
    ): (Game, Card, Boolean, Boolean) = {

        val currentPlayer = game.player(currentPlayerIndex)

        if (input.trim.isEmpty) {
            drawCardIfEmptyInput(currentPlayer, game, tableCard, currentPlayerIndex)
        } else {
        parseCardIndex(input, currentPlayer, game, tableCard, currentPlayerIndex, inputs)
        }
}

    def drawCardIfEmptyInput(
        player: Player,
        game: Game,
        tableCard: Card,
        currentPlayerIndex: Int
    ): (Game, Card, Boolean, Boolean) = {
        val (newPlayer, newDeck) = dealCardsToHand(player, game.deck, 1)
        val updatedPlayers = game.player.updated(currentPlayerIndex, newPlayer)
        val newGame = game.copy(player = updatedPlayers, deck = newDeck)

        (newGame, tableCard, false, false)
    }

    def parseCardIndex(
        input: String,
        player: Player,
        game: Game,
        tableCard: Card,
        currentPlayerIndex: Int,
        inputs: PlayerInput
    ): (Game, Card, Boolean, Boolean) = {
    try {
        val index = input.toInt
        if (index >= 0 && index < player.hand.length) {
        val cardToPlay = player.hand(index)
        playCardIfValid(cardToPlay, game, tableCard, currentPlayerIndex, inputs)
        } else handleInvalidInput(game, tableCard, "Ungültiger Index.")
        } catch {
            case _: NumberFormatException =>
            handleInvalidInput(game, tableCard, "Bitte eine Zahl eingeben oder Enter drücken.")
        }
    }      

    def playCardIfValid(
        card: Card,
        game: Game,
        tableCard: Card,
        currentPlayerIndex: Int,
        inputs: PlayerInput
    ): (Game, Card, Boolean, Boolean) = {
    if (isPlayable(tableCard, card)) {

        val (newGame, skipNext) = turn(card, game, currentPlayerIndex, inputs)
        val playerWon = newGame.player(currentPlayerIndex).hand.isEmpty
        (newGame, newGame.table, skipNext, playerWon)
    } else {
        handleInvalidInput(game, tableCard, "Karte kann nicht gespielt werden.")
    }
}

    def handleInvalidInput(
        game: Game,
        tableCard: Card,
        message: String
    ): (Game, Card, Boolean, Boolean) = {
        _status = message
        notifyObservers

        (game, tableCard, false, false)
    }

    def nextPlayerIndex(currentIndex: Int, playerCount: Int, skipNext: Boolean): Int =
        if skipNext then skipNextPlayer(currentIndex, playerCount)
        else (currentIndex + 1) % playerCount

    def dealCardsToHand(
        player: Player,
        deck: List[Card],
        n: Int
        ): (Player, List[Card]) = {
        val (dealtCards, remainingDeck) = deck.splitAt(n)
        val newHand = player.hand ::: dealtCards
        val updatedPlayer = player.copy(hand = newHand)
        (updatedPlayer, remainingDeck)
        }   

    def isPlayable(table: Card, hand: Card): Boolean =
        hand.colour == table.colour || hand.symbol == table.symbol || hand.symbol == Symbol.Wish || hand.symbol == Symbol.Plus_4

    def turn(hand: Card, game: Game, currentPlayerIndex: Int, inputs: PlayerInput): (Game, Boolean) =
        val currentPlayer = game.player(currentPlayerIndex)
        val updatedHand = currentPlayer.hand.filterNot(_ == hand)
        val updatedPlayerList = game.player.updated(currentPlayerIndex, currentPlayer.copy(hand = updatedHand))

        val baseGame = game.copy(player = updatedPlayerList)
        val nextIndex = (currentPlayerIndex + 1) % game.player.length

        hand.symbol match
        case Symbol.Plus_2 =>
            plusN(baseGame, nextIndex, hand, 2)

        case Symbol.Plus_4 =>
            val (gameAfterDraw, skip) = plusN(baseGame, nextIndex, hand, 4)
            val (wishedCard, finalGame) = chooseColourForCard(hand, gameAfterDraw, inputs)
            (finalGame, skip)

        case Symbol.Block =>

            val newGame = baseGame.copy(table = hand)
            (newGame, true)

        case Symbol.Wish =>
            val (wishedCard, newGame) = chooseColourForCard(hand, baseGame, inputs)
            (newGame, false)

        case Symbol.Reverse =>
            val newGame = baseGame.copy(table = hand)
            (newGame, true)

        case _ =>
            val newGame = baseGame.copy(table = hand)
            (newGame, false)

    def deckmaker(inputs : PlayerInput): Game =
        val deck = for {
        coulor <- Coulor.values
        symbol <- Symbol.values
        } yield Card(coulor, symbol)

        val shuffledDeck = scala.util.Random.shuffle(deck.toList)

        val ersteHand = shuffledDeck.take(5)
        val zweiteHand = shuffledDeck.slice(5, 10)
        val remainingDeck = shuffledDeck.drop(10)
        val name1 = inputs.getInput()
        val name2 = inputs.getInput()

        val players = List(
        Player(name1, ersteHand, 0),
        Player(name2, zweiteHand,1)
        )
        val tableCards = shuffledDeck(10)
        Game(players,remainingDeck,tableCards)


    def wisher(input: String): Coulor =

        if(input == "r") {
            Coulor.red
        } else if(input == "g") {
            Coulor.green
        } else if(input == "y") {
            Coulor.yellow
        } else {
            Coulor.blue
        }

    def chooseColourForCard(hand: Card, baseGame: Game,  inputs: PlayerInput): (Card, Game) = {

        _status = "Bitte gewünschte Farbe eingeben: "
        notifyObservers
        val wishedColour = wisher(inputs.getInput())
        val wishedCard = hand.copy(colour = wishedColour)
        (wishedCard, baseGame.copy(table = wishedCard))
    }

    def plusN(game: Game, nextPlayerIndex: Int, hand: Card, n: Int): (Game, Boolean) = {
        val (newPlayer, newDeck) = dealCardsToHand(game.player(nextPlayerIndex), game.deck, n)
        val newPlayers = game.player.updated(nextPlayerIndex, newPlayer)
        val newGame = game.copy(player = newPlayers, deck = newDeck, table = hand)
        (newGame, true) 
        }

    def skipNextPlayer(currentIndex: Int, playerCount: Int): Int =
    (currentIndex + 2) % playerCount

    def currentPlayerFinder(game : Game, currentPlayerIndex : Int ): Player = 
        val currentPlayer = game.player(currentPlayerIndex)
        currentPlayer
}



































