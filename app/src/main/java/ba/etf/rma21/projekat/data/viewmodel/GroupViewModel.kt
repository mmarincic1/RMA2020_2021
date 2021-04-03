package ba.etf.rma21.projekat.data.viewmodel

import ba.etf.rma21.projekat.data.repositories.GrupaRepository

class GroupViewModel {

    fun getGroupsByPredmetString(predmet: String): List<String> {
        return GrupaRepository.getGroupsByPredmetString(predmet)
    }
}