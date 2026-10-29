package com.example.mynanny.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mynanny.ui.home.HomeRoute
import com.example.mynanny.ui.signup.SignUpRoute
import com.example.mynanny.ui.welcome.WelcomeRoute

@Composable
fun MyNannyNavHost(
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) HomeDestination else WelcomeDestination,
        modifier = modifier,
    ) {
        composable<WelcomeDestination> {
            // Search results and login screens are the next features to plug in here.
            WelcomeRoute(
                onSearchNannies = { _ -> },
                onLogin = {},
                onSignUp = { navController.navigate(SignUpDestination) },
            )
        }
        composable<SignUpDestination> {
            SignUpRoute(
                onBack = { navController.popBackStack() },
                onSignedUp = { navController.navigateClearingBackStack(HomeDestination) },
            )
        }
        composable<HomeDestination> {
            HomeRoute(
                onSignedOut = { navController.navigateClearingBackStack(WelcomeDestination) },
            )
        }
    }
}

/** Navigates to [destination] and removes every previous screen, so "back" leaves the app. */
private fun NavHostController.navigateClearingBackStack(destination: Any) {
    navigate(destination) {
        popUpTo(graph.id) { inclusive = true }
        launchSingleTop = true
    }
}
