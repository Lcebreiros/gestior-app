package com.example.gestior.presentation.screens.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gestior.domain.model.PaymentMethod
import com.example.gestior.util.toCurrencyString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrderScreen(
    onNavigateBack: () -> Unit,
    onOrderCreated: (Int) -> Unit,
    viewModel: CreateOrderViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDiscountDialog by remember { mutableStateOf(false) }
    var showPaymentMethodDialog by remember { mutableStateOf(false) }

    // Mostrar error si hay
    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    // Navegar al detalle cuando se cree el pedido
    LaunchedEffect(state.orderCreated) {
        state.orderCreated?.let { order ->
            onOrderCreated(order.id)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Pedido") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { /* TODO: Seleccionar cliente */ }
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Cliente")
                    }
                }
            )
        },
        bottomBar = {
            CreateOrderBottomBar(
                total = state.total,
                itemCount = state.items.size,
                onSaveDraft = { viewModel.createOrder(finalize = false) },
                onFinalize = { viewModel.createOrder(finalize = true) },
                isLoading = state.isLoading,
                enabled = state.items.isNotEmpty()
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Búsqueda de productos */ }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar producto")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Client Info (if selected)
            if (state.clientName != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = state.clientName,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }

                        IconButton(onClick = { viewModel.setClient(null, null) }) {
                            Icon(Icons.Default.Close, contentDescription = "Remover cliente")
                        }
                    }
                }
            }

            // Order configuration row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Discount button
                OutlinedButton(
                    onClick = { showDiscountDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.LocalOffer,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (state.discount > 0) {
                            "-${state.discount.toCurrencyString()}"
                        } else {
                            "Descuento"
                        }
                    )
                }

                // Payment method button
                OutlinedButton(
                    onClick = { showPaymentMethodDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Payment,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = when (state.paymentMethod) {
                            PaymentMethod.CASH -> "Efectivo"
                            PaymentMethod.CARD -> "Tarjeta"
                            PaymentMethod.TRANSFER -> "Transferencia"
                            PaymentMethod.MULTIPLE -> "Múltiple"
                        }
                    )
                }
            }

            // Items list
            if (state.items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Text(
                            text = "Sin items",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Agrega productos para crear el pedido",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.items) { item ->
                        OrderItemCard(
                            item = item,
                            onQuantityChange = { newQuantity ->
                                viewModel.updateItemQuantity(item.product.id, newQuantity)
                            },
                            onPriceChange = { newPrice ->
                                viewModel.updateItemPrice(item.product.id, newPrice)
                            },
                            onRemove = {
                                viewModel.removeItem(item.product.id)
                            }
                        )
                    }

                    // Summary card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                SummaryRow(
                                    label = "Subtotal",
                                    amount = state.subtotal
                                )

                                if (state.discount > 0) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    SummaryRow(
                                        label = "Descuento",
                                        amount = -state.discount,
                                        isDiscount = true
                                    )
                                }

                                Divider(modifier = Modifier.padding(vertical = 8.dp))

                                SummaryRow(
                                    label = "TOTAL",
                                    amount = state.total,
                                    isTotal = true
                                )
                            }
                        }
                    }

                    // Bottom spacing for FAB
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }

    // Discount Dialog
    if (showDiscountDialog) {
        DiscountDialog(
            currentDiscount = state.discount,
            onConfirm = { discount ->
                viewModel.setDiscount(discount)
                showDiscountDialog = false
            },
            onDismiss = { showDiscountDialog = false }
        )
    }

    // Payment Method Dialog
    if (showPaymentMethodDialog) {
        PaymentMethodDialog(
            currentMethod = state.paymentMethod,
            onMethodSelected = { method ->
                viewModel.setPaymentMethod(method)
                showPaymentMethodDialog = false
            },
            onDismiss = { showPaymentMethodDialog = false }
        )
    }
}

@Composable
fun CreateOrderBottomBar(
    total: Double,
    itemCount: Int,
    onSaveDraft: () -> Unit,
    onFinalize: () -> Unit,
    isLoading: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "$itemCount items",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = total.toCurrencyString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onSaveDraft,
                        enabled = enabled && !isLoading
                    ) {
                        Text("Guardar")
                    }

                    Button(
                        onClick = onFinalize,
                        enabled = enabled && !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Finalizar")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderItemCard(
    item: OrderItemDraft,
    onQuantityChange: (Double) -> Unit,
    onPriceChange: (Double) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.product.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )

                    if (item.product.sku != null) {
                        Text(
                            text = "SKU: ${item.product.sku}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onRemove) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quantity controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = { onQuantityChange(item.quantity - 1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Disminuir")
                    }

                    Text(
                        text = item.quantity.toInt().toString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.widthIn(min = 40.dp)
                    )

                    IconButton(
                        onClick = { onQuantityChange(item.quantity + 1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Aumentar")
                    }
                }

                // Price and subtotal
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = item.price.toCurrencyString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = item.subtotal.toCurrencyString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryRow(
    label: String,
    amount: Double,
    isTotal: Boolean = false,
    isDiscount: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal,
            color = if (isDiscount) MaterialTheme.colorScheme.error
                   else MaterialTheme.colorScheme.onSecondaryContainer
        )

        Text(
            text = amount.toCurrencyString(),
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.SemiBold,
            color = if (isDiscount) MaterialTheme.colorScheme.error
                   else if (isTotal) MaterialTheme.colorScheme.primary
                   else MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun DiscountDialog(
    currentDiscount: Double,
    onConfirm: (Double) -> Unit,
    onDismiss: () -> Unit
) {
    var discountText by remember { mutableStateOf(currentDiscount.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Aplicar Descuento") },
        text = {
            Column {
                OutlinedTextField(
                    value = discountText,
                    onValueChange = { discountText = it },
                    label = { Text("Monto del descuento") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val discount = discountText.toDoubleOrNull() ?: 0.0
                    onConfirm(discount)
                }
            ) {
                Text("Aplicar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun PaymentMethodDialog(
    currentMethod: PaymentMethod,
    onMethodSelected: (PaymentMethod) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Método de Pago") },
        text = {
            Column {
                PaymentMethod.values().forEach { method ->
                    val label = when (method) {
                        PaymentMethod.CASH -> "Efectivo"
                        PaymentMethod.CARD -> "Tarjeta"
                        PaymentMethod.TRANSFER -> "Transferencia"
                        PaymentMethod.MULTIPLE -> "Múltiple"
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentMethod == method,
                            onClick = { onMethodSelected(method) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = label)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}
