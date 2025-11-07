package com.example.gestior.presentation.navigation

import com.example.gestior.util.Constants

sealed class Screen(val route: String) {
    data object Splash : Screen(Constants.Routes.SPLASH)
    data object Login : Screen(Constants.Routes.LOGIN)
    data object Register : Screen(Constants.Routes.REGISTER)
    data object Dashboard : Screen(Constants.Routes.DASHBOARD)
    data object Products : Screen(Constants.Routes.PRODUCTS)
    data object ProductDetail : Screen(Constants.Routes.PRODUCT_DETAIL)
    data object ProductAdd : Screen(Constants.Routes.PRODUCT_ADD)
    data object Orders : Screen(Constants.Routes.ORDERS)
    data object OrderDetail : Screen(Constants.Routes.ORDER_DETAIL)
    data object OrderCreate : Screen(Constants.Routes.ORDER_CREATE)
    data object Clients : Screen(Constants.Routes.CLIENTS)
    data object ClientDetail : Screen(Constants.Routes.CLIENT_DETAIL)
    data object Stock : Screen(Constants.Routes.STOCK)
    data object Settings : Screen(Constants.Routes.SETTINGS)
    data object BarcodeScanner : Screen(Constants.Routes.BARCODE_SCANNER)
}
