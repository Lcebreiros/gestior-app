package com.example.gestior.util

import android.content.Context
import android.widget.Toast
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Extensiones útiles para la aplicación
 */

// Context extensions
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

// String extensions
fun String.toDateFormatted(
    inputPattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
    outputPattern: String = "dd/MM/yyyy HH:mm"
): String {
    return try {
        val inputFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
        val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
        val date = inputFormat.parse(this)
        date?.let { outputFormat.format(it) } ?: this
    } catch (e: Exception) {
        this
    }
}

// Number extensions
fun Number.toCurrency(currencySymbol: String = "$"): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "AR"))
    return format.format(this).replace("$", currencySymbol)
}

fun Double.toCurrencyString(currencySymbol: String = "$"): String {
    return "$currencySymbol %.2f".format(this)
}

// Date extensions
fun Date.toFormattedString(pattern: String = "dd/MM/yyyy HH:mm"): String {
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    return format.format(this)
}

fun Date.isToday(): Boolean {
    val today = Date()
    val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    return sdf.format(this) == sdf.format(today)
}

// Validation extensions
fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    return this.matches(emailRegex.toRegex())
}

fun String.isValidPhone(): Boolean {
    val phoneRegex = "^[+]?[0-9]{8,15}$"
    return this.matches(phoneRegex.toRegex())
}
