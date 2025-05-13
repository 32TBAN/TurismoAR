package com.example.ratest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ratest.data.di.dataModule
import com.example.ratest.domain.di.domainModule
import com.example.ratest.presentation.AppContent
import com.example.ratest.presentation.di.arModule
import com.example.ratest.presentation.di.viewModelModule
import com.example.ratest.ui.theme.RAtestTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
        startKoin {
            androidContext(this@MainActivity)
            modules(
                dataModule,
                domainModule,
                viewModelModule,
                arModule
            )
        }

        setContent {
            RAtestTheme {
                AppContent()
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppContent()
}
