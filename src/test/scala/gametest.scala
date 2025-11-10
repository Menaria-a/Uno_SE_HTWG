package de.htwg.Uno

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers._
import de.htwg.Uno.controller.Controler

class UnoSpecAll extends AnyWordSpec with Matchers {

    "The skipNextPlayer function" should {
        "skip exactly one player" in {
            skipNextPlayer(1, 3) shouldBe 0
            skipNextPlayer(0, 4) shouldBe 2
        }
    }

    "The wisher function" should {
        "return the correct wished colour" in {
            wisher("r") shouldBe Coulor.red
            wisher("g") shouldBe Coulor.green
            wisher("y") shouldBe Coulor.yellow
            wisher("b") shouldBe Coulor.blue
        }
    }

    "The nextPlayerIndex function" should {
        "return the correct next player index" in {
            nextPlayerIndex(0, 2, false) shouldBe 1
            nextPlayerIndex(1, 2, false) shouldBe 0
            nextPlayerIndex(0, 3, true) shouldBe 2
        }
    }

    "The isPlayable function" should {
        "detect valid plays correctly" in {
            val table = Card(Coulor.red, Symbol.One)
            val sameColour = Card(Coulor.red, Symbol.Two)
            val sameSymbol = Card(Coulor.blue, Symbol.One)
            val wishCard = Card(Coulor.green, Symbol.Wish)
            val invalid = Card(Coulor.blue, Symbol.Three)

            isPlayable(table, sameColour) shouldBe true
            isPlayable(table, sameSymbol) shouldBe true
            isPlayable(table, wishCard) shouldBe true
            isPlayable(table, invalid) shouldBe false
        }
    }

    "The dealCardsToHand function" should {
        "add the correct number of cards to a player's hand" in {
            val p = Player("Tester", Nil, 0)
            val deck = List(Card(Coulor.red, Symbol.One), Card(Coulor.green, Symbol.Two))
            val (newPlayer, newDeck) = dealCardsToHand(p, deck, 1)
            newPlayer.hand.length shouldBe 1
            newDeck.length shouldBe 1
        }
    }

    "The plusN function" should {
        "add N cards to the next player's hand and skip the turn" in {
            val p1 = Player("A", Nil, 0)
            val p2 = Player("B", Nil, 1)
            val deck = List.fill(5)(Card(Coulor.red, Symbol.One))
            val game = Game(List(p1, p2), deck, Card(Coulor.green, Symbol.Two))

            val (newGame, skipped) = plusN(game, 1, Card(Coulor.red, Symbol.Plus_2), 2)
            newGame.player(1).hand.length shouldBe 2
            skipped shouldBe true
        }
    }

    "The chooseColourForCard function" should {
        "replace the wish card with the chosen colour" in {
      // Eingabe simulieren: rot wird gewünscht
            val input = new java.io.ByteArrayInputStream("r\n".getBytes)
            Console.withIn(input) {
            val game = Game(List(Player("X", Nil, 0)), Nil, Card(Coulor.blue, Symbol.One))
            val (card, newGame) = chooseColourForCard(Card(Coulor.red, Symbol.Wish), game)
            card.colour shouldBe Coulor.red
            newGame.table.colour shouldBe Coulor.red
            }
        }
    }

    "The wisher function with unknown input" should {
        "default to blue if input is invalid" in {
            wisher("x") shouldBe Coulor.blue
        }
    }



    "The skipNextPlayer function with multiple players" should {
        "wrap around correctly" in {
            skipNextPlayer(2, 4) shouldBe 0
        }
    }

    "The handleInvalidInput function" should {
        "return unchanged game and table" in {
            val g = Game(Nil, Nil, Card(Coulor.red, Symbol.One))
            val (ng, table, skip, won) = handleInvalidInput(g, g.table, "Fehler")
            ng shouldBe g
            table shouldBe g.table
            skip shouldBe false
            won shouldBe false
        }
    }

    "The playCardIfValid function" should {
        "play a valid card and reject invalid ones" in {
            val p = Player("A", List(Card(Coulor.red, Symbol.One)), 0)
            val g = Game(List(p), Nil, Card(Coulor.red, Symbol.Two))

            val (g1, table1, skip1, won1) = playCardIfValid(Card(Coulor.red, Symbol.One), g, g.table, 0)
            table1.colour shouldBe Coulor.red
            won1 shouldBe true

            val invalid = Game(List(p), Nil, Card(Coulor.blue, Symbol.Two))
            val (g2, table2, skip2, won2) = playCardIfValid(Card(Coulor.red, Symbol.One), invalid, invalid.table, 0)
            table2 shouldBe invalid.table
            won2 shouldBe false
        }
    }

    "The drawCardIfEmptyInput function" should {
        "draw one card when player presses Enter" in {
            val p = Player("A", Nil, 0)
            val deck = List(Card(Coulor.red, Symbol.One))
            val g = Game(List(p), deck, Card(Coulor.red, Symbol.Two))
            val (ng, table, skip, won) = drawCardIfEmptyInput(p, g, g.table, 0)
            ng.player(0).hand.size shouldBe 1
            table shouldBe g.table
            skip shouldBe false
            won shouldBe false
        }
    }

