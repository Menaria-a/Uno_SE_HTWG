package de.htwg.Uno
import de.htwg.Uno.Tui
import de.htwg.Uno
import scala.io.StdIn.readLine
import scala.compiletime.ops.boolean
    import de.htwg.Uno.Tui.handrenderer

    def main(args: Array[String]): Unit =
        println(" Ersten Namen eingeben dannach dem Zweiten :")

        val game = deckmacher()
        println(Tui.gamerenderer(game))

        //print("Choose Card by Index :")
        //val index = readLine().toInt
        //val cardToPlay = ersteHand(index)

        //if (isPlayable(tableCards,cardToPlay) == true) {
            //println("gut")
        //} else {
            //println("schlecht")
        //}

        //println("mau")
        //val game2 = zug(cardToPlay,game,0)
        //println(Tui.gamerenderer(game2))

            // ===== MAIN LOOP =====
        var currentPlayerIndex = 0
        var tableCardVar = game.table
        var activeGame = game
        var running = true

        while (running) {
            val currentPlayer = activeGame.player(currentPlayerIndex)
            println(s"\n${currentPlayer.name} ist am Zug.")
            //println(Tui.handrenderer(currentPlayer.hand))
            println(s"Aktuelle Karte: ${tableCardVar.colour} ${tableCardVar.symbol}")
            print("Wähle Kartenindex oder drücke Enter zum Ziehen: ")

            val input = readLine()
            if (input.trim.isEmpty) {
            // Karte ziehen
                val (newPlayer, newDeck) = dealCardsToHand(currentPlayer, activeGame.deck, 1)
                val updatedPlayers = activeGame.player.updated(currentPlayerIndex, newPlayer)
                activeGame = activeGame.copy(player = updatedPlayers, deck = newDeck)
                println(s"${currentPlayer.name} zieht eine Karte.")
            } else {
                try {
                    val index = input.toInt
                    if (index >= 0 && index < currentPlayer.hand.length) {
                        val cardToPlay = currentPlayer.hand(index)
                        if (isPlayable(tableCardVar, cardToPlay)) {
                            println(s"${currentPlayer.name} spielt ${cardToPlay.colour} ${cardToPlay.symbol}")
                            activeGame = zug(cardToPlay, activeGame, currentPlayerIndex)
                            tableCardVar = cardToPlay
                            val updatedPlayer = activeGame.player(currentPlayerIndex)
                            if (updatedPlayer.hand.isEmpty) {
                                println(s"🎉 ${updatedPlayer.name} hat gewonnen! 🎉")
                                running = false
                            }
                        } else {
                            println("❌ Karte kann nicht gespielt werden.")
                        }
                    } else {
                        println("❌ Ungültiger Index.")
                    }
                } catch {
                    case _: NumberFormatException =>
                        println("❌ Bitte eine Zahl eingeben oder Enter drücken.")
                }
            }

            if (running) {
                currentPlayerIndex = (currentPlayerIndex + 1) % activeGame.player.length
            }

            println(Tui.gamerenderer(activeGame))
        }

        println("Spiel beendet.")


            

        
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

    def isPlayable(table : Card, hand : Card) : Boolean =

        if (hand.colour == table.colour || hand.symbol == table.symbol || hand.symbol == Symbol.Wish ) {
            return true
        }
        else {
            return false
            }

    def zug(hand: Card, game: Game, currentPlayerIndex: Int): Game = {
    var players = game.player
    var deck = game.deck
    var tableCard = hand

  // Aktuellen Spieler aktualisieren (Karte aus Hand entfernen)
    val currentPlayer = players(currentPlayerIndex)
    val updatedHand = currentPlayer.hand.filterNot(_ == hand)
    players = players.updated(currentPlayerIndex, currentPlayer.copy(hand = updatedHand))

    hand.symbol match {
    case Symbol.Plus_2 =>
      // Nächster Spieler zieht 2 Karten und überspringt seinen Zug
        val nextIndex = (currentPlayerIndex + 1) % players.length
        val (newPlayer, newDeck) = dealCardsToHand(players(nextIndex), deck, 2)
        players = players.updated(nextIndex, newPlayer)
        deck = newDeck

    case Symbol.Plus_4 =>
      // Nächster Spieler zieht 4 Karten und überspringt seinen Zug
        val nextIndex = (currentPlayerIndex + 1) % players.length
        val (newPlayer, newDeck) = dealCardsToHand(players(nextIndex), deck, 4)
        players = players.updated(nextIndex, newPlayer)
        deck = newDeck
      // TODO: Spieler wählt neue Farbe (hier noch festgelegt als rot z.B.)
        tableCard = hand.copy(colour = Coulor.red)

    case Symbol.Reverse =>
      // Bei 2 Spielern wirkt Reverse wie Skip (nächster Spieler überspringen)
      // Wir behandeln das im main loop beim nächsten Spieler

    case Symbol.Block =>
      // Nächster Spieler wird übersprungen
      // Wir behandeln das im main loop beim nächsten Spieler

    case Symbol.Wish =>
      // Spieler wählt neue Farbe (hier Beispiel: rot)
        tableCard = hand.copy(colour = Coulor.red)

    case _ =>

    }


  // Aktualisiertes Spiel zurückgeben
    game.copy(player = players, deck = deck, table = tableCard)
}
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



























