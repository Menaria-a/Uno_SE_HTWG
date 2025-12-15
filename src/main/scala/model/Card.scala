package de.htwg.Uno.model
import de.htwg.Uno.model.Model.*

trait Card:
    def coulor: Coulor 
    def symbol: Symbol


    def copy(
        coulor: Coulor,
        symbol: Symbol
    ): Card = Card(coulor,symbol)

object Card: 
    def apply(coulor: Coulor, symbol: Symbol): Card =
        impl.CardImpl(coulor, symbol)
