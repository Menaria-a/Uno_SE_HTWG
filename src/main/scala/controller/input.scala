package de.htwg.Uno.controller

import de.htwg.Uno.model.Player
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.Game
import scala.collection.mutable.ArraySeq.ofUnit

trait PlayerInput:
  def getInput(game: Game, input: PlayerInput): Integer

  def getInputs(): String
