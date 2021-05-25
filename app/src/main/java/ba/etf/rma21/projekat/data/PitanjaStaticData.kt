package ba.etf.rma21.projekat.data

import ba.etf.rma21.projekat.data.models.Pitanje

// - naziv: String - jedinstveni naziv pitanja u okviru kvizova u kojima se nalazi
// - tekst: String - tekst pitanja
// - opcije: List<String> - lista ponuđenih odgovora (3 odgovora su ponuđena)
// - tacan: Int - indeks tačnog odgovora u listi ponuđenih

fun getPitanja(): List<Pitanje>{
//    return listOf(
//        Pitanje("pitanje0", "Ko je Iron Man?", listOf("Phill Coulson", "Tony Stark", "Pepper Potts"), 1),
//        Pitanje("pitanje1", "Ko je ubio Thanosa?", listOf("Iron Man", "Thor", "Hulk"), 1),
//        Pitanje("pitanje2", "Koga je Iron Man najviše poželio poslije \"Blica\"?", listOf("Hulka", "Samoga sebe", "Petera"), 2),
//        Pitanje("pitanje3", "Koje je zračenje napravilo Hulka?", listOf("Alfa", "Beta", "Gama"), 2),
//        Pitanje("pitanje4", "Ko je uspio podići Thorov čekić?", listOf("Cap. America", "Tony Stark", "Spiderman"), 0),
//        Pitanje("pitanje5", "Ko govori \"Grooot\"?", listOf("Groot", "Hulk", "Hawkeye"), 0),
//        Pitanje("pitanje6", "Šta skupljaju Avengersi u posljednjem dijelu?", listOf("infinity stones", "dark stones", "cool stones"), 0),
//        Pitanje("pitanje7", "Koliko puta je umro Loki?", listOf("1", "infinity", "0"), 1),
//        Pitanje("pitanje8", "Ko se vratio u prošlost i ostao?", listOf("Spiderman", "Tony Stark", "Cap.America"), 2)
//    )
    return emptyList()
}