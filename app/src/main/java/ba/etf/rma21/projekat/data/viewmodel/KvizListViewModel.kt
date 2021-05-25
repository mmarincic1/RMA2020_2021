package ba.etf.rma21.projekat.data.viewmodel

import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class KvizListViewModel {
    fun getQuizzes(onSuccess: (quizzes: List<Kviz>) -> Unit,
                    onError: () -> Unit
                   ){
        GlobalScope.launch{
            val quizzes = KvizRepository.getAll()
            when(quizzes){
                is List<Kviz> -> onSuccess?.invoke(quizzes)
                else -> onError?.invoke()
            }
        }
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

    fun addMojKviz(predmet: String, grupa: String): Unit{
        KvizRepository.addMojiKvizovi(predmet, grupa)
    }

    fun zavrsiKviz(datum: Date, predmet: String, kvizz: String, bodovi: Int){
        KvizRepository.zavrsiKviz(datum, predmet, kvizz, bodovi)
    }

    fun getStatus(predmet: String, kvizz: String): String{
        return KvizRepository.getStatus(predmet, kvizz)
    }
}