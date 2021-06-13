package ba.etf.rma21.projekat.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.models.Predmet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DBRepository {

    companion object {
        private lateinit var context: Context
        fun getContext(): Context {
            return context
        }


        fun setContext(_context: Context) {
            context = _context
        }

        @SuppressLint("SimpleDateFormat")
        private fun getDateFormat(date: Date): String {
            val format = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
            return format.format(date)
        }

        // - vraća true ako je bilo promjene ili false ukoliko su ispravni trenutni podaci
        // ovdje se takodjer brisu stare i prave nove tabele
        suspend fun updateNow(): Boolean? {
            return withContext(Dispatchers.IO) {
                val db = AppDatabase.getInstance(AccountRepository.getContext())
                //try {
                val acc1 = AccountRepository.getAcc()
                if (acc1 != null) {
                        val rezultat =
                            acc1?.lastUpdate?.let {
                                ApiAdapter.retrofit.validniPodaci(
                                    AccountRepository.getHash(),
                                    it
                                )
                            }
                        if (rezultat != null) {
                            if (rezultat.changed!!) {
                                // OBRISI STARE
                                obrisiSveDb()
                                // URADI UPDATE
                                napraviNoveDb()
                                // upisi novi lastUpdate
                                val datum = getDateFormat(Date())
                                db.accountDao().updateLastUpdate(datum, AccountRepository.getHash())
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

        suspend fun obrisiSveDb() {
            return withContext(Dispatchers.IO) {
                val db = AppDatabase.getInstance(AccountRepository.getContext())
                db.grupaDao().obrisiDb()
                db.predmetDao().obrisiDb()
                db.kvizDao().obrisiDb()
                db.pitanjeDao().obrisiDb()
            }
        }

        suspend fun napraviNoveDb() {
            return withContext(Dispatchers.IO) {
                val db = AppDatabase.getInstance(AccountRepository.getContext())
                val grupe = ApiAdapter.retrofit.getUpisaneGrupe(AccountRepository.getHash())
                db.grupaDao().napraviDb(grupe)
                val predmeti = mutableListOf<Predmet>()
                for (grupa in grupe) {
                    predmeti.add(ApiAdapter.retrofit.getPredmetId(grupa.predmetId))
                }
                db.predmetDao().napraviDb(predmeti)

                val kvizovi = KvizRepository.getUpisani()
                db.kvizDao().napraviDb(kvizovi)

                val pitanja = mutableListOf<Pitanje>()
                for (kviz in kvizovi) {
                    val pomocnaPitanja = PitanjeKvizRepository.getPitanja(kviz.id)
                    pitanja.addAll(pomocnaPitanja)
                }
                var indexDb = 0
                pitanja.stream().forEach { x -> x.idDb = indexDb++}
                db.pitanjeDao().napraviDb(pitanja)
            }
        }
    }
}