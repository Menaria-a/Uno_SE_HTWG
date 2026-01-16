package de.htwg.Uno.util.Impl

import com.google.inject.Inject
import de.htwg.Uno.model.state.GameStates
import de.htwg.Uno.util.FileIOInterface
import de.htwg.Uno.model.*
import de.htwg.Uno.model.Enum.*
import scala.util.Try
import java.io.*
import scala.xml.PrettyPrinter
import scala.xml.{Elem, Node}
import de.htwg.Uno.model.Model.*

class FileIOXml @Inject() (Path: String, gameStates: GameStates)
    extends FileIOInterface:

  override def save(game: Game): Try[Unit] = Try {
    val xml = gameToXml(game)
    val prettyPrinter = new PrettyPrinter(80, 2)
    val prettyXml = prettyPrinter.format(xml)
    val pw = new PrintWriter(new File(Path))
    try pw.write(prettyXml)
    finally pw.close()
  }

  override def load(): Try[Game] = Try {
    val xml = scala.xml.XML.loadFile(Path)
    xmlToGame(xml)
  }

  private def gameToXml(game: Game): Elem =
    val playerXml = game.player.map(playerToXml)
    val deckXml = game.deck.map(cardToXml)

    <Game>
      <Players>{playerXml}</Players>
      <Index>{game.index}</Index>
      <Deck>{deckXml}</Deck>
      <Table>{game.table.map(cardToXml).getOrElse(<Empty/>)}</Table>
      <ActionState>{game.ActionState.toString}</ActionState>
      <TurnState>{turnStateToXml(game.TurnState)}</TurnState>
    </Game>

  private def playerToXml(player: Player): Elem =
    <Player>
      <Name>{player.name}</Name>
      <Hand>{player.hand.map(cardToXml)}</Hand>
      <Index>{player.index}</Index>
    </Player>

  private def cardToXml(card: Card): Elem =
    <Card>
      <Coulor>{card.colour.toString}</Coulor>
      <Symbol>{card.symbol.toString}</Symbol>
    </Card>

  private def turnStateToXml(state: TurnState): Elem = state match
    case TurnState.PlayerTurn(player) =>
      <PlayerTurn>{playerToXml(player)}</PlayerTurn>
    case TurnState.GameWon(player) =>
      <GameWon>{playerToXml(player)}</GameWon>
    case TurnState.None =>
      <None/>

  private def xmlToGame(node: Node): Game =
    val players = (node \ "Players" \ "Player").map(xmlToPlayer).toList
    val index = (node \ "Index").text.toInt
    val deck = (node \ "Deck" \ "Card").map(xmlToCard).toList
    val table = (node \ "Table").headOption.flatMap { t =>
      (t \ "Card").headOption.map(xmlToCard)
    }

    val actionState = (node \ "ActionState").text match
      case s if ActionState.values.exists(_.toString == s) =>
        ActionState.values.find(_.toString == s).get
      case other =>
        throw new IllegalArgumentException(s"Unknown ActionState: $other")

    val turnState = (node \ "TurnState").headOption
      .map(xmlToTurnState)
      .getOrElse(TurnState.None)

    Game(players, index, deck, table, actionState, turnState)

  private def xmlToPlayer(node: Node): Player =
    Player(
      (node \ "Name").text,
      (node \ "Hand" \ "Card").map(xmlToCard).toList,
      (node \ "Index").text.toInt
    )

  private def xmlToCard(node: Node): Card =
    Card(
      Coulor.valueOf((node \ "Coulor").text),
      Symbol.valueOf((node \ "Symbol").text)
    )

  private def xmlToTurnState(node: Node): TurnState =
    val child =
      node.child.filter(_.label != "#PCDATA").headOption.getOrElse(<None/>)
    child.label match
      case "PlayerTurn" =>
        TurnState.PlayerTurn(xmlToPlayer((child \ "Player").head))
      case "GameWon" => TurnState.GameWon(xmlToPlayer((child \ "Player").head))
      case "None"    => TurnState.None
      case other =>
        throw new IllegalArgumentException(s"Unknown TurnState: $other")
