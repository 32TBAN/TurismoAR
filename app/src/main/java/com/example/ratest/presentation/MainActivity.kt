package com.example.ratest.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ratest.data.di.*
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
                useCaseModule,
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
