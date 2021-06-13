package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PredmetIGrupaRepository {
    companion object {
        private lateinit var context: Context
        fun getContext(): Context {
            return context
        }


        fun setContext(_context: Context) {
            context = _context
        }
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

    // - upisuje studenta u grupu sa id-em idGrupa i vraća true ili vraća false ako nije moguće upisati studenta
    suspend fun upisiUGrupu(idGrupa:Int):Boolean{
        return withContext(Dispatchers.IO) {
            val odgovor = ApiAdapter.retrofit.upisiUGrupu(idGrupa, AccountRepository.getHash())
            var rezultat = true
            if(odgovor.message.contains("not found") ||
                odgovor.message.contains("Ne postoji account"))
                rezultat = false
            return@withContext rezultat
        }
    }

        //- vraća grupe u kojima je student upisan
        suspend fun getUpisaneGrupe(): List<Grupa> {
            return withContext(Dispatchers.IO) {
                return@withContext ApiAdapter.retrofit.getUpisaneGrupe(AccountRepository.getHash())
            }
        }
    }
}