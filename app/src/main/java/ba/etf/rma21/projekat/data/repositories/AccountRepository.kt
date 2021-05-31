package ba.etf.rma21.projekat.data.repositories

class AccountRepository {
    companion object {
        var acHash: String = "0934422d-53e3-4817-b211-1964211c912d"
    }
    // lokalno a ne na web
    fun postaviHash(accHash:String):Boolean{
        try {
            acHash = accHash
            return true
        }catch (e: Exception){
            return false
        }
    }

    fun getHash():String{
        //acHash = "0934422d-53e3-4817-b211-1964211c912d" // NEMOJ ZABORAVITI OBRISATI !!!!!!!
        return acHash
    }
}