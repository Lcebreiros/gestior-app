package com.example.gestior.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gestior.presentation.screens.auth.LoginScreen
import com.example.gestior.presentation.screens.dashboard.DashboardScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Auth Routes
        composable(route = Screen.Login.route) {
            LoginScreen(
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(route = Screen.Register.route) {
            // TODO: RegisterScreen
        }

        // Main App Routes
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToProducts = { navController.navigate(Screen.Products.route) },
                onNavigateToOrders = { navController.navigate(Screen.Orders.route) },
                onNavigateToClients = { navController.navigate(Screen.Clients.route) },
                onNavigateToStock = { navController.navigate(Screen.Stock.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        // Products
        composable(route = Screen.Products.route) {
            // TODO: ProductsScreen
        }

        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) {
            // TODO: ProductDetailScreen
        }

        // Orders
        composable(route = Screen.Orders.route) {
            // TODO: OrdersScreen
        }

        // Clients
        composable(route = Screen.Clients.route) {
            // TODO: ClientsScreen
        }

        // Settings
        composable(route = Screen.Settings.route) {
            // TODO: SettingsScreen
        }
    }
}
