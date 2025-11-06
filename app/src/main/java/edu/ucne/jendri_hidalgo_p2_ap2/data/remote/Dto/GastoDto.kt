package edu.ucne.jendri_hidalgo_p2_ap2.data.remote.Dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GastoDto(
    @Json(name = "gastoId")
    val gastoId: Int = 0,
    @Json(name = "fecha")
    val fecha: String,
    @Json(name = "suplidor")
    val suplidor: String,
    @Json(name = "ncf")
    val ncf: String,
    @Json(name = "itbis")
    val itbis: Double,
    @Json(name = "monto")
    val monto: Double
)