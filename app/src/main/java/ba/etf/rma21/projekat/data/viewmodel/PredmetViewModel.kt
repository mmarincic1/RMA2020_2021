package ba.etf.rma21.projekat.data.viewmodel

import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.repositories.PredmetRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PredmetViewModel {

    fun getPredmetsByGodina(godina: Int,
                            onSuccess: (predmeti: List<Predmet>) -> Unit,
                            onError: () -> Unit
    ){
        GlobalScope.launch{
            val predmeti = PredmetRepository.getPredmetsByGodina(godina)
            when(predmeti){
                is List<Predmet> -> onSuccess?.invoke(predmeti)
                else -> onError?.invoke()
            }
        }
    }

}