package de.htwg.Uno.model.state

import de.htwg.Uno.model.state.{
    WishCardState as InterWishCardState,
    DrawCardState as IntDrawCardState,
    GameState as IntGameState,
    InitState as IntInitState,
    PlayCardState as IntPlayCardState
} 


object StateInterface:

    type GameState = IntGameState
    type InitState = IntInitState.type


    val WishCardState = InterWishCardState
    val DrawCardState = IntDrawCardState
    val InitState = IntInitState
    val PlayCardState = IntPlayCardState