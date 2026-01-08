package de.htwg.Uno.model.impl

import de.htwg.Uno.model.*


private [model] case class PlayerImpl(
    name: String,
    hand: List[Card] = List(),
    index: Int = 0
) extends Player:
    override def copy(
        name: String = this.name,
        hand: List[Card] = this.hand,
        index: Int = this.index
    ): Player = PlayerImpl(name,hand,index)


class PlayerFactoryImpl extends PlayerFactory:
    def apply(
        name: String,
        hand: List[Card] = List(),
        index: Int = 0
    ): Player = PlayerImpl(name,hand,index)