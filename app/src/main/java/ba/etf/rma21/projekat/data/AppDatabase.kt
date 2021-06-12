package ba.etf.rma21.projekat.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ba.etf.rma21.projekat.data.dao.AccountDao
import ba.etf.rma21.projekat.data.dao.GrupaDao
import ba.etf.rma21.projekat.data.dao.KvizDao
import ba.etf.rma21.projekat.data.dao.PredmetDao
import ba.etf.rma21.projekat.data.models.Account
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Predmet
import kotlin.coroutines.CoroutineContext

@Database(entities = arrayOf(Account::class, Grupa::class, Predmet::class, Kviz::class), version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun accountDao(): AccountDao
    abstract fun grupaDao(): GrupaDao
    abstract fun predmetDao(): PredmetDao
    abstract fun kvizDao(): KvizDao

    companion object{
        private var INSTANCE: AppDatabase? = null

        fun setInstance(appdb:AppDatabase):Unit{
            INSTANCE=appdb
        }

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = buildRoomDB(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "RMA21DB"
            ).build()
    }
}