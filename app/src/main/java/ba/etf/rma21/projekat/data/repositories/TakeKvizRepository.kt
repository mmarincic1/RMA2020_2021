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
                try {
                    val rezultat = ApiAdapter.retrofit.zapocniKviz(idKviza, AccountRepository.getHash())
                    return@withContext rezultat
                }catch (e: Exception){
                    return@withContext null
                }
            }
        }

        //- vraća listu pokušaja ili null ukoliko student nema niti jedan započeti kviz
        suspend fun getPocetiKvizovi(): List<KvizTaken>? {
            return withContext(Dispatchers.IO) {
                try {
                    val rezultat = ApiAdapter.retrofit.getPocetiKvizovi(AccountRepository.getHash())
                    if(rezultat.isEmpty())
                        return@withContext null
                    return@withContext rezultat
                }catch(e: Exception){
                    return@withContext null
                }

            }
        }

        suspend fun getPocetiKvizoviApp(): Boolean{
            return withContext(Dispatchers.IO) {
                val pokrenutiKvizovi = ApiAdapter.retrofit.getPocetiKvizovi(AccountRepository.getHash())
                var imaGa = false
                for(pKviz in pokrenutiKvizovi){
                    if(pKviz.KvizId == KvizRepository.pokrenutiKviz.id){
                        KvizRepository.radjeniKviz = pKviz
                        imaGa = true
                        break
                    }
                }
                if(!imaGa){
                    val kviz = ApiAdapter.retrofit.zapocniKviz(KvizRepository.pokrenutiKviz.id, AccountRepository.getHash())
                    KvizRepository.radjeniKviz = kviz
                }
                return@withContext true
            }
        }
    }
}