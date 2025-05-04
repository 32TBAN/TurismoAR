package com.example.ratest.presentation.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.ratest.R
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ratest.presentation.navigation.lables
import com.example.ratest.presentation.navigation.screens
import com.example.ratest.presentation.navigation.titlesTopBar
import com.example.ratest.ui.theme.Green
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ratest.ui.theme.DarkGreen
import com.example.ratest.ui.theme.White

@Composable
fun CustomBottomBar(
    selectedIndex: Int,
    onTabSelected: (Any) -> Unit
) {

    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Place,
        Icons.Default.MenuBook
    )

    BottomNavigation(
        backgroundColor = White
    ) {
        icons.forEachIndexed { index, icon ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null
                    )
                },
                label = { Text(lables[index], fontSize = 10.sp) },
                selected = selectedIndex == index,
                onClick = { onTabSelected(screens[index]) },
                selectedContentColor = Green,
                unselectedContentColor = Green.copy(alpha = 0.5f)
            )
        }
    }
}


@Composable
fun CustomTopBar(navController: NavController) {

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
//    Log.d("CustomTopBar", "currentRoute: $currentRoute")
//    Log.d("CustomTopBar", "${titlesTopBar[0].first}")
    val currentScreen = currentRoute.substringAfterLast(".")
    val currentLabel =
        titlesTopBar.find { it.first::class.simpleName == currentScreen }?.second ?: ""

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painterResource(id = R.drawable.app_logo),
                contentDescription = "Logo",
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "$currentLabel",
                color = DarkGreen,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
