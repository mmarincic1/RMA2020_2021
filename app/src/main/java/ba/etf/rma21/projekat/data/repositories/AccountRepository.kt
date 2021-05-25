package ba.etf.rma21.projekat.data.repositories

class AccountRepository {
    private lateinit var studentHash: String

    // lokalno a ne na web
    fun postaviHash(acHash:String):Boolean{
        studentHash = acHash
        return true
    }

    fun getHash():String{
        return studentHash
    }
}