package de.htwg.Uno.model.impl

import de.htwg.Uno.model.{Card}
import de.htwg.Uno.model.Model.Coulor
import de.htwg.Uno.model.Model.Symbol


private[model] case class CardImpl(
    coulor: Coulor,
    symbol: Symbol
)extends Card