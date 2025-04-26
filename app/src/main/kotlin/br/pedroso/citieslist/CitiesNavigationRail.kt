package br.pedroso.citieslist

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.pedroso.citieslist.designsystem.theme.CitiesListTheme

@Composable
fun CitiesNavigationRail(
    topLevelScreens: List<AppScreen.TopLevelScreen> = AppScreen.TopLevelScreen.Screens,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavigationRail(modifier = modifier) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        topLevelScreens.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            NavigationRailItem(
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter =
                            painterResource(
                                id =
                                    if (selected) {
                                        screen.selectedIconResource
                                    } else {
                                        screen.unselectedIconResource
                                    },
                            ),
                        contentDescription = null,
                    )
                },
                label = { Text(text = stringResource(id = screen.labelResource)) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CitiesNavigationRailPreview() {
    CitiesListTheme {
        CitiesNavigationRail()
    }
}
