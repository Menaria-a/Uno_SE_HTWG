package de.htwg.Uno

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers._
import de.htwg.Uno.controller.Controler
import de.htwg.Uno.modell.Model.Coulor
import de.htwg.Uno.modell.Model.Symbol
import de.htwg.Uno.modell.Model.Card
import de.htwg.Uno.modell.Model.Game
import de.htwg.Uno.modell.Model.Player
import de.htwg.Uno.controller.PlayerInput
import de.htwg.Uno.util.Observable
import de.htwg.Uno.util.Observer
import de.htwg.Uno.aView.Tui




class UnoSpecAll extends AnyWordSpec with Matchers {

    val controller = new Controler()
    val TuiInstance = new Tui(controller: Controler)


    "The skipNextPlayer function" should {
        "skip exactly one player" in {
            controller.skipNextPlayer(1, 3) shouldBe 0
            controller.skipNextPlayer(0, 4) shouldBe 2
        }
    }

    "The wisher function" should {
        "return the correct wished colour" in {
            controller.wisher("r") shouldBe Coulor.red
            controller.wisher("g") shouldBe Coulor.green
            controller.wisher("y") shouldBe Coulor.yellow
            controller.wisher("b") shouldBe Coulor.blue
        }
    }

    "The nextPlayerIndex function" should {
        "return the correct next player index" in {
            controller.nextPlayerIndex(0, 2, false) shouldBe 1
            controller.nextPlayerIndex(1, 2, false) shouldBe 0
            controller.nextPlayerIndex(0, 3, true) shouldBe 2
        }
    }

    "The isPlayable function" should {
        "detect valid plays correctly" in {
            val table = Card(Coulor.red, Symbol.One)
            val sameColour = Card(Coulor.red, Symbol.Two)
            val sameSymbol = Card(Coulor.blue, Symbol.One)
            val wishCard = Card(Coulor.green, Symbol.Wish)
            val invalid = Card(Coulor.blue, Symbol.Three)

            controller.isPlayable(table, sameColour) shouldBe true
            controller.isPlayable(table, sameSymbol) shouldBe true
            controller.isPlayable(table, wishCard) shouldBe true
            controller.isPlayable(table, invalid) shouldBe false
        }
    }

    "The dealCardsToHand function" should {
        "add the correct number of cards to a player's hand" in {
            val p = Player("Tester", Nil, 0)
            val deck = List(Card(Coulor.red, Symbol.One), Card(Coulor.green, Symbol.Two))
            val (newPlayer, newDeck) = controller.dealCardsToHand(p, deck, 1)
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

            val (newGame, skipped) = controller.plusN(game, 1, Card(Coulor.red, Symbol.Plus_2), 2)
            newGame.player(1).hand.length shouldBe 2
            skipped shouldBe true
        }
    }

    "The chooseColourForCard function" should {
        "replace the wish card with the chosen colour" in {
      // --- Arrange ---
      // Eingabe simulieren (z. B. „r“ für Rot)
            val input = new java.io.ByteArrayInputStream("r\n".getBytes)
            Console.withIn(input) {
                val game = Game(List(Player("X", Nil, 0)), Nil, Card(Coulor.blue, Symbol.One))

        // Fake-PlayerInput, der „r“ liefert
                val fakeInput = new PlayerInput {
                override def getInput(): String = "r"
                }

        // --- Act ---
                val (card, newGame) = controller.chooseColourForCard(
                Card(Coulor.red, Symbol.Wish),
                game,
                fakeInput
                )

        // --- Assert ---
        card.colour shouldBe Coulor.red
        newGame.table.colour shouldBe Coulor.red
            }
        }   
    }
    

    "The wisher function with unknown input" should {
        "default to blue if input is invalid" in {
            controller.wisher("x") shouldBe Coulor.blue
        }
    }



    "The skipNextPlayer function with multiple players" should {
        "wrap around correctly" in {
            controller.skipNextPlayer(2, 4) shouldBe 0
        }
    }

