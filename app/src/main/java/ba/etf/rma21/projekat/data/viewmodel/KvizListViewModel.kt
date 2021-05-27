package ba.etf.rma21.projekat.data.viewmodel

import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.repositories.TakeKvizRepository
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

    fun getMyQuizzes(onSuccess: (quizzes: List<Kviz>) -> Unit,
                     onError: () -> Unit){
        GlobalScope.launch{
            val quizzes = KvizRepository.getUpisani()
            when(quizzes){
                is List<Kviz> -> onSuccess?.invoke(quizzes)
                else -> onError?.invoke()
            }
        }
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

    fun zavrsiKviz(datum: Date, predmet: String, kvizz: String, bodovi: Int){
        KvizRepository.zavrsiKviz(datum, predmet, kvizz, bodovi)
    }

    fun getStatus(kviz: Kviz): String{
        return KvizRepository.getStatus(kviz)
    }

    fun getPocetiKvizovi(onSuccess: (quizzes: List<KvizTaken>) -> Unit,
                         onError: () -> Unit){
        GlobalScope.launch{
            val quizzes = TakeKvizRepository.getPocetiKvizovi()
            when(quizzes){
                is List<KvizTaken> -> onSuccess?.invoke(quizzes)
                else -> onError?.invoke()
            }
        }
    }

    fun zapocniKviz(
        pKvizId: Int,
        onSuccess: (pKviz: KvizTaken) -> Unit,
                    onError: () -> Unit){
        GlobalScope.launch{
            val pKviz = TakeKvizRepository.zapocniKviz(pKvizId)
            when(pKviz){
                is KvizTaken-> onSuccess?.invoke(pKviz)
                else -> onError?.invoke()
            }
        }
    }

    fun getPocetiKvizoviApp(onSuccess: () -> Unit,
                         onError: () -> Unit){
        GlobalScope.launch{
            TakeKvizRepository.getPocetiKvizoviApp()
       onSuccess?.invoke()
            }
        }
}