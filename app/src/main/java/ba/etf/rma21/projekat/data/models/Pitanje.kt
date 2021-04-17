package ba.etf.rma21.projekat.data.models


// - naziv: String - jedinstveni naziv pitanja u okviru kvizova u kojima se nalazi
// - tekst: String - tekst pitanja
// - opcije: List<String> - lista ponuđenih odgovora (3 odgovora su ponuđena)
// - tacan: Int - indeks tačnog odgovora u listi ponuđenih

class Pitanje(val naziv: String, val tekst: String, val opcije: List<String>, val tacan: Int) {

}