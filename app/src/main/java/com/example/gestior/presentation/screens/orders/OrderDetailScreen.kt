package com.example.gestior.presentation.screens.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gestior.domain.model.OrderItem
import com.example.gestior.util.toDateFormatted
import com.example.gestior.util.toCurrencyString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: OrderDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showCancelDialog by remember { mutableStateOf(false) }
    var showFinalizeDialog by remember { mutableStateOf(false) }

    // Mostrar error si hay
    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    // Navegar atrás si la acción fue exitosa
    LaunchedEffect(state.actionSuccess) {
        if (state.actionSuccess) {
            snackbarHostState.showSnackbar("Pedido actualizado")
            viewModel.resetActionSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Pedido") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Compartir */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Compartir")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.order != null -> {
                    val order = state.order!!

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Header Card
                        item {
                            OrderHeaderCard(
                                orderNumber = order.orderNumber,
                                status = order.status,
                                createdAt = order.createdAt
                            )
                        }

                        // Client Info Card
                        if (order.clientName != null) {
                            item {
                                InfoCard(
                                    title = "Cliente",
                                    icon = Icons.Default.Person
                                ) {
                                    Text(
                                        text = order.clientName,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }

                        // Items Card
                        item {
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.ShoppingBag,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "Items del Pedido",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    if (!order.items.isNullOrEmpty()) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                        order.items.forEach { item ->
                                            OrderItemRow(item = item)
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Sin items",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        // Notes Card
                        if (!order.notes.isNullOrBlank()) {
                            item {
                                InfoCard(
                                    title = "Notas",
                                    icon = Icons.Default.Notes
                                ) {
                                    Text(
                                        text = order.notes,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        // Totals Card
                        item {
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    TotalRow(
                                        label = "Subtotal",
                                        amount = order.subtotal ?: order.total
                                    )

                                    if (order.discount > 0) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        TotalRow(
                                            label = "Descuento",
                                            amount = -order.discount,
                                            isDiscount = true
                                        )
                                    }

                                    if (order.taxAmount > 0) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        TotalRow(
                                            label = "Impuestos",
                                            amount = order.taxAmount
                                        )
                                    }

                                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                                    TotalRow(
                                        label = "TOTAL",
                                        amount = order.total,
                                        isTotal = true
                                    )
                                }
                            }
                        }

                        // Action Buttons
                        if (order.isDraft()) {
                            item {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Button(
                                        onClick = { showFinalizeDialog = true },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = !state.isFinalizingOrder
                                    ) {
                                        if (state.isFinalizingOrder) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(20.dp),
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        } else {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Finalizar Pedido")
                                        }
                                    }

                                    OutlinedButton(
                                        onClick = { showCancelDialog = true },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = !state.isCancelingOrder
                                    ) {
                                        if (state.isCancelingOrder) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(20.dp)
                                            )
                                        } else {
                                            Icon(Icons.Default.Cancel, contentDescription = null)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Cancelar Pedido")
                                        }
                                    }
                                }
                            }
                        }

                        // Bottom spacing
                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            }
        }
    }

    // Cancel Dialog
    if (showCancelDialog) {
        CancelOrderDialog(
            onConfirm = {
                viewModel.cancelOrder(null)
                showCancelDialog = false
            },
            onDismiss = { showCancelDialog = false }
        )
    }

    // Finalize Dialog
    if (showFinalizeDialog) {
        FinalizeOrderDialog(
            onConfirm = {
                viewModel.finalizeOrder()
                showFinalizeDialog = false
            },
            onDismiss = { showFinalizeDialog = false }
        )
    }
}

@Composable
fun OrderHeaderCard(
    orderNumber: String,
    status: String,
    createdAt: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Pedido #$orderNumber",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = status)

                createdAt?.let {
                    Text(
                        text = it.toDateFormatted(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}

@Composable
fun OrderItemRow(
    item: OrderItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${item.quantity.toInt()} x ${item.price.toCurrencyString()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = item.subtotal.toCurrencyString(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun TotalRow(
    label: String,
    amount: Double,
    isTotal: Boolean = false,
    isDiscount: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = if (isTotal) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyLarge,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal,
            color = if (isDiscount) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = amount.toCurrencyString(),
            style = if (isTotal) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyLarge,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.SemiBold,
            color = if (isTotal) MaterialTheme.colorScheme.primary
                    else if (isDiscount) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun CancelOrderDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cancelar Pedido") },
        text = { Text("¿Estás seguro de que deseas cancelar este pedido? Esta acción no se puede deshacer.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Cancelar Pedido")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Volver")
            }
        }
    )
}

@Composable
fun FinalizeOrderDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Finalizar Pedido") },
        text = { Text("¿Deseas finalizar este pedido? Se descontará el stock de los productos.") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Finalizar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
