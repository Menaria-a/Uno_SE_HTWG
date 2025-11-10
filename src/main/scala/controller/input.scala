package de.htwg.Uno.controller

import de.htwg.Uno.modell.Model.Player
import de.htwg.Uno.modell.Model.Card
import de.htwg.Uno.modell.Model.Game


trait PlayerInput:
    def getInput (): String
    //def chooseDefenseCard(defender: Player, attackCard: Card, game: GameState): String