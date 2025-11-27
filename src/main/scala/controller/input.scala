package de.htwg.Uno.controller

import de.htwg.Uno.model.Player
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.Game


trait PlayerInput:
    def getInput (): Integer

    def getInputs (): String
