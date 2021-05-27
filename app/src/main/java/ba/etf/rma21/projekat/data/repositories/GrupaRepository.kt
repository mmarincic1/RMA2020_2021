package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GrupaRepository {
    companion object {
        init {
        }

        suspend fun getGroupsByPredmet(predmet: Predmet): List<Grupa> {
            return withContext(Dispatchers.IO){
                var pom: MutableList<Grupa> = ApiAdapter.retrofit.getGrupeZaPredmet(predmet.id) as MutableList<Grupa>
                pom.add(0, Grupa(-1, "", -1, ""))
                return@withContext pom
            }
        }
    }
}