    "The parseCardIndex function" should {
        "play a card when valid index is chosen" in {
            val p = Player("A", List(Card(Coulor.red, Symbol.One)), 0)
            val g = Game(List(p), Nil, Card(Coulor.red, Symbol.One))
            val (ng, table, skip, won) = parseCardIndex("0", p, g, g.table, 0)
            table.symbol shouldBe Symbol.One
        }

        "handle invalid string input" in {
            val p = Player("A", Nil, 0)
            val g = Game(List(p), Nil, Card(Coulor.red, Symbol.One))
            noException shouldBe thrownBy(parseCardIndex("x", p, g, g.table, 0))
        }

        "handle out of range index gracefully" in {
            val p = Player("A", Nil, 0)
            val g = Game(List(p), Nil, Card(Coulor.red, Symbol.One))
            noException shouldBe thrownBy(parseCardIndex("5", p, g, g.table, 0))
        }
    }

    "The handleTurn function" should {
        "draw when input is empty or play card when valid index" in {
            val p = Player("A", List(Card(Coulor.red, Symbol.One)), 0)
            val g = Game(List(p), List(Card(Coulor.blue, Symbol.Two)), Card(Coulor.red, Symbol.Two))

            val (g1, _, _, _) = handleTurn(g, g.table, 0, "")
            g1.player(0).hand.size shouldBe 2 // drew a card

            val (g2, table, _, _) = handleTurn(g, g.table, 0, "0")
            table.colour shouldBe Coulor.red
        }
    }

    "The deckmacher function" should {
        "create a valid new game with two players" in {
            val input = new java.io.ByteArrayInputStream("A\nB\n".getBytes)
            Console.withIn(input) {
                val g = deckmacher()
                g.player.length shouldBe 2
                g.player.head.hand.length shouldBe 5
                g.deck.length should be > 0
            }
        }
    }

    "The initGame function" should {
        "initialize a game successfully" in {
            val input = new java.io.ByteArrayInputStream("A\nB\n".getBytes)
            Console.withIn(input) {
                val g = initGame()
                g.player.size shouldBe 2
            }
        }
    }






    "zug function" should {
        "handle Plus_2 correctly" in {
            val p1 = Player("A", Nil, 0)
            val p2 = Player("B", Nil, 1)
            val game = Game(List(p1, p2), List.fill(5)(Card(Coulor.red, Symbol.One)), Card(Coulor.red, Symbol.Two))
    
            val (g1, skip1) = zug(Card(Coulor.red, Symbol.Plus_2), game, 0)
            g1.player(1).hand.length shouldBe 2
            skip1 shouldBe true
        }

        "handle Plus_4 correctly" in {
            val p1 = Player("A", List(Card(Coulor.red, Symbol.Plus_4)), 0)
            val p2 = Player("B", Nil, 1)
            val game = Game(List(p1, p2), List.fill(5)(Card(Coulor.green, Symbol.One)), Card(Coulor.blue, Symbol.One))

  // Simuliere die Benutzereingabe für die gewünschte Farbe "r" (rot)
            val input = new java.io.ByteArrayInputStream("r\n".getBytes)
            Console.withIn(input) {
            val (newGame, skip) = zug(Card(Coulor.red, Symbol.Plus_4), game, 0)
            newGame.player(1).hand.length shouldBe 4   // Nächster Spieler zieht 4 Karten
            newGame.table.colour shouldBe Coulor.red   // Gewünschte Farbe gesetzt
            skip shouldBe true                         // Nächster Spieler wird übersprungen
        }
    }

        "handle Block correctly" in {
            val p1 = Player("A", Nil, 0)
            val p2 = Player("B", Nil, 1)
            val game = Game(List(p1, p2), Nil, Card(Coulor.red, Symbol.Two))
    
            val (g1, skip1) = zug(Card(Coulor.red, Symbol.Block), game, 0)
            skip1 shouldBe true
        }

        "handle Reverse correctly" in {
            val p1 = Player("A", Nil, 0)
            val p2 = Player("B", Nil, 1)
            val game = Game(List(p1, p2), Nil, Card(Coulor.red, Symbol.Two))
    
            val (g1, skip1) = zug(Card(Coulor.red, Symbol.Reverse), game, 0)
            skip1 shouldBe true
        }

        "handle Wish correctly" in {
            val p1 = Player("A", Nil, 0)
            val game = Game(List(p1), Nil, Card(Coulor.blue, Symbol.One))

  // Simuliere die Benutzereingabe "r" für Rot
            val input = new java.io.ByteArrayInputStream("r\n".getBytes)
            Console.withIn(input) {
            val (card, g1) = chooseColourForCard(Card(Coulor.red, Symbol.Wish), game)
            card.colour shouldBe Coulor.red
            g1.table.colour shouldBe Coulor.red
            }
        }
    }

    "zug function with Wish card" should {
        "set the wished colour correctly and skip the next player if needed" in {
    // Spieler vorbereiten
            val p1 = Player("A", List(Card(Coulor.red, Symbol.Wish)), 0)
            val p2 = Player("B", Nil, 1)

            val game = Game(
            List(p1, p2),
            List(Card(Coulor.green, Symbol.One)), // Deck
            Card(Coulor.blue, Symbol.Two)        // Startkarte
            )

    // Simulierte Eingabe: Spieler wählt "r" = rot
            val input = new java.io.ByteArrayInputStream("r\n".getBytes)
            Console.withIn(input) {
            val (newGame, skip) = zug(Card(Coulor.red, Symbol.Wish), game, 0)

      // Table sollte jetzt rot sein
            newGame.table.colour shouldBe Coulor.red

      // Spieler 1 hat keine Karten mehr
            newGame.player(0).hand shouldBe empty

      // Nächster Spieler sollte nicht übersprungen (je nach Regel)
            skip shouldBe false
            }
        }
    } 
    





}
