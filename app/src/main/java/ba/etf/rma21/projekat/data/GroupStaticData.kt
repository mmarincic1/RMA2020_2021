package ba.etf.rma21.projekat.data

import ba.etf.rma21.projekat.data.models.Grupa

import java.util.*

fun groups(): List<Grupa>{
    // naziv grupe i naziv predmeta
    return listOf(
        Grupa("G1", "IM"),
        Grupa("G2", "TP"),
        Grupa("G3", "RPR"),
        Grupa("G4", "RMA"),
        Grupa("G5", "OOAD")
    )
}