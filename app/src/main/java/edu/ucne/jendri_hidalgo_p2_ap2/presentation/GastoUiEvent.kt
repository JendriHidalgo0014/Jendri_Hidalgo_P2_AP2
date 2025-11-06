package edu.ucne.jendri_hidalgo_p2_ap2.presentation

import edu.ucne.jendri_hidalgo_p2_ap2.domain.model.Gasto

sealed interface GastoEvent {
    data object GetGasto: GastoEvent
    data class FechaChanged(val fecha: String) : GastoEvent
    data class SuplidorChanged(val suplidor: String) : GastoEvent
    data class NcfChanged(val ncf: String) : GastoEvent
    data class ItbisChanged(val itbis: String) : GastoEvent
    data class MontoChanged(val monto: String) : GastoEvent
    data object OpenBottomSheet : GastoEvent
    data object CloseBottomSheet : GastoEvent
    data object Save : GastoEvent
    data class Edit(val gasto: Gasto) : GastoEvent
    data object ClearMessages : GastoEvent
}