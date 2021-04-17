package ba.etf.rma21.projekat.data


import ba.etf.rma21.projekat.data.models.PitanjeKviz

// - naziv: String - jedinstveni naziv pitanja u okviru kvizova u kojima se nalazi
// - kviz: String - jedinstveni naziv kviza

fun pitanjaKvizovi(): List<PitanjeKviz>{
    return listOf(
        PitanjeKviz("pitanje0", "kviz0"),
        PitanjeKviz("pitanje1", "kviz0"),
        PitanjeKviz("pitanje2", "kviz0"),
        PitanjeKviz("pitanje3", "kviz1"),
        PitanjeKviz("pitanje4", "kviz1"),
        PitanjeKviz("pitanje5", "kviz1"),
        PitanjeKviz("pitanje6", "kviz2"),
        PitanjeKviz("pitanje7", "kviz2"),
        PitanjeKviz("pitanje8", "kviz2")
    )
}