    "The handleInvalidInput function" should {
        "return unchanged game and table" in {
            val g = Game(Nil, Nil, Card(Coulor.red, Symbol.One))
            val (ng, table, skip, won) = controller.handleInvalidInput(g, g.table, "Fehler")
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
            val fakeInput = new PlayerInput {
            override def getInput(): String = "r"
            }
            val (g1, table1, skip1, won1) = controller.playCardIfValid(Card(Coulor.red, Symbol.One), g, g.table, 0, fakeInput)
            table1.colour shouldBe Coulor.red
            won1 shouldBe true

            val invalid = Game(List(p), Nil, Card(Coulor.blue, Symbol.Two))
            val (g2, table2, skip2, won2) = controller.playCardIfValid(Card(Coulor.red, Symbol.One), invalid, invalid.table, 0, fakeInput)
            table2 shouldBe invalid.table
            won2 shouldBe false
            }
        }
    

    "The drawCardIfEmptyInput function" should {
        "draw one card when player presses Enter" in {
            val p = Player("A", Nil, 0)
            val deck = List(Card(Coulor.red, Symbol.One))
            val g = Game(List(p), deck, Card(Coulor.red, Symbol.Two))
            val (ng, table, skip, won) = controller.drawCardIfEmptyInput(p, g, g.table, 0)
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
            val fakeInput = new PlayerInput {
            override def getInput(): String = "r"
            }
            val (ng, table, skip, won) = controller.parseCardIndex("0", p, g, g.table, 0, fakeInput)
            table.symbol shouldBe Symbol.One
        }

        "handle invalid string input" in {
            val p = Player("A", Nil, 0)
            val g = Game(List(p), Nil, Card(Coulor.red, Symbol.One))
            val fakeInput = new PlayerInput {
            override def getInput(): String = "r"
            }
            noException shouldBe thrownBy(controller.parseCardIndex("x", p, g, g.table, 0, fakeInput))
        }

        "handle out of range index gracefully" in {
            val p = Player("A", Nil, 0)
            val g = Game(List(p), Nil, Card(Coulor.red, Symbol.One))
            val fakeInput = new PlayerInput {
            override def getInput(): String = "r"
            }
            noException shouldBe thrownBy(controller.parseCardIndex("5", p, g, g.table, 0, fakeInput))
        }
    }

    "The handleTurn function" should {
        "draw when input is empty or play card when valid index" in {
            val p = Player("A", List(Card(Coulor.red, Symbol.One)), 0)
            val g = Game(List(p), List(Card(Coulor.blue, Symbol.Two)), Card(Coulor.red, Symbol.Two))
            val fakeInput = new PlayerInput {
            override def getInput(): String = "r"
            }

            val (g1, _, _, _) = controller.handleTurn(g, g.table, 0, "",fakeInput)
            g1.player(0).hand.size shouldBe 2 // drew a card

            val (g2, table, _, _) = controller.handleTurn(g, g.table, 0, "0", fakeInput)
            table.colour shouldBe Coulor.red
        }
    }

    "The deckmacher function" should {
        "create a valid new game with two players" in {
            val input = new java.io.ByteArrayInputStream("A\nB\n".getBytes)
            Console.withIn(input) {
            val fakeInput = new PlayerInput {
            override def getInput(): String = "r"
            }
                val g = controller.deckmacher(fakeInput)
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
            val fakeInput = new PlayerInput {
            override def getInput(): String = "r"
            }
                val g = controller.initGame(fakeInput)
                g.player.size shouldBe 2
            }
        }
    }






    "zug function" should {
        "handle Plus_2 correctly" in {
            val p1 = Player("A", Nil, 0)
            val p2 = Player("B", Nil, 1)
            val game = Game(List(p1, p2), List.fill(5)(Card(Coulor.red, Symbol.One)), Card(Coulor.red, Symbol.Two))
            val fakeInput = new PlayerInput {
            override def getInput(): String = "r"
            }
    
            val (g1, skip1) = controller.zug(Card(Coulor.red, Symbol.Plus_2), game, 0, fakeInput)
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
            val fakeInput = new PlayerInput {
            override def getInput(): String = "r"
            }
            val (newGame, skip) = controller.zug(Card(Coulor.red, Symbol.Plus_4), game, 0, fakeInput)
            newGame.player(1).hand.length shouldBe 4   // Nächster Spieler zieht 4 Karten
            newGame.table.colour shouldBe Coulor.red   // Gewünschte Farbe gesetzt
            skip shouldBe true                         // Nächster Spieler wird übersprungen
        }
    }

