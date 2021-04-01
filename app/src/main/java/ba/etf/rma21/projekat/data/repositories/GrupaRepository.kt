package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.groups
import ba.etf.rma21.projekat.data.models.Grupa
import java.util.stream.Collectors

class GrupaRepository {
    companion object {
        init {
            // TODO: Implementirati
        }

        fun getGroupsByPredmet(nazivPredmeta: String): List<String> {
            val pom = getAll().stream().filter { grupa -> grupa.nazivPredmeta == nazivPredmeta }.collect(
                Collectors.toList())
            var pom1 = mutableListOf<String>()
            for(pomcic in pom)
                pom1.add(pomcic.toString())
            return pom1
        }

        fun getAll(): List<Grupa>{
            return groups()
        }
    }
}