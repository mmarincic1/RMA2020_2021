package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Predmet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.stream.Collectors

class PredmetRepository {
    companion object {

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

    }

}