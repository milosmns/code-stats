package server.di

import server.config.ServerConfig

fun provideServerConfig(): ServerConfig = ServerConfig()
