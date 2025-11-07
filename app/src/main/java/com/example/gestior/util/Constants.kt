package com.example.gestior.util

object Constants {
    // API Configuration
    const val BASE_URL = "https://rellenitosrevelados.com/" // Base URL de la API de rellenito-alfajores
    const val API_TIMEOUT = 30L // segundos

    // DataStore Keys
    const val PREFERENCES_NAME = "gestior_preferences"
    const val KEY_AUTH_TOKEN = "auth_token"
    const val KEY_USER_ID = "user_id"
    const val KEY_USER_EMAIL = "user_email"
    const val KEY_USER_NAME = "user_name"
    const val KEY_IS_LOGGED_IN = "is_logged_in"

    // Database
    const val DATABASE_NAME = "gestior_database"
    const val DATABASE_VERSION = 1

    // Navigation
    object Routes {
        const val SPLASH = "splash"
        const val LOGIN = "login"
        const val REGISTER = "register"
        const val DASHBOARD = "dashboard"
        const val PRODUCTS = "products"
        const val PRODUCT_DETAIL = "product_detail/{productId}"
        const val PRODUCT_ADD = "product_add"
        const val ORDERS = "orders"
        const val ORDER_DETAIL = "order_detail/{orderId}"
        const val ORDER_CREATE = "order_create"
        const val CLIENTS = "clients"
        const val CLIENT_DETAIL = "client_detail/{clientId}"
        const val STOCK = "stock"
        const val SETTINGS = "settings"
        const val BARCODE_SCANNER = "barcode_scanner"

        fun productDetail(productId: Int) = "product_detail/$productId"
        fun orderDetail(orderId: Int) = "order_detail/$orderId"
        fun clientDetail(clientId: Int) = "client_detail/$clientId"
    }

    // Pagination
    const val PAGE_SIZE = 20
    const val INITIAL_PAGE = 1

    // UI
    const val ANIMATION_DURATION = 300
    const val DEBOUNCE_DELAY = 500L
}
