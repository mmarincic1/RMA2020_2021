package ba.etf.rma21.projekat.data.viewmodel

import ba.etf.rma21.projekat.data.repositories.PredmetRepository

class PredmetViewModel {

    fun addUpisani(godina: Int, naziv: String) {
        PredmetRepository.addUpisani(godina, naziv)
    }

    fun getFromYear(godina: Int): List<String> {
        return PredmetRepository.getFromYear(godina)
    }

}