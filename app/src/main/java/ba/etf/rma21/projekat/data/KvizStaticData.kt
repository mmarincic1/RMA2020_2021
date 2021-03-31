package ba.etf.rma21.projekat.data

import ba.etf.rma21.projekat.data.models.Kviz
import java.util.*

fun quizzes(): List<Kviz>{
    // val naziv: String, val nazivPredmeta: String, val datumPocetka: Date, val datumKraj: Date,
    //    val datumRada: Date, val trajanje: Int, val nazivGrupe: String, val osvojeniBodovi: Float
    return listOf(
        Kviz("Kviz 0", "IM", Date(2021, 4, 20), Date(2021, 4, 20),
            Date(2021, 4, 20), 30, "G1", 2.5F
    ),
        Kviz("Kviz 1", "TP", Date(2021, 4, 22), Date(2021, 4, 22),
        Date(2021, 4, 22), 3, "G2", 5F
    ),
        Kviz("Kviz 2", "RPR", Date(2021, 5, 1), Date(2021, 5, 1),
        Date(2021, 5, 1), 7, "G3", 1F
    )
    );
}