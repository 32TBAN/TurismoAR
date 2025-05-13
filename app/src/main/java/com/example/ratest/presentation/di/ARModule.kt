package com.example.ratest.presentation.di

import com.example.ratest.data.repository.TourRepositoryImpl
import com.example.ratest.domain.repository.TourRepository
import com.example.ratest.domain.usecase.TourManager
import com.example.ratest.presentation.viewmodels.ARViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val arModule = module {
    single<TourRepository> { TourRepositoryImpl(get()) }
    single { TourManager(get()) }
    viewModel { ARViewModel(get()) }
}
