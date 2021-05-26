package ba.etf.rma21.projekat.data.models

import com.google.gson.annotations.SerializedName


// - naziv: String - jedinstveni naziv pitanja u okviru kvizova u kojima se nalazi
// - tekst: String - tekst pitanja
// - opcije: List<String> - lista ponuđenih odgovora (3 odgovora su ponuđena)
// - tacan: Int - indeks tačnog odgovora u listi ponuđenih
// example: OrderedMap { "id": 1, "naziv": "P1", "tekstPitanja": "Koji je prvi odgovor?", "opcije": List [ "A", "B", "C" ], "tacan": 0 }]
class Pitanje(
    @SerializedName("id") val id: Int,
    @SerializedName("naziv") val naziv: String,
    @SerializedName("tekstPitanja") val tekstPitanja: String,
    @SerializedName("opcije") val opcije: List<String>,
    @SerializedName("tacan") val tacan: Int
) {

}