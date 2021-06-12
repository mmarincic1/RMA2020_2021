package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Predmet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DBRepository {

    // - vraća true ako je bilo promjene ili false ukoliko su ispravni trenutni podaci
    // ovdje se takodjer brisu stare i prave nove tabele
    suspend fun updateNow(): Boolean? {
        return withContext(Dispatchers.IO) {
            //try {
            val acc = AccountRepository()
            val acc1 = acc.getAcc()
            if (acc1 != null) {
                if (acc1.lastUpdate == "") {
                    // OBRISI STARE
                    obrisiSveDb()
                    // URADI UPDATE
                    napraviNoveDb()
                    //
                    return@withContext true
                }
            } else {
                val rezultat =
                    acc1?.lastUpdate?.let { ApiAdapter.retrofit.validniPodaci(acc.getHash(), it) }
                if (rezultat != null) {
                    if (rezultat.changed!!) {
                        // OBRISI STARE
                        obrisiSveDb()
                        // URADI UPDATE
                        napraviNoveDb()
                        //
                        return@withContext true
                    } else return@withContext false
                }
                return@withContext false
//            }catch (e: Exception){
//                return@withContext false
//            }
            }
            return@withContext false
        }
    }

    suspend fun obrisiSveDb(){
        return withContext(Dispatchers.IO) {
            val db = AppDatabase.getInstance(AccountRepository.getContext())
            db.grupaDao().obrisiDb()
            db.predmetDao().obrisiDb()
        }
    }

    suspend fun napraviNoveDb(){
        return withContext(Dispatchers.IO) {
            val db = AppDatabase.getInstance(AccountRepository.getContext())
            val acc = AccountRepository()
            val grupe = ApiAdapter.retrofit.getUpisaneGrupe(acc.getHash())
            db.grupaDao().napraviDb(grupe)
            var predmeti = mutableListOf<Predmet>()
            for(grupa in grupe){
                predmeti.add(ApiAdapter.retrofit.getPredmetId(grupa.predmetId))
            }
            db.predmetDao().napraviDb(predmeti)
        }
    }
}