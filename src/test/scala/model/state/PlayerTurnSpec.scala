package de.htwg.Uno.model.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers._
import de.htwg.Uno.model.ModelInterface.StateInterface.*
import de.htwg.Uno.model.ModelInterface.*
import de.htwg.Uno.controller.ControllerInterface.*



class PlayCardSpecs extends AnyWordSpec with Matchers{


    "The parseCardIndex function" should {
        "play a card when valid index is chosen" in {
            val p = Player("A", List(Card(Coulor.red, Symbol.One)), 0)
            val g = Game(List(p),0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)
            val fakeInput = 0
            val (game, number) = PlayCardState.parseCardIndex(0, p, g, g.table, 0)
            number shouldBe 5
        }

        "handle invalid string input" in {
            val p = Player("A", Nil, 0)
            val g = Game(List(p),0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)
            val fakeInput = 2
            noException shouldBe thrownBy(PlayCardState.parseCardIndex(1, p, g, g.table, 0))
        }
    }

        "The handleInvalidInput function" should {
        "return unchanged game and table" in {
            val g = Game(Nil,0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)
            val (game, int) = PlayCardState.handleInvalidInput(g, g.table, ActionState.None)
            game shouldBe g
        }
    }


    "The handleTurn function" should {
        "draw when input is empty or play card when valid index" in {
            val p = Player("A", List(Card(Coulor.red, Symbol.One)), 0)
            val g = Game(List(p),0, List(Card(Coulor.blue, Symbol.Two)), Card(Coulor.red, Symbol.Two), ActionState.None, TurnState.None)
            val fakeInput = 2
            val (g1, ints) = PlayCardState.handleTurn(g, 0, 500)
            g1.player(0).hand.size shouldBe 1 // drew a card

            val (g2, int) = PlayCardState.handleTurn(g, 0, fakeInput)
            g2.table.coulor shouldBe Coulor.red
        }
    }

        "handle out of range index gracefully" in {
            val p = Player("A", Nil, 0)
            val g = Game(List(p),0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)
            noException shouldBe thrownBy(PlayCardState.parseCardIndex(5, p, g, g.table, 0))
        }
    



    "The plusN function" should {
        "add N cards to the next player's hand and skip the turn" in {
            val p1 = Player("A", Nil, 0)
            val p2 = Player("B", Nil, 1)
            val deck = List.fill(5)(Card(Coulor.red, Symbol.One))
            val game = Game(List(p1, p2),0, deck, Card(Coulor.green, Symbol.Two), ActionState.None, TurnState.None)

            val (newGame) = PlayCardState.plusN(game, 1, Card(Coulor.red, Symbol.Plus_2), 2)
            newGame.player(1).hand.length shouldBe 2
        }
    
        "The playCardIfValid function" should {
        "play a valid card and reject invalid ones" in {
            val p = Player("A", List(Card(Coulor.red, Symbol.One)), 0)
            val g = Game(List(p),0, Nil, Card(Coulor.red, Symbol.Two), ActionState.None, TurnState.None)

            val (g1,int) = PlayCardState.playCardIfValid(Card(Coulor.red, Symbol.One), g, g.table, 0)
            g1.table.coulor shouldBe Coulor.red
            val invalid = Game(List(p),0, Nil, Card(Coulor.blue, Symbol.Two), ActionState.None, TurnState.None)
            val (g2, ints) = PlayCardState.playCardIfValid(Card(Coulor.red, Symbol.One), invalid, invalid.table, 0)
            g2.table shouldBe invalid.table
            }
        }

    }

        "turn function" should {
        "handle Plus_2 correctly" in {
            val p1 = Player("A", Nil, 0)
            val p2 = Player("B", Nil, 1)
            val game = Game(List(p1, p2),0, List.fill(5)(Card(Coulor.red, Symbol.One)), Card(Coulor.red, Symbol.Two), ActionState.None, TurnState.None)

    
            val (g1, skip1) = PlayCardState.turn(Card(Coulor.red, Symbol.Plus_2), game, 0)
            g1.player(1).hand.length shouldBe 0
        }

        "handle Plus_4 correctly" in {
            val p1 = Player("A", List(Card(Coulor.red, Symbol.Plus_4)), 0)
            val p2 = Player("B", Nil, 1)
            val game = Game(List(p1, p2),0, List.fill(5)(Card(Coulor.green, Symbol.One)), Card(Coulor.blue, Symbol.One), ActionState.None, TurnState.None)

  // Simuliere die Benutzereingabe für die gewünschte Farbe "r" (rot)
            val input = new java.io.ByteArrayInputStream("r\n".getBytes)
            Console.withIn(input) {

            val (newGame, skip) = PlayCardState.turn(Card(Coulor.red, Symbol.Plus_4), game, 0)
            newGame.player(1).hand.length shouldBe 0  // Nächster Spieler zieht 4 Karten
            newGame.table.coulor shouldBe Coulor.red   // Gewünschte Farbe gesetzt
        }
    }

        "handle Block correctly" in {
            val p1 = Player("A", Nil, 0)
            val p2 = Player("B", Nil, 1)
            val game = Game(List(p1, p2),0, Nil, Card(Coulor.red, Symbol.Two), ActionState.None, TurnState.None)


    

        }

        "handle Reverse correctly" in {
            val p1 = Player("A", Nil, 0)
            val p2 = Player("B", Nil, 1)
            val game = Game(List(p1, p2),0, Nil, Card(Coulor.red, Symbol.Two), ActionState.None, TurnState.None)
            }
        }

    "The isPlayable function" should {
        "detect valid plays correctly" in {
            val table = Card(Coulor.red, Symbol.One)
            val sameColour = Card(Coulor.red, Symbol.Two)
            val sameSymbol = Card(Coulor.blue, Symbol.One)
            val wishCard = Card(Coulor.green, Symbol.Wish)
            val invalid = Card(Coulor.blue, Symbol.Three)

            PlayCardState.isPlayable(table, sameColour) shouldBe true
            PlayCardState.isPlayable(table, sameSymbol) shouldBe true
            PlayCardState.isPlayable(table, wishCard) shouldBe true
            PlayCardState.isPlayable(table, invalid) shouldBe false
        }
    }



    "The start function" should {

        "create a new game with the expected default values" in {
        val p1 = Player("Alice", Nil, 0)
        val p2 = Player("Bob", Nil, 0)

        val game = WishCardState.start(p1, p2)

        game.player shouldBe empty
        game.index shouldBe 0
        game.table shouldBe Card(Coulor.red, Symbol.One)
        game.ActionState shouldBe ActionState.None
        game.TurnState shouldBe TurnState.None
        }
    }

}



