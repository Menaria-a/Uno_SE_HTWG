package de.htwg.Uno.model.builder.Impl

import de.htwg.Uno.model.builder.*
import de.htwg.Uno.model.*
import com.google.inject.Inject
import de.htwg.Uno.model.state.*


class GameBuilderFactoryImpl @Inject() (
    gameStates : GameStates
) extends GameBuilderFactory {
    def create(): GameBuilder =
        GameBuilder(gameStates)
}