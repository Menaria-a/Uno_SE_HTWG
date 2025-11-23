package de.htwg.Uno.controller

import de.htwg.Uno.model.Model.Player
import de.htwg.Uno.model.Model.Card
import de.htwg.Uno.model.Model.Game


trait PlayerInput:
    def getInput (): String
    //def chooseDefenseCard(defender: Player, attackCard: Card, game: GameState): String