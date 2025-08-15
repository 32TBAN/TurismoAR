package com.esteban.turismoar.presentation.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.AddLocation
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.esteban.turismoar.R
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.esteban.turismoar.presentation.navigation.lables
import com.esteban.turismoar.presentation.navigation.screens
import com.esteban.turismoar.presentation.navigation.titlesTopBar
import com.esteban.turismoar.ui.theme.Green
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.esteban.turismoar.presentation.navigation.AddScreen
import com.esteban.turismoar.ui.theme.DarkGreen
import com.esteban.turismoar.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
        modifier = Modifier.border(1.dp, Green.copy(alpha = 0.5f)),
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
fun CustomTopBar(navController: NavController, scope: CoroutineScope, drawerState: DrawerState) {

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
                tint = Color.Unspecified,
                modifier = Modifier.clickable(
                    onClick = {
                        scope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    }
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = currentLabel,
                color = DarkGreen,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CustomLateralPlane(navController: NavController, scope: CoroutineScope, drawerState: DrawerState) {
    ModalDrawerSheet(
        modifier = Modifier.width(240.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(12.dp))
            Text(
                "Turismo RA",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge
            )
            HorizontalDivider()
            NavigationDrawerItem(
                label = { Text("Add") },
                selected = false,
                icon = { Icon(Icons.Outlined.AddLocation, contentDescription = null) },
                onClick = {
                    navController.navigate(AddScreen)
                    scope.launch {
                        if (drawerState.isClosed) {
                            drawerState.open()
                        } else {
                            drawerState.close()
                        }
                    }
                }
            )
            NavigationDrawerItem(
                label = { Text("Settings") },
                selected = false,
                icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                onClick = { /* Handle click */ }
            )
            NavigationDrawerItem(
                label = { Text("Help and feedback") },
                selected = false,
                icon = { Icon(Icons.AutoMirrored.Outlined.Help, contentDescription = null) },
                onClick = { /* Handle click */ },
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}