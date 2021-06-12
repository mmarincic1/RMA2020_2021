package ba.etf.rma21.projekat.data.viewmodel

import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.repositories.TakeKvizRepository
import ba.etf.rma21.projekat.data.view.KvizListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.FieldPosition
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
            val quizzes = KvizRepository.getUpisaniDb()
            when(quizzes){
                is List<Kviz> -> onSuccess?.invoke(quizzes)
                else -> onError?.invoke()
            }
        }
    }

    fun getDoneQuizzes(onSuccess: (quizzes: List<Kviz>) -> Unit,
                     onError: () -> Unit){
        GlobalScope.launch{
            val quizzes = KvizRepository.getDone()
            when(quizzes){
                is List<Kviz> -> onSuccess?.invoke(quizzes)
                else -> onError?.invoke()
            }
        }
    }

    fun getFutureQuizzes(onSuccess: (quizzes: List<Kviz>) -> Unit,
                       onError: () -> Unit){
        GlobalScope.launch{
            val quizzes = KvizRepository.getFuture()
            when(quizzes){
                is List<Kviz> -> onSuccess?.invoke(quizzes)
                else -> onError?.invoke()
            }
        }
    }

    fun getPastQuizzes(onSuccess: (quizzes: List<Kviz>) -> Unit,
                       onError: () -> Unit){
        GlobalScope.launch{
            val quizzes = KvizRepository.getNotTaken()
            when(quizzes){
                is List<Kviz> -> onSuccess?.invoke(quizzes)
                else -> onError?.invoke()
            }
        }
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

    fun getPocetiKvizoviApp(kviz: Kviz, onSuccess: (rezultat: Boolean, kviz: Kviz) -> Unit,
                         onError: () -> Unit){
        GlobalScope.launch{
            val rezultat = TakeKvizRepository.getPocetiKvizoviApp()
            when(rezultat){
                is Boolean-> onSuccess?.invoke(rezultat, kviz)
                else -> onError?.invoke()
            }
            }
        }

    fun zavrsiKviz(idKvizTaken: KvizTaken, rezultat: Int, onSuccess: (rezultat: Int) -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            KvizRepository.zavrsiKviz(idKvizTaken)
            onSuccess?.invoke(rezultat)
        }
    }

    fun getZavrsenKviz(idKviz: Kviz, holder: KvizListAdapter.QuizViewHolder, position: Int ,
                       onSuccess: (kviz: Kviz,rezultat: Boolean, holder: KvizListAdapter.QuizViewHolder, position: Int) -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            val rezultat = KvizRepository.getZavrsenKviz(idKviz)
            when(rezultat){
                is  Boolean-> onSuccess?.invoke(idKviz, rezultat, holder, position)
            }
        }
    }
}