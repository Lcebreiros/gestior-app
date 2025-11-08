package com.example.gestior.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gestior.ui.screens.auth.LoginScreen
import com.example.gestior.ui.screens.auth.RegisterScreen
import com.example.gestior.ui.screens.dashboard.DashboardScreen
import com.example.gestior.ui.screens.products.ProductsScreen
import com.example.gestior.ui.screens.products.ProductDetailScreen
import com.example.gestior.ui.screens.products.CreateProductScreen
import com.example.gestior.ui.screens.orders.OrdersScreen
import com.example.gestior.ui.screens.clients.ClientsScreen
import com.example.gestior.ui.screens.profile.ProfileScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToProducts = {
                    navController.navigate(Screen.Products.route)
                },
                onNavigateToOrders = {
                    navController.navigate(Screen.Orders.route)
                },
                onNavigateToClients = {
                    navController.navigate(Screen.Clients.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(Screen.Products.route) {
            ProductsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProduct = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onNavigateToCreateProduct = {
                    navController.navigate(Screen.CreateProduct.route)
                }
            )
        }

        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) {
            val productId = it.arguments?.getInt("productId") ?: 0
            ProductDetailScreen(
                productId = productId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.CreateProduct.route) {
            CreateProductScreen(
                onNavigateBack = { navController.popBackStack() },
                onProductCreated = { navController.popBackStack() }
            )
        }

        composable(Screen.Orders.route) {
            OrdersScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCreateOrder = {
                    navController.navigate(Screen.CreateOrder.route)
                }
            )
        }

        composable(Screen.Clients.route) {
            ClientsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCreateClient = {
                    navController.navigate(Screen.CreateClient.route)
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
