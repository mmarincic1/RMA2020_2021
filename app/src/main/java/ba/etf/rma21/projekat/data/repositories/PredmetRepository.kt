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

        fun getPredmetsByGodina(godina: Int): List<Predmet>{
            return getAll().stream().filter { predmet -> predmet.godina == godina }.collect(
                Collectors.toList())
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
            val pom =  izdvojiVecUpisane(getPredmetsByGodina(godina))
            var pom1 = mutableListOf<String>()
            for(pomcic in pom){
                pom1.add(pomcic.toString())
            }
            return pom1
        }

        fun izdvojiVecUpisane(predmeti: List<Predmet>): List<Predmet>{
            var pom1 = mutableListOf<Predmet>()
            for(predmetic in predmeti){
                var nemaGa = true
                for(predmetic1 in upisani){
                    if(predmetic.equals(predmetic1)) nemaGa = false
                }
                if(nemaGa) pom1.add(predmetic)
            }
            return pom1
        }
    }

}