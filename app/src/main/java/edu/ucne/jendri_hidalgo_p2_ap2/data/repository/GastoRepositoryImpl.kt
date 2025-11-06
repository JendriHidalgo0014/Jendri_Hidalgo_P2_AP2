package edu.ucne.jendri_hidalgo_p2_ap2.data.repository

import edu.ucne.jendri_hidalgo_p2_ap2.data.mapper.toDto
import edu.ucne.jendri_hidalgo_p2_ap2.data.mapper.toGasto
import edu.ucne.jendri_hidalgo_p2_ap2.data.remote.ApiService
import edu.ucne.jendri_hidalgo_p2_ap2.data.remote.Resource
import edu.ucne.jendri_hidalgo_p2_ap2.domain.model.Gasto
import edu.ucne.jendri_hidalgo_p2_ap2.domain.repository.GastoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GastoRepositoryImpl @Inject constructor(
    private val api: ApiService
) : GastoRepository {

    override fun getGasto(): Flow<Resource<List<Gasto>>> = flow {
        try {
            emit(Resource.Loading())
            val response = api.getGasto()

            if (response.isSuccessful) {
                val gastos = response.body()?.map { it.toGasto() } ?: emptyList()
                emit(Resource.Success(gastos))
            } else {
                emit(Resource.Error("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    override fun getGasto(id: Int): Flow<Resource<Gasto>> = flow {
        try {
            emit(Resource.Loading())
            val response = api.getGasto(id)

            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it.toGasto()))
                } ?: emit(Resource.Error("Gasto no encontrado"))
            } else {
                emit(Resource.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    override fun createGasto(gasto: Gasto): Flow<Resource<Gasto>> = flow {
        try {
            emit(Resource.Loading())
            val response = api.createGasto(gasto.toDto())

            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it.toGasto()))
                } ?: emit(Resource.Error("Error al crear"))
            } else {
                emit(Resource.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    override fun updateGasto(id: Int, gasto: Gasto): Flow<Resource<Gasto>> = flow {
        try {
            emit(Resource.Loading())
            val response = api.updateGasto(id, gasto.toDto())

            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it.toGasto()))
                } ?: emit(Resource.Error("Error al actualizar"))
            } else {
                emit(Resource.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

}