        "handle Block correctly" in {
            val p1 = Player("A", Nil, 0)
            val p2 = Player("B", Nil, 1)
            val game = Game(List(p1, p2), Nil, Card(Coulor.red, Symbol.Two))
            val fakeInput = new PlayerInput {
            override def getInput(): String = "r"
            }
    
            val (g1, skip1) = controller.zug(Card(Coulor.red, Symbol.Block), game, 0, fakeInput)
            skip1 shouldBe true
        }

        "handle Reverse correctly" in {
            val p1 = Player("A", Nil, 0)
            val p2 = Player("B", Nil, 1)
            val game = Game(List(p1, p2), Nil, Card(Coulor.red, Symbol.Two))
            val fakeInput = new PlayerInput {
            override def getInput(): String = "r"
            }
    
            val (g1, skip1) = controller.zug(Card(Coulor.red, Symbol.Reverse), game, 0, fakeInput)
            skip1 shouldBe true
        }

        "handle Wish correctly" in {
            val p1 = Player("A", Nil, 0)
            val game = Game(List(p1), Nil, Card(Coulor.blue, Symbol.One))

  // Simuliere die Benutzereingabe "r" für Rot
            val input = new java.io.ByteArrayInputStream("r\n".getBytes)
            Console.withIn(input) {
            val fakeInput = new PlayerInput {
            override def getInput(): String = "r"
            }
            val (card, g1) = controller.chooseColourForCard(Card(Coulor.red, Symbol.Wish), game, fakeInput)
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
            val fakeInput = new PlayerInput {
            override def getInput(): String = "r"
            }
            val (newGame, skip) = controller.zug(Card(Coulor.red, Symbol.Wish), game, 0, fakeInput)

      // Table sollte jetzt rot sein
            newGame.table.colour shouldBe Coulor.red

      // Spieler 1 hat keine Karten mehr
            newGame.player(0).hand shouldBe empty

      // Nächster Spieler sollte nicht übersprungen (je nach Regel)
            skip shouldBe false
            }
        }
    } 
    "The remove function" should {
    "remove the observer from the subscribers list" in {
      // Dummy-Observer, der nichts tut
        val obs1 = new Observer { override def update: Unit = () }
        val obs2 = new Observer { override def update: Unit = () }

        val observable = new Observable()
        observable.add(obs1)
        observable.add(obs2)

      // Vorher: beide drin
        observable.subscribers should contain allOf (obs1, obs2)

      // Aktion
        observable.remove(obs1)

      // Danach: obs1 entfernt, obs2 bleibt
        observable.subscribers should not contain obs1
        observable.subscribers should contain (obs2)
    }
    }

    class FakeController extends Controler {
        private var _game: Game = Game(Nil, Nil, Card(Coulor.red, Symbol.One))
        override def game: Game = _game
        def game_=(g: Game): Unit = _game = g
    }
    

    "The update function" should {
    "clear the screen and print the game, person, and status" in {
        val controller = new FakeController()


        val outputStream = new java.io.ByteArrayOutputStream()
        Console.withOut(outputStream) {
        TuiInstance.update
        }

        val output = outputStream.toString

      // 🧪 Erwartungen
        output should include ("\u001b[2J\u001b[H")  // Bildschirm löschen
        output should include ("Table:")             // vom gamerenderer
//        output should include ("TestPlayer")         // Spielername
//        output should include ("FakePerson")         // vom Controller
//        output should include ("FakeStatus")         // vom Controller
        }   
    }


    "notifyObservers" should {
    "call update on all subscribers" in {
        var obs1Updated = false
        var obs2Updated = false

        val obs1 = new Observer { def update: Unit = obs1Updated = true }
        val obs2 = new Observer { def update: Unit = obs2Updated = true }

        val observable = new Observable()
        observable.add(obs1)
        observable.add(obs2)

      // Aktion
        observable.notifyObservers

      // Prüfen, dass beide Observer aktualisiert wurden
        obs1Updated shouldBe true
        obs2Updated shouldBe true
    }

    "do nothing if there are no subscribers" in {
        val observable = new Observable()
        noException should be thrownBy observable.notifyObservers
    }
    }


    "setGameAndNotify" should {
    "set game, status and person and notify observers" in {
      // FakeController mit Observer
        class TestController extends FakeController {
            var notified = false
            override def notifyObservers: Unit = notified = true
            def callSetGame(gs: Game, st: String, pe: String): Unit = 
            setGameAndNotify(gs, st, pe)  // Aufruf der privaten Methode über Wrapper
        }

        val ctrl = new TestController
        val game = Game(List(Player("X", Nil, 0)), Nil, Card(Coulor.red, Symbol.One))
        val status = "ready"
        val person = "Alice"

        ctrl.callSetGame(game, status, person)

      // Prüfen, dass die Werte gesetzt wurden
        ctrl.game shouldBe game
        ctrl.status shouldBe status
        ctrl.person shouldBe person

      // Prüfen, dass notifyObservers aufgerufen wurde
        ctrl.notified shouldBe true
        }
    }


    "safeToInt" should {
        "return Some(Int) for valid numbers" in {
            val ctrl = new FakeController
            controller.safeToInt("42") shouldBe Some(42)
            ctrl.safeToInt("  7 ") shouldBe Some(7)
        }

            "return None for invalid numbers" in {
                val ctrl = new FakeController
                ctrl.safeToInt("abc") shouldBe None
                ctrl.safeToInt("") shouldBe None
                ctrl.safeToInt("12a") shouldBe None
        }
    }

    class FakeInput(inputs: List[String]) extends PlayerInput {
        private var i = 0
        override def getInput(): String = {
            if (i < inputs.length) {
                val value = inputs(i)
                i += 1
                value
            } else ""
        }
    }

    class TestController extends FakeController {
        var notifiedTimes = 0

  // notifyObservers zählen, um zu prüfen, dass es aufgerufen wurde
        override def notifyObservers: Unit = notifiedTimes += 1

  // handleTurn für deterministisches Verhalten (Spieler gewinnt nach 1 Zug)
        override def handleTurn(
        game: Game,
        tableCard: Card,
        currentPlayerIndex: Int,
        input: String,
        inputs: PlayerInput
        ): (Game, Card, Boolean, Boolean) = {
        val playerWon = true
        (game, tableCard, false, playerWon)
        }

  // Dummy nextPlayerIndex, wird nicht gebraucht
        override def nextPlayerIndex(current: Int, total: Int, skip: Boolean): Int = 0
    }   



    "gameLoop" should {
    "update _person, _status and call notifyObservers" in {
      // Einfache Spielsituation mit 1 Spieler
        val game = Game(
        player = List(Player("Alice", Nil, 0)),
        deck = Nil,
        table = Card(Coulor.red, Symbol.One)
        )
        

        val fakeInput = new FakeInput(List("0")) // Simulierter Kartenindex

        val ctrl = new TestController
        ctrl.game = game

      // gameLoop starten
        ctrl.gameLoop(game, game.table, 0, fakeInput)

      // Prüfen, dass _person korrekt gesetzt wurde
        ctrl.person should include("Alice ist am Zug")

      // Prüfen, dass _status den Sieg anzeigt
        ctrl.status should include("hat gewonnen")

      // Prüfen, dass notifyObservers mindestens einmal aufgerufen wurde
        ctrl.notifiedTimes should be > 0
        }
    }



    "currentPlayerFinder" should {
        "return the correct player based on index" in {
            val player1 = Player("Alice", Nil, 0)
            val player2 = Player("Bob", Nil, 1)
            val game = Game(
            player = List(player1, player2),
            deck = Nil,
            table = Card(Coulor.red, Symbol.One)
        )

      // Index 0 -> Alice
            val p0 = controller.currentPlayerFinder(game, 0)
            p0.name shouldBe "Alice"
            p0.index shouldBe 0

      // Index 1 -> Bob
            val p1 = controller.currentPlayerFinder(game, 1)
            p1.name shouldBe "Bob"
            p1.index shouldBe 1
            }

            "throw an exception for invalid index" in {
            val player = Player("Alice", Nil, 0)
            val game = Game(List(player), Nil, Card(Coulor.red, Symbol.One))

      // Zugriff auf Index 1 existiert nicht
            an [IndexOutOfBoundsException] should be thrownBy {
            controller.currentPlayerFinder(game, 1)
            }
        }
    }





}
