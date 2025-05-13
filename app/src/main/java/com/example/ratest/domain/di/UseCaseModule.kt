package com.example.ratest.domain.di

import com.example.ratest.domain.usecase.GetAllRoutesUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetAllRoutesUseCase(get()) }
}
