package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.groups
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet
import java.util.stream.Collectors

class GrupaRepository {
    companion object {
        init {
        }

        fun getGroupsByPredmetString(nazivPredmeta: String): List<String> {
            val pom = getAll().stream().filter { grupa -> grupa.nazivPredmeta == nazivPredmeta }.collect(
                Collectors.toList())
            var pom1 = mutableListOf<String>()
            for(pomcic in pom)
                pom1.add(pomcic.toString())
            return pom1
        }

        fun getGroupsByPredmet(nazivPredmeta: String): List<Grupa> {
            val pom =  getAll().stream().filter { predmet -> predmet.nazivPredmeta == nazivPredmeta }.collect(
                Collectors.toList())
            if(pom.size == 0) return emptyList()
            return pom
        }

        fun getAll(): List<Grupa>{
            return groups()
        }
    }
}