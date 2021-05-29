package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.viewmodel.KvizListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class TakeKvizRepository {
    companion object {
        // - kreira pokušaj za kviz, vraća kreirani pokušaj ili null u slučaju greške
        suspend fun zapocniKviz(idKviza: Int): KvizTaken? {
            return withContext(Dispatchers.IO) {
                val acc = AccountRepository()
                try {
                    val rezultat = ApiAdapter.retrofit.zapocniKviz(idKviza, acc.getHash())
                    return@withContext rezultat
                }catch (e: Exception){
                    return@withContext null
                }
            }
        }

        //- vraća listu pokušaja ili null ukoliko student nema niti jedan započeti kviz
        suspend fun getPocetiKvizovi(): List<KvizTaken> {
            return withContext(Dispatchers.IO) {
                val acc = AccountRepository()
                return@withContext ApiAdapter.retrofit.getPocetiKvizovi(acc.getHash())
            }
        }

        suspend fun getPocetiKvizoviApp(): Boolean{
            return withContext(Dispatchers.IO) {
                val acc = AccountRepository()
                val pokrenutiKvizovi = ApiAdapter.retrofit.getPocetiKvizovi(acc.getHash())
                var imaGa = false
                for(pKviz in pokrenutiKvizovi){
                    if(pKviz.KvizId == KvizRepository.pokrenutiKviz.id){
                        KvizRepository.radjeniKviz = pKviz
                        imaGa = true
                        break
                    }
                }
                if(!imaGa){
                    val kviz = ApiAdapter.retrofit.zapocniKviz(KvizRepository.pokrenutiKviz.id, acc.getHash())
                    KvizRepository.radjeniKviz = kviz
                }
                return@withContext true
            }
        }
    }
}