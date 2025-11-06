package edu.ucne.jendri_hidalgo_p2_ap2.data.mapper

import edu.ucne.jendri_hidalgo_p2_ap2.data.remote.Dto.GastoDto
import edu.ucne.jendri_hidalgo_p2_ap2.domain.model.Gasto

fun GastoDto.toGasto(): Gasto {
    return Gasto(
        gastoId = gastoId,
        fecha = fecha,
        suplidor = suplidor,
        ncf = ncf,
        itbis = itbis,
        monto = monto
    )
}

fun Gasto.toDto(): GastoDto {
    return GastoDto(
        gastoId = gastoId,
        fecha = fecha,
        suplidor = suplidor,
        ncf = ncf,
        itbis = itbis,
        monto = monto
    )
}