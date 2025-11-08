package com.example.gestior.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object Products : Screen("products")
    object ProductDetail : Screen("product/{productId}") {
        fun createRoute(productId: Int) = "product/$productId"
    }
    object CreateProduct : Screen("product/create")
    object Orders : Screen("orders")
    object OrderDetail : Screen("order/{orderId}") {
        fun createRoute(orderId: Int) = "order/$orderId"
    }
    object CreateOrder : Screen("order/create")
    object Clients : Screen("clients")
    object ClientDetail : Screen("client/{clientId}") {
        fun createRoute(clientId: Int) = "client/$clientId"
    }
    object CreateClient : Screen("client/create")
    object Profile : Screen("profile")
}
