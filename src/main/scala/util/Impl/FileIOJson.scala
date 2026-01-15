package de.htwg.Uno.util.Impl

import com.google.inject.Inject
import de.htwg.Uno.model.state.GameStates
import de.htwg.Uno.util.FileIOInterface
import de.htwg.Uno.model.*
import de.htwg.Uno.model.Enum.*
import scala.util.Try
import java.io.*
import play.api.libs.json.*
import scala.io.Source
import scalafx.scene.input.KeyCode.Play
import de.htwg.Uno.model.Model.*

class FileIOJson @Inject() (Path: String, gameStates: GameStates) extends FileIOInterface:

    override def save(game: Game): Try[Unit] = Try {
        val json = gameToJson(game)
        val prettyJson = Json.prettyPrint(json)

        val pw = new PrintWriter(new File(Path))
        try {
            pw.write(prettyJson)
        } finally {
            pw.close()
        }
    }

    override def load(): Try[Game] = Try {
        val source = Source.fromFile(Path)
        try {
            val content = source.getLines.mkString
            val json = Json.parse(content)
            jsonToGame(json)
        } finally {
            source.close()
        }
    }

    private def gameToJson(game: Game): JsValue = {
        Json.obj(
            "player" -> JsArray(game.player.map(playerToJson)),
            "index" -> game.index,
            "deck" -> JsArray(game.deck.map(cardToJson)),
            "table" -> game.table.map(cardToJson).getOrElse(JsNull),
            "actionState" -> game.ActionState.toString,
            "turnState" -> turnStateToJson(game.TurnState)

        )
    }

    private def jsonToGame(json: JsValue): Game = {
        val player = (json \ "player").as[JsArray].value.map(jsonToPlayer).toList
        val index = (json \ "index").as[Int]
        val deck = (json \ "deck").as[JsArray].value.map(jsonToCard).toList
        val table: Option[Card] =
                (json \ "table").get match
                case JsNull => None
                case value  => Some(jsonToCard(value))
        val actionState =
            ActionState.valueOf((json \ "actionState").as[String])
        val turnState =
                    jsonToTurnState((json \ "turnState").get)
        Game (player, index,deck,table, actionState, turnState)
    }

    private def playerToJson(player: Player): JsValue = {
        Json.obj(
            "name" -> player.name,
            "hand" -> JsArray(player.hand.map(cardToJson)),
            "index" -> player.index
        )
    }

    private def jsonToPlayer(json: JsValue): Player = {
        Player(
            (json \ "name").as[String],
            (json \ "hand").as[JsArray].value.map(jsonToCard).toList,
            (json \ "index").as[Int]
        )
    }

    private def cardToJson(card: Card): JsValue = {
        Json.obj(
            "coulor" -> card.colour.toString,
            "symbol" -> card.symbol.toString
        )
    }

    private def jsonToCard(json: JsValue): Card = {
        Card(
            Coulor.valueOf((json \ "coulor").as[String]),
            Symbol.valueOf((json \ "symbol").as[String])
        )
    }

    private def turnStateToJson(state: TurnState): JsValue = state match
        case TurnState.PlayerTurn(player) =>
            Json.obj(
            "type" -> "PlayerTurn",
            "player" -> playerToJson(player)
        )

        case TurnState.GameWon(player) =>
            Json.obj(
            "type" -> "GameWon",
            "player" -> playerToJson(player)
        )   

        case TurnState.None =>
            Json.obj(
            "type" -> "None"
        )

    private def jsonToTurnState(json: JsValue): TurnState =
        (json \ "type").as[String] match
        case "PlayerTurn" =>
            TurnState.PlayerTurn(
            jsonToPlayer((json \ "player").get)
        )

        case "GameWon" =>
            TurnState.GameWon(
            jsonToPlayer((json \ "player").get)
        )

        case "None" =>
            TurnState.None

        case other =>
            throw new IllegalArgumentException(s"Unknown TurnState: $other")
