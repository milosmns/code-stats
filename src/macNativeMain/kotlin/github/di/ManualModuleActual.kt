package github.di

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.curl.Curl

actual fun provideHttpClientEngineFactory(): HttpClientEngineFactory<*> = Curl
