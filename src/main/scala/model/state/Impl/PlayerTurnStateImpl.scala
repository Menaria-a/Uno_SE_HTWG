package de.htwg.Uno.model.state.Impl
import de.htwg.Uno.model.ModelInterface.*

private[state] case object PlayCardStateImpl extends GameState:

    override def playCard(game: Game, playerIdx: Int, cardIdx: Int): (Game, Integer) =
        handleTurn(game, playerIdx,cardIdx)

    def handleTurn(
        game: Game,
        currentPlayerIndex: Int,
        chosenCardIndex: Int,
    ): (Game, Integer) =
        val currentPlayer: Player = game.player(currentPlayerIndex)
        val tableCard: Card = game.table 

        chosenCardIndex match
        case 500 => (game, 3)
        case _ => parseCardIndex(chosenCardIndex, currentPlayer, game, game.table, currentPlayerIndex)


    def parseCardIndex(
        index: Int,
        player: Player,
        game: Game,
        tableCard: Card,
        currentPlayerIndex: Int,
    ): (Game, Integer) =
        if index < 0 || index >= player.hand.size || index == 66 then
            handleInvalidInput(game, tableCard, ActionState.OutOfRange)
        else
            val cardToPlay = player.hand(index)
            playCardIfValid(cardToPlay, game, tableCard, currentPlayerIndex)

    def playCardIfValid(
        card: Card,
        game: Game,
        tableCard: Card,
        currentPlayerIndex: Int,
    ): (Game, Integer) =
        if isPlayable(tableCard, card) then
            val (newGame, wishdex) = turn(card, game, currentPlayerIndex)
            (newGame, wishdex)
        else handleInvalidInput(game, tableCard, ActionState.CardNotPlayable)

    def handleInvalidInput(
        game: Game,
        tableCard: Card,
        message: ActionState
    ): (Game, Integer) =
        val g = game.copy(ActionState = message)
        (g,0)

    def turn(
        card: Card,
        game: Game,
        currentPlayerIndex: Int,
    ): (Game, Integer) =
        val currentPlayer = game.player(currentPlayerIndex)
        val updatedHand = currentPlayer.hand.filterNot(_ == card)
        val updatedPlayers = game.player.updated(currentPlayerIndex, currentPlayer.copy(hand = updatedHand))
        val baseGame = game.copy(player = updatedPlayers)

        if updatedHand.isEmpty then
            val winner = updatedPlayers(currentPlayerIndex)
            val winGame = baseGame.copy(
            TurnState = TurnState.GameWon(winner),
            table = card
            )
            return (winGame,5)


        card.symbol match
            case Symbol.Plus_2 =>
                val nextIndex = nextPlayerIndex(currentPlayerIndex, game.player.size, true)
                val games = plusN(baseGame, (nextIndex + 1) % 2 , card, 2)
                val indexy = games.copy(index = nextIndex, table = card)
                val gameee = indexy
                (gameee , 6)
            case Symbol.Plus_4 =>
                val nextIndex = nextPlayerIndex(currentPlayerIndex, game.player.size, true)
                val afterDraw = plusN(baseGame, (nextIndex + 1) % 2, card, 4)
                val Indexy = afterDraw.copy(index = nextIndex, table = card)
                val games = Indexy
                (games, 1)
            case Symbol.Block =>
                val nextIndex = nextPlayerIndex(currentPlayerIndex, game.player.size, true)
                val games  = baseGame.copy(table = card, index = nextIndex)
                (games, 6)
            case Symbol.Reverse =>
                val nextIndex = nextPlayerIndex(currentPlayerIndex, game.player.size, true)
                val games = baseGame.copy(table = card, index = nextIndex)
                (games, 6)
            case Symbol.Wish =>
                val nextIndex = nextPlayerIndex(currentPlayerIndex, game.player.size, false)
                val Indexy = baseGame.copy(index = nextIndex)
                val games = Indexy
                (games, 1)
            case _ =>
                val nextIndex = nextPlayerIndex(currentPlayerIndex, game.player.size, false)
                val games = baseGame.copy(table = card, index = nextIndex)
                (games, 0)


    def isPlayable(table: Card, hand: Card): Boolean =
        hand.coulor == table.coulor || hand.symbol == table.symbol || hand.symbol == Symbol.Wish || hand.symbol == Symbol.Plus_4

    def nextPlayerIndex(currentIndex: Int, playerCount: Int, skipNext: Boolean): Int =
        if (skipNext) {
            val mau =(currentIndex + 2) % 2
            mau} else {val mau = (currentIndex + 1) % 2
            mau}


    def plusN(game: Game, nextPlayerIndex: Int, card: Card, n: Int): Game =
        val player = game.player(nextPlayerIndex)
        val (newPlayer, newDeck) = dealCardsToHand(player, game.deck, n)
        val updatedPlayers = game.player.updated(nextPlayerIndex, newPlayer)
        game.copy(player = updatedPlayers, deck = newDeck)

    def dealCardsToHand(player: Player, deck: List[Card], n: Int): (Player, List[Card]) =
        val (dealtCards, remainingDeck) = deck.splitAt(n)
        (player.copy(hand = player.hand ::: dealtCards), remainingDeck)


    override def start(p1: Player, p2: Player): Game = 
        val game = Game(Nil,0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)
        game
    
    override def chooseColour(game: Game, colour: Coulor, hand: Card, input: Integer): (Card, Game) =
        val game = Game(Nil,0, Nil, Card(Coulor.red, Symbol.One), ActionState.None, TurnState.None)
        val card = Card(Coulor.red, Symbol.One)
        (card, game)