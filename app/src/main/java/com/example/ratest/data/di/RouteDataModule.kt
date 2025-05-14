package com.example.ratest.data.di

import com.example.ratest.data.local.RouteDataSource
import com.example.ratest.data.repository.RouteRepositoryImpl
import com.example.ratest.data.repository.TourRepositoryImpl
import com.example.ratest.domain.repository.RouteRepository
import com.example.ratest.domain.repository.TourRepository
import com.example.ratest.domain.usecase.GetAllRoutesUseCase
import com.example.ratest.domain.usecase.TourManager
import com.example.ratest.presentation.viewmodels.ar.ARViewModel
import com.example.ratest.presentation.viewmodels.home.RouteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single { RouteDataSource(get()) }
    single<RouteRepository> { RouteRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModel { RouteViewModel(get()) }
}
val useCaseModule  = module {
    factory { GetAllRoutesUseCase(get()) }
}

val arModule = module {
    single<TourRepository> { TourRepositoryImpl(get()) }
    single { TourManager(get()) }
    viewModel { ARViewModel(get()) }
}
