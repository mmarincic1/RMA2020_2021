package ba.etf.rma21.projekat.data.viewmodel

import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.repositories.PredmetRepository

class PredmetViewModel {

    fun addUpisani(godina: Int, naziv: String) {
        PredmetRepository.addUpisani(godina, naziv)
    }

    fun getFromYear(godina: Int): List<String> {
        return PredmetRepository.getFromYear(godina)
    }

    fun getAll(): List<Predmet>{
        return PredmetRepository.getAll()
    }

    fun getUpisani(): List<Predmet>{
        return PredmetRepository.getUpisani()
    }

    fun getPredmetsByGodina(godina: Int): List<Predmet> {
        return PredmetRepository.getPredmetsByGodina(godina)
    }

}