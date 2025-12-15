package de.htwg.Uno.model
import de.htwg.Uno.model.Card


trait Player:
    def name: String
    def hand: List[Card]
    def index: Int


    def copy(
        name: String = this.name,
        hand: List[Card] = this.hand,
        index: Int = this.index
    ): Player = Player(name,hand,index)

object Player:
    def apply(
        name: String,
        hand: List[Card] = List(),
        index: Int = 0
    ): Player = 
        impl.PlayerImpl(name,hand,index)

