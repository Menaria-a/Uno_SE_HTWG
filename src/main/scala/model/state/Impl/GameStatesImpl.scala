package de.htwg.Uno.model.state.Impl

import com.google.inject.Inject
import de.htwg.Uno.model.state.*


class GameStatesImpl @Inject()(
    val InitState: InitState,
    val PlayerTurnState: PlayCardState,
    val drawCardState: DrawCardState,
    val WishCardState: WishCardState
) extends GameStates