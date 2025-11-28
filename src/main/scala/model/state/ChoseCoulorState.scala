package de.htwg.Uno.model.state


import de.htwg.Uno.model.Model._
import de.htwg.Uno.model.Enum._
import de.htwg.Uno.model.Player
import de.htwg.Uno.model.Card
import de.htwg.Uno.model.Game


  case object WishCardState extends GameState:

    override def chooseColour(game: Game, colour: Coulor, hand: Card, input: Integer ): (Card, Game)= 
      val news = chooseColourForCard(hand, game, input)
      news
    

    def chooseColourForCard(hand: Card, baseGame: Game, input: Integer): (Card, Game) =

        val newGame = baseGame.copy(ActionState = ActionState.ChooseColour)
        val wishedColour = wisher(input)
        val wishedCard = hand.copy(colour = wishedColour)
        (wishedCard, newGame.copy(table = wishedCard))


    def wisher(input: Integer): Coulor =
    input match
        case 2 => Coulor.red
        case 3 => Coulor.green
        case 1 => Coulor.yellow
        case 4   => Coulor.blue



    override def start(p1: Player, p2: Player): Game = 
        val game = Game(Nil,0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)
        game


    override def playCard(game: Game, playerIdx: Int, cardIdx: Int): (Game, Integer) =
      val game = Game(Nil,0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)
      (game, 2)
    






