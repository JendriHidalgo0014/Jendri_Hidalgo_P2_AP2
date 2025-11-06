package edu.ucne.jendri_hidalgo_p2_ap2.presentation

import edu.ucne.jendri_hidalgo_p2_ap2.domain.model.Gasto

data class GastoUiState(
    val isLoading: Boolean = false,
    val gasto: List<Gasto> = emptyList(),
    val error: String? = null,
    val gastoSeleccionado: Gasto? = null,
    val fecha: String = "",
    val suplidor: String = "",
    val ncf: String = "",
    val itbis: String = "",
    val monto: String = "",
    val showBottomSheet: Boolean = false,
    val isEditing: Boolean = false,
    val successMessage: String? = null
)