package de.htwg.Uno.controller
import de.htwg.Uno.modell.Model
import de.htwg.Uno.modell.Model.Card
import de.htwg.Uno.modell.Model.Coulor
import de.htwg.Uno.modell.Model.Symbol
import de.htwg.Uno.controller.PlayerInput
import de.htwg.Uno.modell.Model.Game
import de.htwg.Uno.modell.Model.Player
import de.htwg.Uno.util.Observable
import scala.util.Try




case class Controler() extends Observable {

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

//test
    //override def update: Unit = ()

//    def main(args: Array[String]): Unit =
//        val startGame = initGame()
//        gameLoop(startGame, startGame.table, 0)


//initialiesiert das spiel
    def initGame(inputs: PlayerInput): Game =
        //_status = "Namen eingeben "
        //notifyObservers
        //val name1 = inputs.getInput()
        //val name2 = inputs.getInput()
        val newGame = deckmacher(inputs)
        _game = newGame               //  Das Spiel intern speichern
        //_status = "Game initialized"  // optionaler Status
        notifyObservers
        newGame

//loop
    def gameLoop(game: Game, tableCard: Card, currentPlayerIndex: Int, inputs: PlayerInput): Unit =
        val currentPlayer = game.player(currentPlayerIndex)
        _person = s"\n${currentPlayer.name} ist am Zug."
        notifyObservers
        //println(s"\n${currentPlayer.name} ist am Zug.")
        //println(s"Aktuelle Karte: ${tableCard.colour} ${tableCard.symbol}")
        _status = "Wähle Kartenindex oder drücke Enter zum Ziehen: "
        notifyObservers
        //print("Wähle Kartenindex oder drücke Enter zum Ziehen: ")

        val input = inputs.getInput()
        val (updatedGame, newTable, skipNext, playerWon) = handleTurn(game, tableCard, currentPlayerIndex, input, inputs)
        //println(Tui.gamerenderer(updatedGame))
        _game = updatedGame
        notifyObservers

        if (playerWon) {
            val winner = updatedGame.player(currentPlayerIndex)
            _status = s"${winner.name} hat gewonnen!"
            notifyObservers
            //println(s"${winner.name} hat gewonnen!")
            //println("Spiel beendet.")
        } else {
            val nextIndex = nextPlayerIndex(currentPlayerIndex, updatedGame.player.length, skipNext)
            gameLoop(updatedGame, newTable, nextIndex, inputs) // <== Rekursion statt while/var
        }

// spielzug wird gehandelt
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

// karte ziehen
    def drawCardIfEmptyInput(
        player: Player,
        game: Game,
        tableCard: Card,
        currentPlayerIndex: Int
    ): (Game, Card, Boolean, Boolean) = {
        val (newPlayer, newDeck) = dealCardsToHand(player, game.deck, 1)
        val updatedPlayers = game.player.updated(currentPlayerIndex, newPlayer)
        val newGame = game.copy(player = updatedPlayers, deck = newDeck)
        //println(s"${player.name} zieht eine Karte.")
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
        //println(s"${game.player(currentPlayerIndex).name} spielt ${card.colour} ${card.symbol}")
        val (newGame, skipNext) = zug(card, game, currentPlayerIndex, inputs)
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
        //println(message)
        (game, tableCard, false, false)
    }

// spielerwechsel
    def nextPlayerIndex(currentIndex: Int, playerCount: Int, skipNext: Boolean): Int =
        if skipNext then skipNextPlayer(currentIndex, playerCount)
        else (currentIndex + 1) % playerCount

//karten auf hände austeilen
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

//kann man die karte spiele
    def isPlayable(table: Card, hand: Card): Boolean =
        hand.colour == table.colour || hand.symbol == table.symbol || hand.symbol == Symbol.Wish || hand.symbol == Symbol.Plus_4


//welche karten art
    def zug(hand: Card, game: Game, currentPlayerIndex: Int, inputs: PlayerInput): (Game, Boolean) =
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
            //println("Nächster Spieler wird übersprungen!")
            //notifyObservers
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

// deck wird gemacht
    def deckmacher(inputs : PlayerInput): Game =
        val deck = for {
        coulor <- Coulor.values
        symbol <- Symbol.values
        } yield Card(coulor, symbol)

        val shuffledDeck = scala.util.Random.shuffle(deck.toList)
        // Spieler mit Beispielkarten füllen (je 5 Karten)
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

// wunsch wird überpruft
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

//gewüschte farbe wird übertragen
    def chooseColourForCard(hand: Card, baseGame: Game,  inputs: PlayerInput): (Card, Game) = {
        //println("Bitte gewünschte Farbe eingeben: ")
        _status = "Bitte gewünschte Farbe eingeben: "
        notifyObservers
        val wishedColour = wisher(inputs.getInput())
        val wishedCard = hand.copy(colour = wishedColour)
        (wishedCard, baseGame.copy(table = wishedCard))
    }

// n karten werden addiert
    def plusN(game: Game, nextPlayerIndex: Int, hand: Card, n: Int): (Game, Boolean) = {
        val (newPlayer, newDeck) = dealCardsToHand(game.player(nextPlayerIndex), game.deck, n)
        val newPlayers = game.player.updated(nextPlayerIndex, newPlayer)
        val newGame = game.copy(player = newPlayers, deck = newDeck, table = hand)
        (newGame, true) // Nächster Spieler wird übersprungen
}
// zum nächsten speiler skippen
    def skipNextPlayer(currentIndex: Int, playerCount: Int): Int =
    (currentIndex + 2) % playerCount

    def currentPlayerFinder(game : Game, currentPlayerIndex : Int ): Player = 
        val currentPlayer = game.player(currentPlayerIndex)
        currentPlayer
}



































