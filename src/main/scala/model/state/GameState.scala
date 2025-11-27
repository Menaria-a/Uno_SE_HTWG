package de.htwg.Uno.model.state
import de.htwg.Uno.model.Game
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.Model.Coulor
import de.htwg.Uno.model.Player

trait GameState:
    def start( p1: Player, p2: Player): Game 
    def drawCard(game: Game, playerIdx: Int): Game = game
    def playCard(game: Game, playerIdx: Int, cardIdx: Int): (Game, Integer)
    def chooseColour(game: Game, colour: Coulor, hand: Card, input: Integer ): (Card, Game)

