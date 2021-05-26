package ba.etf.rma21.projekat.data.models

import com.google.gson.annotations.SerializedName

// id	integer
//The Auto-generated id

//odgovoreno*	integer
//indeks odgovora

class Odgovor(
    @SerializedName("id") val id: Int,
    @SerializedName("odgovoreno") val odgovoreno: Int
) {

}
