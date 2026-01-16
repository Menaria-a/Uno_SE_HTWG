package de.htwg.Uno.model
import de.htwg.Uno.model.Card

case class Player(name: String, hand: List[Card] = List(), index: Int)
