package ba.etf.rma21.projekat.data.models

import com.google.gson.annotations.SerializedName

data class Predmet(
    @SerializedName("id") val id: Int,
    @SerializedName("naziv") val naziv: String,
    @SerializedName("godina") val godina: Int) {

    override fun toString(): String {
        return "$naziv"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Predmet

        if (naziv != other.naziv) return false
        if (godina != other.godina) return false

        return true
    }

    override fun hashCode(): Int {
        var result = naziv.hashCode()
        result = 31 * result + godina
        return result
    }

}