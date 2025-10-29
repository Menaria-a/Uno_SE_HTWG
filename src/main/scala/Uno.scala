<<<<<<< Updated upstream
object Uno: //mau
=======
package de.htwg.uno

object Uno: //ratte
>>>>>>> Stashed changes
  def main(args: Array[String]): Unit =

    enum Coulor:
      case red, yellow, blue, green

    enum Symbol(val value: Int):
      case One extends Symbol(1)
      case Two extends Symbol(2)
      case Three extends Symbol(3)
      case Four extends Symbol(4)
      case Five extends Symbol(5)
      case Six extends Symbol(6)
      case Seven extends Symbol(7)
      case Eight extends Symbol(8)
      case Nine extends Symbol(9)
      case Zero extends Symbol(0)
      case Plus_2 extends Symbol(11)
      case Plus_4 extends Symbol(12)
      case Reverse extends Symbol(13)
      case Block extends Symbol(14)
      case Wish extends Symbol(15)

    case class Card (colour: Coulor, symbol: Symbol)
    case class Player (name: String, hand: List[Card] = List())
    case class Game(
      player : List[Player],
      deck: List[Card],
      table: List[Card] = List(),
    )

    def creator(card: Card): List[String] =
      val coulorInd = card.colour match
        case Coulor.red => "r"
        case Coulor.yellow => "y"
        case Coulor.blue => "b"
        case Coulor.green => "g"
      val picture = card.symbol match
        case Symbol.Zero => "0"
        case Symbol.One => "1"
        case Symbol.Two => "2"
        case Symbol.Three => "3"
        case Symbol.Four => "4"
        case Symbol.Five => "5"
        case Symbol.Six => "6"
        case Symbol.Seven => "7"
        case Symbol.Eight => "8"
        case Symbol.Nine => "9"
        case Symbol.Plus_2 => "+2"
        case Symbol.Plus_4 => "+4"
        case Symbol.Reverse => "↺"
        case Symbol.Block => "⯃"
        case Symbol.Wish => "?"
      List(
        "+------+",
        f"| $coulorInd%-2s   |",
        f"|  $picture%-2s  |",
        "|      |",
        "+------+"
      )
        
    def handrenderer(hand: List[Card]): String =
          if hand.isEmpty then "UnoUno"
          else 
            val lines = hand.map(creator)
            val combined = lines.transpose.map(_.mkString(" "))
            combined.mkString("\n")

    def tablerenderer(table: List[Card]): String =
          if table.isEmpty then "hilfe"
          else 
            val lines = table.map(creator)
            val combined = lines.transpose.map(_.mkString(" "))
            combined.mkString("\n")
        
    def gamerenderer(game: Game): String =
      val playerStr = game.player
      .map(p => s"${p.name}'s hand:\n${handrenderer(p.hand)}")
      .mkString("\n\n")
      val tableStr = s"Table:\n${tablerenderer(game.table)}"
      s"$tableStr\n\n$playerStr"


    val deck = for {
    coulor <- Coulor.values
    symbol <- Symbol.values
    } yield Card(coulor, symbol)

    val shuffledDeck = scala.util.Random.shuffle(deck.toList)
        

        // Spieler mit Beispielkarten füllen (je 5 Karten)
    val melissaHand = shuffledDeck.take(5)
    val joudHand = shuffledDeck.slice(5, 10)
    val remainingDeck = shuffledDeck.drop(10)

    val players = List(
      Player("Melissa", melissaHand),
      Player("Joud", joudHand)
    )

    val tableCards = List(
      shuffledDeck(10)
    
    ) // Beispielhafte Karten auf dem Tisch

    val game = Game(players, remainingDeck, tableCards)


    println(gamerenderer(game))