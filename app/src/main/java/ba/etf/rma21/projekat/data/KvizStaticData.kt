package ba.etf.rma21.projekat.data

import ba.etf.rma21.projekat.data.models.Kviz
import java.util.*

fun quizzes(): List<Kviz>{
    // val naziv: String, val nazivPredmeta: String, val datumPocetka: Date, val datumKraj: Date,
    //    val datumRada: Date, val trajanje: Int, val nazivGrupe: String, val osvojeniBodovi: Float
    return listOf(
        Kviz("Kviz 0", "IM", Date(2021, 3, 20), Date(2021, 3, 22),
            null, 30, "G1", null
    ),
        Kviz("Kviz 1", "TP", Date(2021, 4, 22), Date(2021, 4, 23),
        null, 3, "G2", null
    ),
        Kviz("Kviz 2", "RPR", Date(2021, 3, 21), Date(2021, 3, 23),
        Date(2021, 3, 22), 7, "G3", 1F
    ),
        Kviz("Kviz 3", "RMA", Date(2021, 3, 30), Date(2021, 4, 22),
        null, 7, "G4", null
    ),
        Kviz("Kviz 4", "OOAD", Date(2021, 4, 7), Date(2021, 4, 8),
            null, 13, "G5", null
        )
    );
}