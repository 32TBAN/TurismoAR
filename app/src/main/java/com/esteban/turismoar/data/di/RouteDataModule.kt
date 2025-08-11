package com.esteban.turismoar.data.di

import com.esteban.turismoar.data.local.RouteDataSource
import com.esteban.turismoar.data.repository.RouteRepositoryImpl
import com.esteban.turismoar.data.repository.TourRepositoryImpl
import com.esteban.turismoar.domain.repository.RouteRepository
import com.esteban.turismoar.domain.repository.TourRepository
import com.esteban.turismoar.domain.usecase.GetAllRoutesUseCase
import com.esteban.turismoar.domain.usecase.TourManager
import com.esteban.turismoar.presentation.viewmodels.ar.ARViewModel
import com.esteban.turismoar.presentation.viewmodels.home.RouteViewModel
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
