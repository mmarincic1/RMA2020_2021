package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.predmeti
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.stream.Collectors

class PredmetRepository {
    companion object {

        private var upisani: MutableList<Predmet>

        init {
            upisani = mutableListOf()
        }

        suspend fun getPredmetsByGodina(godina: Int): List<Predmet>{

            return withContext(Dispatchers.IO){
                var pom = ApiAdapter.retrofit.getPredmetsByGodina()
                var rezultat = mutableListOf<Predmet>()
                rezultat.add(Predmet(-1, "", -1))
                val acc = AccountRepository()
                // ubaci sve predmete
                for(predmet in pom) {
                    if(predmet.godina == godina)
                        rezultat.add(predmet)
                }
                // izbaci upisane predmete
                var grupe = ApiAdapter.retrofit.getUpisaneGrupe(acc.getHash())
                for(grupa in grupe){
                    val naziv = ApiAdapter.retrofit.getPredmetId(grupa.predmetId).naziv
                    var brojac = 0;
                    if(rezultat.stream().anyMatch{x -> x.naziv == naziv}){
                        rezultat.removeAt(brojac+1)
                        brojac++
                    }
                }
                return@withContext rezultat
            }
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

    }

}