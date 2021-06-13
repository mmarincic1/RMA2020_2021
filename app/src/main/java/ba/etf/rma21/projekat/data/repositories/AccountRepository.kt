package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRepository {
    companion object {
        var acHash: String = "0934422d-53e3-4817-b211-1964211c912d"
        private lateinit var context: Context
        fun getContext(): Context{
            return context
        }
    }

    fun setContext(_context:Context){
        context=_context
    }



    // u bazu
    // OVDJE CU MORATI OBRISATI I OSTALE TABELE U BAZI,
    // TAKODJER VJEROVATNO CU MORATI PROMIJENITI DA NE DOHVACA ACC SA SERVERA!!!
    suspend fun postaviHash(accHash:String): Boolean{
        return withContext(Dispatchers.IO){
            try{
                val db = AppDatabase.getInstance(context)
                db.accountDao().obrisiAcc()
                val acc = ApiAdapter.retrofit.getAcc(accHash)
                acc.lastUpdate = ""
                db.accountDao().insertAcc(acc)
                return@withContext true
            }catch (e: Exception){
                return@withContext false
            }
        }
    }

    // ovo POPRAVI da vrac iz BAZE
    fun getHash():String{
        //acHash = "0934422d-53e3-4817-b211-1964211c912d" // NEMOJ ZABORAVITI OBRISATI !!!!!!!
        return acHash
    }

    suspend fun getAcc(): Account?{
        return withContext(Dispatchers.IO){
            try{
                val db = AppDatabase.getInstance(context)
                val rez = db.accountDao().getAcc()
                return@withContext rez
            }catch (e: Exception){
                return@withContext null
            }
        }
    }
}