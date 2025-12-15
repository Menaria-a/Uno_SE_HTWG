package de.htwg.Uno.model.state
import de.htwg.Uno.model.ModelInterface.*


trait GameState:
    def start( p1: Player, p2: Player): Game 
    def drawCard(game: Game, playerIdx: Int): Game = game
    def playCard(game: Game, playerIdx: Int, cardIdx: Int): (Game, Integer)
    def chooseColour(game: Game, colour: Coulor, hand: Card, input: Integer ): (Card, Game)



val WishCardState: GameState = Impl.WishCardStateImpl
val DrawCardState: GameState = Impl.DrawCardStateImpl
val InitState: GameState = Impl.InitStateImpl
val PlayCardState: GameState = Impl.PlayCardStateImpl

