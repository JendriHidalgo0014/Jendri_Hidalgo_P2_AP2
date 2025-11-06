package edu.ucne.jendri_hidalgo_p2_ap2.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.jendri_hidalgo_p2_ap2.domain.model.Gasto


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastoScreen(
    modifier: Modifier = Modifier,
    viewModel: GastoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.error, uiState.successMessage) {
        if (uiState.error != null || uiState.successMessage != null) {
            kotlinx.coroutines.delay(3000)
            viewModel.onEvent(GastoEvent.ClearMessages)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Gastos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(GastoEvent.OpenBottomSheet) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = uiState.error ?: "Error desconocido",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.onEvent(GastoEvent.GetGasto) }) {
                            Text("Reintentar")
                        }
                    }
                }
                uiState.gasto.isEmpty() -> {
                    Text(
                        text = "No hay gastos",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.gasto) { gasto ->
                            GastoItem(
                                gasto = gasto,
                                onEdit = { viewModel.onEvent(GastoEvent.Edit(gasto)) },
                                onDelete = {}
                            )
                        }
                    }
                }
            }

            uiState.successMessage?.let { message ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(message)
                }
            }
        }

        // BottomSheet
        if (uiState.showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.onEvent(GastoEvent.CloseBottomSheet) }
            ) {
                GastoBottomSheetContent(
                    uiState = uiState,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }
}

@Composable
fun GastoItem(
    gasto: Gasto,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = gasto.suplidor,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "NCF: ${gasto.ncf}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Fecha: ${gasto.fecha.take(10)}",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "ITBIS: $${String.format("%.2f", gasto.itbis)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Monto: $${String.format("%.2f", gasto.monto)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

}

@Composable
fun GastoBottomSheetContent(
    uiState: GastoUiState,
    onEvent: (GastoEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = if (uiState.isEditing) "Editar Gasto" else "Nuevo Gasto",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.suplidor,
            onValueChange = { onEvent(GastoEvent.SuplidorChanged(it)) },
            label = { Text("Suplidor") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.ncf,
            onValueChange = { onEvent(GastoEvent.NcfChanged(it)) },
            label = { Text("NCF") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.itbis,
            onValueChange = { onEvent(GastoEvent.ItbisChanged(it)) },
            label = { Text("ITBIS") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.monto,
            onValueChange = { onEvent(GastoEvent.MontoChanged(it)) },
            label = { Text("Monto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { onEvent(GastoEvent.CloseBottomSheet) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }

            Button(
                onClick = { onEvent(GastoEvent.Save) },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (uiState.isEditing) "Actualizar" else "Guardar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}