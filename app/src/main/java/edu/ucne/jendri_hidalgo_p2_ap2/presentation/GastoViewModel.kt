package edu.ucne.jendri_hidalgo_p2_ap2.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.jendri_hidalgo_p2_ap2.data.remote.Resource
import edu.ucne.jendri_hidalgo_p2_ap2.domain.model.Gasto
import edu.ucne.jendri_hidalgo_p2_ap2.domain.usecase.CreateGastoUseCase
import edu.ucne.jendri_hidalgo_p2_ap2.domain.usecase.GetGastoUseCase
import edu.ucne.jendri_hidalgo_p2_ap2.domain.usecase.UpdateGastoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class GastoViewModel @Inject constructor(
    private val getGastoUseCase: GetGastoUseCase,
    private val createGastoUseCase: CreateGastoUseCase,
    private val updateGastoUseCase: UpdateGastoUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GastoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getGastos()
    }

    fun onEvent(event: GastoEvent) {
        when (event) {
            GastoEvent.GetGasto -> getGastos()
            is GastoEvent.FechaChanged -> {
                _uiState.update { it.copy(fecha = event.fecha) }
            }
            is GastoEvent.SuplidorChanged -> {
                _uiState.update { it.copy(suplidor = event.suplidor) }
            }
            is GastoEvent.NcfChanged -> {
                _uiState.update { it.copy(ncf = event.ncf) }
            }
            is GastoEvent.ItbisChanged -> {
                _uiState.update { it.copy(itbis = event.itbis) }
            }
            is GastoEvent.MontoChanged -> {
                _uiState.update { it.copy(monto = event.monto) }
            }
            GastoEvent.OpenBottomSheet -> {
                _uiState.update {
                    it.copy(
                        showBottomSheet = true,
                        isEditing = false,
                        fecha = "",
                        suplidor = "",
                        ncf = "",
                        itbis = "",
                        monto = ""
                    )
                }
            }
            GastoEvent.CloseBottomSheet -> {
                _uiState.update {
                    it.copy(
                        showBottomSheet = false,
                        gastoSeleccionado = null,
                        isEditing = false,
                        fecha = "",
                        suplidor = "",
                        ncf = "",
                        itbis = "",
                        monto = ""
                    )
                }
            }
            GastoEvent.Save -> saveGasto()
            is GastoEvent.Edit -> editGasto(event.gasto)
            GastoEvent.ClearMessages -> {
                _uiState.update { it.copy(error = null, successMessage = null) }
            }
        }
    }

    private fun getGastos() {
        getGastoUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            gasto = result.data ?: emptyList(),
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun saveGasto() {
        val state = _uiState.value

        // Validaciones
        if (state.suplidor.isBlank()) {
            _uiState.update { it.copy(error = "El suplidor es requerido") }
            return
        }

        if (state.ncf.isBlank()) {
            _uiState.update { it.copy(error = "El NCF es requerido") }
            return
        }

        val itbis = state.itbis.toDoubleOrNull()
        if (itbis == null || itbis < 0) {
            _uiState.update { it.copy(error = "El ITBIS debe ser un número válido") }
            return
        }

        val monto = state.monto.toDoubleOrNull()
        if (monto == null || monto <= 0) {
            _uiState.update { it.copy(error = "El monto debe ser un número válido") }
            return
        }

        val gasto = Gasto(
            gastoId = state.gastoSeleccionado?.gastoId ?: 0,
            fecha = state.fecha.ifBlank { LocalDateTime.now().toString() },
            suplidor = state.suplidor,
            ncf = state.ncf,
            itbis = itbis,
            monto = monto
        )

        if (state.isEditing && state.gastoSeleccionado != null) {
            updateGasto(gasto)
        } else {
            createGasto(gasto)
        }
    }

    private fun createGasto(gasto: Gasto) {
        createGastoUseCase(gasto).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            showBottomSheet = false,
                            successMessage = "Gasto creado exitosamente",
                            fecha = "",
                            suplidor = "",
                            ncf = "",
                            itbis = "",
                            monto = ""
                        )
                    }
                    getGastos()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun updateGasto(gasto: Gasto) {
        updateGastoUseCase(gasto.gastoId, gasto).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            showBottomSheet = false,
                            successMessage = "Gasto actualizado exitosamente",
                            gastoSeleccionado = null,
                            isEditing = false,
                            fecha = "",
                            suplidor = "",
                            ncf = "",
                            itbis = "",
                            monto = ""
                        )
                    }
                    getGastos()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun editGasto(gasto: Gasto) {
        _uiState.update {
            it.copy(
                showBottomSheet = true,
                isEditing = true,
                gastoSeleccionado = gasto,
                fecha = gasto.fecha,
                suplidor = gasto.suplidor,
                ncf = gasto.ncf,
                itbis = gasto.itbis.toString(),
                monto = gasto.monto.toString()
            )
        }
    }

}