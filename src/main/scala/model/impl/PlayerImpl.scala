package de.htwg.Uno.model.impl

import de.htwg.Uno.model.{Player,Card}

private [model] case class PlayerImpl(
    name: String,
    hand: List[Card] = List(),
    index: Int = 0
) extends Player