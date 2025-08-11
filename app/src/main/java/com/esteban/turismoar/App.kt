package com.esteban.turismoar

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.esteban.turismoar.data.di.*

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                dataModule,
                useCaseModule,
                viewModelModule,
                arModule
            )
        }
    }
}
