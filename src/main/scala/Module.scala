package de.htwg.Uno

import com.google.inject.* 
import net.codingwell.scalaguice.ScalaModule
import de.htwg.Uno.model.state.Impl.*
import de.htwg.Uno.model.state.*
import de.htwg.Uno.model.*
import de.htwg.Uno.model.builder.*
import de.htwg.Uno.model.builder.Impl.*
import de.htwg.Uno.controller.Command.*
import de.htwg.Uno.controller.Command.Impl.*
import de.htwg.Uno.controller.Controller
import de.htwg.Uno.util.Undo.CommandManager


class Module extends AbstractModule with ScalaModule:
    override def configure(): Unit =
        bind[GameStates].to[GameStatesImpl].asEagerSingleton()
        bind[InitState].toInstance(InitStateImpl)
        bind[PlayCardState].toInstance(PlayCardStateImpl)
        bind[DrawCardState].toInstance(DrawCardStateImpl)
        bind[WishCardState].toInstance(WishCardStateImpl)
        bind[CommandFactory].to[CommandFactoryImpl].asEagerSingleton()
        bind[GameBuilderFactory].to[GameBuilderFactoryImpl]
        bind[Controller].to[controller.Impl.ControllerImpl]in(classOf[Singleton])

    @Provides
    def provideGame(
        GameBuilderFactory: GameBuilderFactory
    ): Game =
        GameBuilderFactory
        .create().build().get
    @Provides 
    def provideManager(): CommandManager =
    new CommandManager

