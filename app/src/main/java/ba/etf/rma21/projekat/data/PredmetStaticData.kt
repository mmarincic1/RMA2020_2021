package ba.etf.rma21.projekat.data

import ba.etf.rma21.projekat.data.models.Predmet

fun predmeti(): List<Predmet>{
    // naziv predmeta i godina
    return listOf(
        Predmet("IM", 1),
        Predmet("TP", 1),
        Predmet("RPR", 2),
        Predmet("RMA", 2),
        Predmet("OOAD", 2)
    )
}