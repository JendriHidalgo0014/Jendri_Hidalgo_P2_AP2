package edu.ucne.jendri_hidalgo_p2_ap2.domain.model

data class Gasto(
    val gastoId: Int,
    val fecha: String,
    val suplidor: String,
    val ncf: String,
    val itbis: Double,
    val monto: Double
)
