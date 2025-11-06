package edu.ucne.jendri_hidalgo_p2_ap2.data.remote

import edu.ucne.jendri_hidalgo_p2_ap2.data.remote.Dto.GastoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("api/Gastos")
    suspend fun getGasto(): Response<List<GastoDto>>

    @GET("api/Gastos/{id}")
    suspend fun getGasto(@Path("id") id: Int): Response<GastoDto>

    @POST("api/Gastos")
    suspend fun createGasto(@Body gasto: GastoDto): Response<GastoDto>

    @PUT("api/Gastos/{id}")
    suspend fun updateGasto(
        @Path("id") id: Int,
        @Body gasto: GastoDto
    ): Response<GastoDto>

}