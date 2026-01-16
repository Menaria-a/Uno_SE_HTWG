import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.Uno.model._
import de.htwg.Uno.model.Enum._
import de.htwg.Uno.model.builder.GameBuilder
import de.htwg.Uno.model.Model.*
import de.htwg.Uno.model.state.GameStates

class GameSpec(gameStates: GameStates) extends AnyWordSpec with Matchers {

  "A Game" should {

    "correctly convert to a GameBuilder with the same state" in {
      // Sample players
      val player1 = Player("Alice", List(Card(Coulor.red, Symbol.Three)), 0)
      val player2 = Player("Bob", List(Card(Coulor.blue, Symbol.Reverse)), 1)
      val players = List(player1, player2)

      // Sample deck and table
      val deck = List(Card(Coulor.green, Symbol.Seven), Card(Coulor.yellow, Symbol.Reverse))
      val tableCard = Some(Card(Coulor.red, Symbol.Five))
      val actionState = ActionState.None
      val turnState = TurnState.None

      // Create game instance
      val game = Game(players, 0, deck, tableCard, actionState, turnState)

      // Convert to builder
      val builder = de.htwg.Uno.model.builder.Impl.GameBuilder.apply(gameStates)
      val resultBuilder = game.toBuilder(builder)

      // Assertions
      resultBuilder.withPlayers shouldEqual players
      resultBuilder.withIndex shouldEqual 0
      resultBuilder.withDeck shouldEqual deck
      resultBuilder.withTable shouldEqual tableCard
      resultBuilder.withActionState shouldEqual actionState
      resultBuilder.withTurnState shouldEqual turnState
    }
  }
}


