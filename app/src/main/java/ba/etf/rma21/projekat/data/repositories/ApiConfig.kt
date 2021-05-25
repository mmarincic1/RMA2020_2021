package ba.etf.rma21.projekat.data.repositories

class ApiConfig {
    private var Url: String  =  "https://rma21-etf.herokuapp.com"

    fun postaviBaseURL(baseUrl:String):Unit{
        Url = baseUrl
    }

    fun getUrl(): String{
        return Url
    }
}