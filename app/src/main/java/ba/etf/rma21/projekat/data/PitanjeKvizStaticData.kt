package ba.etf.rma21.projekat.data


import ba.etf.rma21.projekat.data.models.PitanjeKviz

// - naziv: String - jedinstveni naziv pitanja u okviru kvizova u kojima se nalazi
// - kviz: String - jedinstveni naziv kviza

// 2 kviza imaju 4 pitanja i jedan kviz ima 3 pitanja
fun pitanjaKvizovi(): List<PitanjeKviz>{
    return listOf(
        PitanjeKviz("pitanje0", "Kviz 0"),
        PitanjeKviz("pitanje1", "Kviz 0"),
        PitanjeKviz("pitanje2", "Kviz 0"),
        PitanjeKviz("pitanje3", "Kviz 0"),
        PitanjeKviz("pitanje3", "Kviz 1"),
        PitanjeKviz("pitanje4", "Kviz 1"),
        PitanjeKviz("pitanje5", "Kviz 1"),
        PitanjeKviz("pitanje5", "Kviz 2"),
        PitanjeKviz("pitanje6", "Kviz 2"),
        PitanjeKviz("pitanje7", "Kviz 2"),
        PitanjeKviz("pitanje8", "Kviz 2")
    )
}