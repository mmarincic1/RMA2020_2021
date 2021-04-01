package ba.etf.rma21.projekat.data.viewmodel

import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.repositories.KvizRepository

class KvizListViewModel {
    fun getQuizzes(): List<Kviz>{
        return KvizRepository.getAll()
    }

    fun getMyQuizzes(): List<Kviz>{
        return KvizRepository.getMyKvizes()
    }

    fun getDoneQuizzes(): List<Kviz>{
        return KvizRepository.getDone()
    }

    fun getFutureQuizzes(): List<Kviz>{
        return KvizRepository.getFuture()
    }

    fun getPastQuizzes(): List<Kviz>{
        return KvizRepository.getNotTaken()
    }
}