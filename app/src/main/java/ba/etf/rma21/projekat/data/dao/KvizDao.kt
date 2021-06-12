package ba.etf.rma21.projekat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ba.etf.rma21.projekat.data.models.Kviz

@Dao
interface KvizDao {
    @Query("DELETE FROM kviz")
    suspend fun obrisiDb()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun napraviDb(kvizovi: List<Kviz>)

    @Query("SELECT * FROM kviz")
    suspend fun dajSveKvizoveDb(): List<Kviz>
}