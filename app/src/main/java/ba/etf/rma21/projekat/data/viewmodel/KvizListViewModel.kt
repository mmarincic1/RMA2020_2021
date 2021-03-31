package ba.etf.rma21.projekat.data.viewmodel

import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.repositories.KvizRepository

class KvizListViewModel {
    fun getQuizzes(): List<Kviz>{
        return KvizRepository.getAll()
    }
}