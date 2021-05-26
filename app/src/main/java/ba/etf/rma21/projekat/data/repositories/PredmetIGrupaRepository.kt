package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PredmetIGrupaRepository {
    companion object {
        // - vraća sve predmete
        suspend fun getPredmeti(): List<Predmet> {
            return withContext(Dispatchers.IO) {
                return@withContext ApiAdapter.retrofit.getPredmeti()
            }
        }

        // - vraća sve grupe
        suspend fun getGrupe(): List<Grupa> {
            return withContext(Dispatchers.IO) {
                return@withContext ApiAdapter.retrofit.getGrupe()
            }
        }

        // - vraća grupe na predmetu sa id-em idPredmeta
        suspend fun getGrupeZaPredmet(idPredmeta: Int): List<Grupa> {
            return withContext(Dispatchers.IO) {
                return@withContext ApiAdapter.retrofit.getGrupeZaPredmet(idPredmeta)
            }
        }

//    // - upisuje studenta u grupu sa id-em idGrupa i vraća true ili vraća false ako nije moguće upisati studenta
//    suspend fun upisiUGrupu(idGrupa:Int):Boolean{
//
//    }

        //- vraća grupe u kojima je student upisan
        suspend fun getUpisaneGrupe(): List<Grupa> {
            return withContext(Dispatchers.IO) {
                val acc = AccountRepository()
                return@withContext ApiAdapter.retrofit.getUpisaneGrupe(acc.getHash())
            }
        }
    }
}