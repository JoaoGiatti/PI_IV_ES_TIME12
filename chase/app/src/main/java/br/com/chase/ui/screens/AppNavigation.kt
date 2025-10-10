package br.com.chase.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.chase.ui.screens.home.HomeScreen
import br.com.chase.ui.screens.login.LoginScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val hasCurrentUser = FirebaseAuth.getInstance().currentUser

    // Decide qual tela irá abrir primeiro
    val startDestination = when {
        hasCurrentUser == null -> "login"
        else -> "home"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") { LoginScreen(
            onLoginSuccess = {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        ) }
        composable("home") { HomeScreen() }
    }
}
