package edu.ucne.jendri_hidalgo_p2_ap2.domain.usecase

import edu.ucne.jendri_hidalgo_p2_ap2.data.remote.Resource
import edu.ucne.jendri_hidalgo_p2_ap2.domain.model.Gasto
import edu.ucne.jendri_hidalgo_p2_ap2.domain.repository.GastoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGastoUseCase @Inject constructor(
    private val repository: GastoRepository
) {
    operator fun invoke(): Flow<Resource<List<Gasto>>> {
        return repository.getGasto()
    }
}