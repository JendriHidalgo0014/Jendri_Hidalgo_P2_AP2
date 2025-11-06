package edu.ucne.jendri_hidalgo_p2_ap2.data.remote.Dto

import com.squareup.moshi.Json

class GastoDto (

    @Json
    val gastoId: Int,
    @Json
    val fecha: String,
    @Json
    val suplidor: String,
    @Json
    val ncf: String,
    @Json
    val itbis: Double,
    @Json
    val monto: Double
)