package de.htwg.Uno.model.impl

import de.htwg.Uno.model.*
import de.htwg.Uno.model.Model.Coulor
import de.htwg.Uno.model.Model.Symbol



private[model] case class CardImpl(
    coulor: Coulor,
    symbol: Symbol
)extends Card:
    override def copy(
        coulor: Coulor,
        symbol: Symbol,
    ): Card = CardImpl(coulor,symbol)


class CardFactoryImpl extends CardFactory:
    def apply(coulor: Coulor, symbol: Symbol): Card =
        CardImpl(coulor,symbol)