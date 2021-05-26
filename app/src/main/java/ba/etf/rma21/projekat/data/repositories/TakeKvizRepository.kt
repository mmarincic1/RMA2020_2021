package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.KvizTaken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TakeKvizRepository {
    companion object {
        // - kreira pokušaj za kviz, vraća kreirani pokušaj ili null u slučaju greške
        suspend fun zapocniKviz(idKviza: Int): KvizTaken {
            return withContext(Dispatchers.IO) {
                val acc = AccountRepository()
                return@withContext ApiAdapter.retrofit.zapocniKviz(idKviza, acc.getHash())
            }
        }

        //- vraća listu pokušaja ili null ukoliko student nema niti jedan započeti kviz
        suspend fun getPocetiKvizovi(): List<KvizTaken> {
            return withContext(Dispatchers.IO) {
                val acc = AccountRepository()
                return@withContext ApiAdapter.retrofit.getPocetiKvizovi(acc.getHash())
            }
        }
    }
}