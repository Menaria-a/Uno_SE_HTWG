package de.htwg.Uno.model

object Model:

    
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
    case class Player (name: String, hand: List[Card] = List(), index: Int)
    case class Game(
        player : List[Player],
        deck: List[Card],
        table: Card
    )
    
