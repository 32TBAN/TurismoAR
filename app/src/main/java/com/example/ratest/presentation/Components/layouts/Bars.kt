package com.example.ratest.presentation.Components.layouts

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CustomBottomBar(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val items = listOf("Home", "Search", "Favorites", "More")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Search,
        Icons.Default.Star,
        Icons.Default.Place
    )

    BottomNavigation {
        items.forEachIndexed { index, label ->
            BottomNavigationItem(
                icon = { Icon(icons[index], contentDescription = label) },
                label = { Text(label) },
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) }
            )
        }
    }
}
