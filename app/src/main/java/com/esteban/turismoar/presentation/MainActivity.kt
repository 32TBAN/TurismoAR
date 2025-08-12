package com.esteban.turismoar.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.esteban.turismoar.ui.theme.RAtestTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, false)

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
