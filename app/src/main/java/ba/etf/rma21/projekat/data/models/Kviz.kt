package ba.etf.rma21.projekat.data.models

import java.util.*

data class Kviz(
    val naziv: String, val nazivPredmeta: String, val datumPocetka: Date, val datumKraj: Date,
    var datumRada: Date?, val trajanje: Int, val nazivGrupe: String, var osvojeniBodovi: Float?
) {
    private var status = ""

    fun setStatus(status: String){
        this.status = status
    }

    fun getStatus(): String{
        return status
    }

}