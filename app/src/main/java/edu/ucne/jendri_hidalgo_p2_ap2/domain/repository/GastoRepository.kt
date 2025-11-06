package edu.ucne.jendri_hidalgo_p2_ap2.domain.repository

import edu.ucne.jendri_hidalgo_p2_ap2.data.remote.Resource
import edu.ucne.jendri_hidalgo_p2_ap2.domain.model.Gasto
import kotlinx.coroutines.flow.Flow

interface GastoRepository {

    fun getGasto(): Flow<Resource<List<Gasto>>>

    fun getGasto(id: Int): Flow<Resource<Gasto>>

    fun createGasto(pedido: Gasto): Flow<Resource<Gasto>>

    fun updateGasto(id: Int, pedido: Gasto): Flow<Resource<Gasto>>
}