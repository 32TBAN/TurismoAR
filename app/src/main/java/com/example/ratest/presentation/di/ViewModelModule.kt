package com.example.ratest.presentation.di

import com.example.ratest.presentation.viewmodels.RouteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { RouteViewModel(get()) }
}
