package de.htwg.Uno.model.builder


trait GameBuilderFactory{
    def create(): GameBuilder
}