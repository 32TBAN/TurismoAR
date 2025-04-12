package com.example.ratest.presentation.Components.layouts

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.ratest.R
import androidx.compose.ui.res.painterResource
import com.example.ratest.Utils.BottomBarIcon
import androidx.compose.material3.Icon
import com.example.ratest.presentation.Navigation.lables
import com.example.ratest.presentation.Navigation.screens
import com.example.ratest.ui.theme.Green
import androidx.compose.ui.text.font.FontWeight
import com.example.ratest.ui.theme.DarkGreen
import com.example.ratest.ui.theme.White

@Composable
fun CustomBottomBar(
    selectedIndex: Int,
    onTabSelected: (Any) -> Unit
) {

    val icons = listOf(
        BottomBarIcon.Vector(Icons.Default.Home),
        BottomBarIcon.Vector(Icons.Default.Place),
        BottomBarIcon.Drawable(R.drawable.ic_history)
    )

    BottomNavigation(backgroundColor = Green, contentColor = White) {
        icons.forEachIndexed { index, icon ->
            BottomNavigationItem(
                icon = {
                    when (icon) {
                        is BottomBarIcon.Vector -> Icon(
                            imageVector = icon.icon,
                            contentDescription = null
                        )

                        is BottomBarIcon.Drawable -> Icon(
                            painter = painterResource(id = icon.resId),
                            contentDescription = null
                        )
                    }
                },
                label = { Text(lables[index], color = DarkGreen, fontWeight = FontWeight.Bold) },
                selected = selectedIndex == index,
                onClick = { onTabSelected(screens[index]) },
                selectedContentColor = Green,
                unselectedContentColor = Green.copy(alpha = 0.5f)
            )
        }
    }
}
