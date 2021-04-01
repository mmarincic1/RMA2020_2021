package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.predmeti
import java.util.stream.Collectors

class PredmetRepository {
    companion object {

        private lateinit var upisani: MutableList<Predmet>

        init {
            upisani = mutableListOf()
        }

        fun getUpisani(): List<Predmet>{
            if(upisani.size == 0)
                return emptyList()
            return upisani;
        }

        fun addUpisani(godina: Int, naziv: String): Unit{
            upisani.add(getAll().stream().filter{ predmet -> predmet.godina == godina && predmet.naziv == naziv}.findFirst().get())
        }

        fun getAll(): List<Predmet> {
            return predmeti()
        }

        fun getFromYear(godina: Int): List<String> {
            val pom =  getAll().stream().filter { predmet -> predmet.godina == godina }.collect(
                Collectors.toList())
            var pom1 = mutableListOf<String>()
            for(pomcic in pom){
                pom1.add(pomcic.toString())
            }
            return pom1
        }

    }

}