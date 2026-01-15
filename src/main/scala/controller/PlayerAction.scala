package de.htwg.Uno.controller

import de.htwg.Uno.model.Model.*
import de.htwg.Uno.model.*

sealed trait PlayerAction
case class PlayCardAction(playerIdx: Int, cardIdx: Int, input: Int) extends PlayerAction
case class DrawAction(playerIdx: Int) extends PlayerAction
case object InvalidAction extends PlayerAction
case class UndoAction(game: Game) extends PlayerAction
case object RedoAction extends PlayerAction
case class ChooseColourAction(hand: Card, input: PlayerInput) extends PlayerAction
case object IOAction extends PlayerAction