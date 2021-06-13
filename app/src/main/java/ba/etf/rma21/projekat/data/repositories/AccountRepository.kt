package ba.etf.rma21.projekat.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AccountRepository {
    companion object {
        var acHash: String = "0934422d-53e3-4817-b211-1964211c912d"
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

        // u bazu
        // OVDJE CU MORATI OBRISATI I OSTALE TABELE U BAZI,
        // TAKODJER VJEROVATNO CU MORATI PROMIJENITI DA NE DOHVACA ACC SA SERVERA!!!
        suspend fun postaviHash(accHash: String): Boolean {
            return withContext(Dispatchers.IO) {
                acHash = accHash // BOZE SACUVAJ AL ETO
                try {
                    val db = AppDatabase.getInstance(context)
                    db.accountDao().obrisiAcc()
                    try {
                        db.accountDao().obrisiAcc()
                        val acc = ApiAdapter.retrofit.getAcc(accHash)
                        acc.lastUpdate = getDateFormat(Date())
                        db.accountDao().insertAcc(acc)
                    }catch (e: Exception){
                        db.accountDao().obrisiAcc()
                        // znaci nema ga na serveru
                        val acc = Account(0, "", accHash, getDateFormat(Date()))
                        db.accountDao().insertAcc(acc)
                    }
                    // brisem sve stare
                    db.odgovorDao().obrisiDb()
                    db.kvizDao().obrisiDb()
                    db.grupaDao().obrisiDb()
                    db.pitanjeDao().obrisiDb()
                    db.predmetDao().obrisiDb()
                    return@withContext true
                } catch (e: Exception) {
                    return@withContext false
                }
            }
        }

        // ovo POPRAVI da vrac iz BAZE
        fun getHash(): String {
            return acHash
        }

        suspend fun getAcc(): Account? {
            return withContext(Dispatchers.IO) {
                try {
                    val db = AppDatabase.getInstance(context)
                    val rez = db.accountDao().getAcc()
                    return@withContext rez
                } catch (e: Exception) {
                    return@withContext null
                }
            }
        }
    }

}