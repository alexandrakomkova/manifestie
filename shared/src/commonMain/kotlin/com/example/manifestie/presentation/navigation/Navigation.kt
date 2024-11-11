package com.example.manifestie.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.manifestie.data.datastore.DataStoreHelper.getKoin
import com.example.manifestie.presentation.screens.category.CategorySharedViewModel
import com.example.manifestie.presentation.screens.category.category_details.CategoryDetailScreen
import com.example.manifestie.presentation.screens.category.category_list.CategoryScreen
import com.example.manifestie.presentation.screens.random_quote.RandomQuoteScreen
import com.example.manifestie.presentation.screens.random_quote.RandomQuoteViewModel
import com.example.manifestie.resources.Res
import com.example.manifestie.resources.list_stars_icon
import com.example.manifestie.resources.nav_quotes
import com.example.manifestie.resources.nav_random_quote
import com.example.manifestie.resources.nav_settings
import com.example.manifestie.resources.setting_icon
import com.example.manifestie.resources.sparkles_icon
import io.github.aakira.napier.Napier
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


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
        topBar = {
            if(currentScreen?.route == AppScreen.CategoryDetail.route) {
               getTitle(currentScreen).also {
                    TopBar(
                        title = it,
                        canNavigateBack = currentScreen.route == AppScreen.CategoryDetail.route,
                        navigateUp = { navController.navigateUp() }
                    )
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomBarScreen.QuotesCategoryList.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            val sharedViewModel: CategorySharedViewModel = getKoin().get()

            composable(route = BottomBarScreen.QuotesCategoryList.route) {
                val addCategorySheetState by sharedViewModel.addCategoryState.collectAsState()

                CategoryScreen(
                    viewModel = sharedViewModel,
                    addCategorySheetState = addCategorySheetState,
                    onEvent = sharedViewModel::onEvent,
                    onCategoryClick = {
                        Napier.d(tag = "onNavigate from CategoryScreen", message = it.id)

                        sharedViewModel.updateSelectedCategoryForQuotes(it)
                        onNavigate(AppScreen.CategoryDetail.route)
                    }
                )
            }

            composable(
                route = AppScreen.CategoryDetail.route,
                arguments = AppScreen.CategoryDetail.navArguments
            ) {
                val addQuoteSheetState by sharedViewModel.addQuoteState.collectAsState()

                CategoryDetailScreen(
                    viewModel = sharedViewModel,
                    onUpClick =  { navController.navigateUp() },
                    onEvent = sharedViewModel::onEvent,
                    addQuoteSheetState = addQuoteSheetState
                )
            }

            composable(route = BottomBarScreen.RandomQuote.route) {
                val randomQuoteViewModel = getKoin().get<RandomQuoteViewModel>()
                val chooseCategoryState by randomQuoteViewModel.chooseCategoryState.collectAsState()
                val sharedState by sharedViewModel.state.collectAsState()

                RandomQuoteScreen(
                    viewModel = randomQuoteViewModel,
                    sharedState = sharedState,
                    onEvent = randomQuoteViewModel::onEvent,
                    chooseCategoryState = chooseCategoryState
                )
            }

            composable(route = BottomBarScreen.Settings.route) {
                HomeView()
            }
        }
    }
}

fun getTitle(currentScreen: NavDestination?): String {
    return when (currentScreen?.route) {
        BottomBarScreen.QuotesCategoryList.route -> {
            "Quote Categories"
        }

        BottomBarScreen.Settings.route -> {
            "Settings"
        }

        BottomBarScreen.RandomQuote.route -> {
            "Random Quote"
        }

        AppScreen.CategoryDetail.route -> {
            "Category Detail"
        }

        else -> {
            ""
        }
    }
}

fun navigateTo(
    routeName: String,
    navController: NavController
) {
    // maybe should add back navigation
    navController.navigate(routeName)
}

sealed class AppScreen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object CategoryDetail: AppScreen("nav_category_detail")
}

sealed class BottomBarScreen(
    val route: String,
    val title: org.jetbrains.compose.resources.StringResource,
    val defaultIcon: DrawableResource?
) {

    data object QuotesCategoryList: BottomBarScreen(
        route = "QUOTES",
        title = Res.string.nav_quotes,
        defaultIcon = Res.drawable.list_stars_icon
    )
    data object RandomQuote: BottomBarScreen(
        route = "RANDOM_QUOTE",
        title = Res.string.nav_random_quote,
        defaultIcon = Res.drawable.sparkles_icon
    )
    data object Settings: BottomBarScreen(
        route = "SETTINGS",
        title = Res.string.nav_settings,
        defaultIcon = Res.drawable.setting_icon
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back_button"
                    )
                }
            }
        }
    )
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    val screens = listOf(
        BottomBarScreen.QuotesCategoryList,
        BottomBarScreen.RandomQuote,
        BottomBarScreen.Settings
    )

    AppBottomNavigationBar(
        show = navController.shouldShowBottomBar
    ) {
        screens.forEach { item ->
            AppBottomNavigationBarItem(
                icon = item.defaultIcon,
                label = stringResource(item.title),
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
        //color = Color(0xFF6C63E7),
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier.windowInsetsPadding(BottomAppBarDefaults.windowInsets)
    ) {
        if(show) {
            Column {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = Color.White.copy(alpha = 0.2f)
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
    icon: DrawableResource?,
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
        icon?.let { painterResource(it) }?.let {
            Image(
                painter = it,
                contentDescription = icon.toString(),
                contentScale = ContentScale.Crop,
                colorFilter = if(selected) {
                    ColorFilter.tint(Color.White)
                } else {
                    ColorFilter.tint(Color.DarkGray)
                },
                modifier = modifier.then(
                    Modifier.clickable {
                        onItemClick()
                    }.size(24.dp)
                )
            )
        }
        Text(
            text = label,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            },
            color = if(selected) {
                Color.White
            } else {
                Color.DarkGray
            }
        )
    }

}

private fun navigateBottomBar(
    navController: NavController,
    destination: String
) {
    navController.navigate(destination) {
        navController.graph.startDestinationRoute?.let {
            popUpTo(BottomBarScreen.QuotesCategoryList.route) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}

private val NavController.shouldShowBottomBar
    get() = when (this.currentBackStackEntry?.destination?.route) {
        BottomBarScreen.QuotesCategoryList.route,
        BottomBarScreen.RandomQuote.route,
        BottomBarScreen.Settings.route -> true

        else -> false
    }


@Composable
fun HomeView() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Home")

        Spacer(modifier = Modifier.height(50.dp))

    }
}
