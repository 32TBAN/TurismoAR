package com.example.ratest.data.di

import com.example.ratest.data.local.RouteDataSource
import com.example.ratest.data.repository.RouteRepositoryImpl
import com.example.ratest.domain.repository.RouteRepository
import org.koin.dsl.module

val dataModule = module {
    single { RouteDataSource(get()) }
    single<RouteRepository> { RouteRepositoryImpl(get()) }
}
