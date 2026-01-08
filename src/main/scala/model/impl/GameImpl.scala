package de.htwg.Uno.model.impl
import de.htwg.Uno.model.*
import de.htwg.Uno.model.Enum.*
import de.htwg.Uno.model.builder.GameBuilder
import com.google.inject.Inject
import org.checkerframework.checker.units.qual.A


private[model] class GameImpl(
    val player: List[Player],
    val index: Integer,
    val deck: List[Card],
    val table: Card,
    val ActionState: ActionState,
    val TurnState: TurnState,
    cardFactory: CardFactory,
    playerFactory: PlayerFactory,
    GameBuilder: GameBuilder

) extends Game {


    override def copy(
        player: List[Player] = this.player,
        index: Integer = this.index,
        deck: List[Card] = this.deck,
        table: Card = this.table,
        ActionState: ActionState = this.ActionState,
        TurnState: TurnState = this.TurnState
    ): Game = new GameImpl(player,index,deck,table,ActionState,TurnState,cardFactory, playerFactory, GameBuilder)

    def toBuilder: GameBuilder = {
        val inlineGameFactory = new GameFactory {
            def apply(
            player: List[Player],
            index: Integer,
            deck: List[Card],
            table: Card,
            ActionState: ActionState,
            TurnState: TurnState,
            ): Game = GameImpl(
                player,index,deck,table,ActionState, TurnState,cardFactory,playerFactory,GameBuilder
            )
        }
        GameBuilder
        .withPlayers(player)
        .withIndex(index)
        .withDeck(deck)
        .withTable(table)
        .withActionState(ActionState)
        .withTurnState(TurnState)
    }

    override def equals(obj: Any): Boolean = obj match {
        case that: GameImpl =>
            this.player == that.player &&
            this.index == that.index &&
            this.deck == that.deck &&
            this.table == that.table &&
            this.ActionState == that.ActionState &&
            this.TurnState == that.TurnState
        case _ => false
        }

    override def hashCode(): Int = {
        val state = Seq(
            player,
            index,
            deck,
            table,
            ActionState,
            TurnState
        )
        state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
    } 
}

class GameFactoryImpl @Inject() (
    cardFactory: CardFactory,
    playerFactory: PlayerFactory,
    gameBuilderFactory: GameBuilderFactory
) extends GameFactory:
    def apply(
        player: List[Player],
        index: Integer,
        deck: List[Card],
        table: Card,
        ActionState: ActionState,
        TurnState: TurnState 
    ): Game =
        GameImpl(
            player,index,deck,table,ActionState,TurnState, cardFactory,playerFactory,gameBuilderFactory.create()
        )