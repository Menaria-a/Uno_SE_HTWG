package de.htwg.Uno
import de.htwg.Uno.Tui
import scala.io.StdIn.readLine


object game :
//test

    def main(args: Array[String]): Unit =
        val startGame = initGame()
        gameLoop(startGame, startGame.table, 0)


//initialiesiert das spiel
    def initGame(): Game =
        println("Ersten Namen eingeben, dann den Zweiten:")
        val game = deckmacher()
        println(Tui.gamerenderer(game))
        game

//loop
    def gameLoop(game: Game, tableCard: Card, currentPlayerIndex: Int): Unit =
        val currentPlayer = game.player(currentPlayerIndex)
        println(s"\n${currentPlayer.name} ist am Zug.")
        println(s"Aktuelle Karte: ${tableCard.colour} ${tableCard.symbol}")
        print("Wähle Kartenindex oder drücke Enter zum Ziehen: ")

        val input = readLine()
        val (updatedGame, newTable, skipNext, playerWon) = handleTurn(game, tableCard, currentPlayerIndex, input)
        println(Tui.gamerenderer(updatedGame))

        if (playerWon) {
            println(s"${updatedGame.player(currentPlayerIndex).name} hat gewonnen!")
            println("Spiel beendet.")
        } else {
            val nextIndex = nextPlayerIndex(currentPlayerIndex, updatedGame.player.length, skipNext)
            gameLoop(updatedGame, newTable, nextIndex) // <== Rekursion statt while/var
        }

// spielzug wird gehandelt
    def handleTurn(
        game: Game,
        tableCard: Card,
        currentPlayerIndex: Int,
        input: String
    ): (Game, Card, Boolean, Boolean) = {

        val currentPlayer = game.player(currentPlayerIndex)

        if (input.trim.isEmpty) {
            drawCardIfEmptyInput(currentPlayer, game, tableCard, currentPlayerIndex)
        } else {
        parseCardIndex(input, currentPlayer, game, tableCard, currentPlayerIndex)
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
        println(s"${player.name} zieht eine Karte.")
        (newGame, tableCard, false, false)
    }

    def parseCardIndex(
        input: String,
        player: Player,
        game: Game,
        tableCard: Card,
        currentPlayerIndex: Int
    ): (Game, Card, Boolean, Boolean) = {
    try {
        val index = input.toInt
        if (index >= 0 && index < player.hand.length) {
        val cardToPlay = player.hand(index)
        playCardIfValid(cardToPlay, game, tableCard, currentPlayerIndex)
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
        currentPlayerIndex: Int
    ): (Game, Card, Boolean, Boolean) = {
    if (isPlayable(tableCard, card)) {
        println(s"${game.player(currentPlayerIndex).name} spielt ${card.colour} ${card.symbol}")
        val (newGame, skipNext) = zug(card, game, currentPlayerIndex)
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
        println(message)
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
        hand.colour == table.colour || hand.symbol == table.symbol || hand.symbol == Symbol.Wish


//welche karten art
    def zug(hand: Card, game: Game, currentPlayerIndex: Int): (Game, Boolean) =
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
            val (wishedCard, finalGame) = chooseColourForCard(hand, gameAfterDraw)
            (finalGame, skip)

        case Symbol.Block =>
            println("Nächster Spieler wird übersprungen!")
            val newGame = baseGame.copy(table = hand)
            (newGame, true)

        case Symbol.Wish =>
            val (wishedCard, newGame) = chooseColourForCard(hand, baseGame)
            (newGame, false)

        case Symbol.Reverse =>
            val newGame = baseGame.copy(table = hand)
            (newGame, true)

        case _ =>
            val newGame = baseGame.copy(table = hand)
            (newGame, false)

// deck wird gemacht
    def deckmacher(): Game =
        val deck = for {
        coulor <- Coulor.values
        symbol <- Symbol.values
        } yield Card(coulor, symbol)

        val shuffledDeck = scala.util.Random.shuffle(deck.toList)
        // Spieler mit Beispielkarten füllen (je 5 Karten)
        val ersteHand = shuffledDeck.take(5)
        val zweiteHand = shuffledDeck.slice(5, 10)
        val remainingDeck = shuffledDeck.drop(10)
        val name1 = readLine()
        val name2 = readLine()

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
    def chooseColourForCard(hand: Card, baseGame: Game): (Card, Game) = {
        println("Bitte gewünschte Farbe eingeben: ")
        val wishedColour = wisher(readLine())
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

































