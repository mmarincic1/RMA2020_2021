package ba.etf.rma21.projekat.data.viewmodel

import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.repositories.GrupaRepository
import ba.etf.rma21.projekat.data.repositories.PredmetRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GroupViewModel {

    fun getGroupsByPredmet(predmet: Predmet,
                           onSuccess: (grupe: List<Grupa>) -> Unit,
                           onError: () -> Unit
    ){
        GlobalScope.launch{
            val grupe = GrupaRepository.getGroupsByPredmet(predmet)
            when(grupe){
                is List<Grupa> -> onSuccess?.invoke(grupe)
                else -> onError?.invoke()
            }
        }
    }


}