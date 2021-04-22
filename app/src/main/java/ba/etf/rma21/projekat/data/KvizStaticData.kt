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
        Kviz("Kviz 1", "IM", Date(2021, 3, 20), Date(2021, 3, 22),
            null, 30, "G2", null
        ),
        Kviz("Kviz 2", "IM", Date(2021, 3, 20), Date(2021, 3, 22),
            null, 30, "G3", null
        ),
        Kviz("Kviz 0", "UUP", Date(2022, 4, 22), Date(2022, 4, 23),
            null, 5, "G1", null
        ),
        Kviz("Kviz 1", "UUP", Date(2022, 4, 22), Date(2022, 4, 23),
            null, 5, "G2", null
        ),
        Kviz("Kviz 2", "UUP", Date(2022, 4, 22), Date(2022, 4, 23),
            null, 5, "G3", null
        ),
        Kviz("Kviz 0", "TP", Date(2022, 4, 22), Date(2022, 4, 23),
        null, 3, "G1", null
    ),
        Kviz("Kviz 1", "TP", Date(2022, 4, 22), Date(2022, 4, 23),
            null, 3, "G2", null
        ),
        Kviz("Kviz 2", "TP", Date(2022, 4, 22), Date(2022, 4, 23),
            null, 3, "G3", null
        ),
        Kviz("Kviz 0", "RPR", Date(2022, 3, 21), Date(2022, 3, 23),
        Date(2021, 3, 22), 7, "G1", 1F
    ),
        Kviz("Kviz 1", "RPR", Date(2022, 3, 21), Date(2022, 3, 23),
            Date(2021, 3, 22), 7, "G2", 1F
        ),
        Kviz("Kviz 2", "RPR", Date(2022, 3, 21), Date(2022, 3, 23),
            Date(2021, 3, 22), 7, "G3", 1F
        ),
        Kviz("Kviz 0", "RMA", Date(2021, 3, 30), Date(2022, 4, 22),
        null, 7, "G1", null
    ),
        Kviz("Kviz 1", "RMA", Date(2021, 3, 30), Date(2022, 4, 22),
            null, 7, "G2", null
        ),
        Kviz("Kviz 2", "RMA", Date(2021, 3, 30), Date(2022, 4, 22),
            null, 7, "G3", null
        ),
        Kviz("Kviz 0", "OOAD", Date(2022, 4, 7), Date(2022, 4, 8),
            null, 13, "G1", null
        ),
        Kviz("Kviz 1", "OOAD", Date(2022, 4, 7), Date(2022, 4, 8),
            null, 13, "G2", null
        ),
        Kviz("Kviz 2", "OOAD", Date(2022, 4, 7), Date(2022, 4, 8),
            null, 13, "G3", null
        ),
        Kviz("Kviz 0", "DONE", Date(2021, 3, 21), Date(2021, 3, 23),
            Date(2021, 3, 22), 17, "G1", 10F
        )
    );
}