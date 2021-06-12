package ba.etf.rma21.projekat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma21.projekat.data.models.Account

@Dao
interface AccountDao {
    @Query("SELECT * FROM account")
    suspend fun getAcc(): Account

    @Query("DELETE FROM account")
    suspend fun obrisiAcc()

    @Insert
    suspend fun insertAcc(vararg acc: Account)

    @Query("UPDATE account SET lastUpdate = :lastUpdate WHERE hashStudent = :hash")
    suspend fun updateLastUpdate(lastUpdate: String, hash:String)
}