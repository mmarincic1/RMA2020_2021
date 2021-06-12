package ba.etf.rma21.projekat.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DBRepository {

    // - vraća true ako je bilo promjene ili false ukoliko su ispravni trenutni podaci
    // ovdje se takodjer brisu stare i prave nove tabele
    suspend fun updateNow(): Boolean? {
        return withContext(Dispatchers.IO){
            try {
                val acc = AccountRepository()
                val acc1 = acc.getAcc()
                val rezultat =
                    acc1?.lastUpdate?.let { ApiAdapter.retrofit.validniPodaci(acc.getHash(), it) }
                if(rezultat!!){
                    // OBRISI STARE
                    // URADI UPDATE
                    return@withContext true
                }
                else return@withContext false
            }catch (e: Exception){
                return@withContext false
            }
        }
    }
}