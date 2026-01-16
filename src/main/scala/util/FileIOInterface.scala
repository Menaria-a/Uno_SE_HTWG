package de.htwg.Uno.util

import de.htwg.Uno.model.Game
import scala.util.Try

trait FileIOInterface:
  def save(game: Game): Try[Unit]
  def load(): Try[Game]
