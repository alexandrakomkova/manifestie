package com.example.manifestie.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHostMain(
        navController = navController,
        onNavigate = { routeName ->
            navigateTo(routeName, navController)
        }
    )
}

@Composable
fun NavHostMain(
    navController: NavHostController = rememberNavController(),
    onNavigate: (rootName: String) -> Unit,
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) {

    }

}

fun navigateTo(
    routeName: String,
    navController: NavController
) {
    // maybe should add back navigation
    navController.navigate(routeName)
}

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val defaultIcon: DrawableResource
) {

    data object Quotes: BottomBarScreen(
        route = "RANDOM_QUOTE",
        title = "Random Quote",
        defaultIcon = null
    )
    data object RandomQuote: BottomBarScreen(
        route = "RANDOM_QUOTE",
        title = "Random Quote",
        defaultIcon = null
    )
    data object Settings: BottomBarScreen(
        route = "RANDOM_QUOTE",
        title = "Random Quote",
        defaultIcon = null
    )

}

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    val screens = listOf(
        BottomBarScreen.Quotes,
        BottomBarScreen.RandomQuote,
        BottomBarScreen.Settings
    )

    AppBottomNavigationBar(
        show = navController.shouldShowBottomBar
    ) {
        screens.forEach { item ->
            AppBottomNavigationBarItem(
                icon = item.defaultIcon,
                label = item.title,
                onItemClick = {
                    navigateBottomBar(navController, item.route)
                },
                selected = navController.currentBackStackEntry?.destination?.route == item.route
            )
        }
    }
}

@Composable
fun AppBottomNavigationBar(
    modifier: Modifier = Modifier,
    show: Boolean,
    content: @Composable (RowScope.() -> Unit)
) {
    Surface (
        color = Color.DarkGray,
        contentColor = Color.Blue,
        modifier = modifier.windowInsetsPadding(BottomAppBarDefaults.windowInsets)
    ) {
        if(show) {
            Column {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = Color.Red.copy(alpha = 0.2f)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .selectableGroup(),
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}

@Composable
fun RowScope.AppBottomNavigationBarItem(
    modifier: Modifier = Modifier,
    icon: DrawableResource,
    label: String,
    onItemClick: () -> Unit,
    selected: Boolean,
) {
    Column(
        modifier = modifier
            .weight(1f)
            .clickable(onClick = onItemClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = icon.toString(),
            contentScale = ContentScale.Crop,
            colorFilter = if(selected) {
                ColorFilter.tint(Color.Black)
            } else {
                ColorFilter.tint(Color.LightGray)
            },
            modifier = modifier.then(
                Modifier.clickable {
                    onItemClick()
                }.size(24.dp)
            )
        )
        Text(
            text = label,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            },
            color = if(selected) {
                Color.Black
            } else {
                Color.LightGray
            }
        )
    }

}

private fun navigateBottomBar(
    navController: NavController,
    destination: String
) {
    navController.navigate(destination) {
        navController.graph.startDestinationRoute?.let { route ->
            popUpTo(BottomBarScreen.Quotes.route) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}

private val NavController.shouldShowBottomBar
    get() = when (this.currentBackStackEntry?.destination?.route) {
        BottomBarScreen.Quotes.route,
        BottomBarScreen.RandomQuote.route,
        BottomBarScreen.Settings.route -> true

        else -> false
    }
