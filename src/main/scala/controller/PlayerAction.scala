package de.htwg.Uno.controller

import de.htwg.Uno.model.ModelInterface.*


sealed trait PlayerAction
case class PlayCardAction(card: Card) extends PlayerAction
case object DrawAction extends PlayerAction
case object InvalidAction extends PlayerAction
case object UndoAction extends PlayerAction
case object RedoAction extends PlayerAction
case class ChooseColourAction(colour: Coulor) extends PlayerAction