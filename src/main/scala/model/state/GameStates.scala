package de.htwg.Uno.model.state

trait GameStates {
    def drawCardState: GameState
    def InitState: GameState
    def PlayerTurnState: GameState
    def WishCardState: GameState


    def isdrawCardState (state: GameState): Boolean =
        state == drawCardState
    def isInitState (state: GameState): Boolean =
        state == InitState
    def isPlayerTurnState (state: GameState): Boolean =
        state == PlayerTurnState
    def isWishState (state: GameState): Boolean = 
        state == WishCardState
}