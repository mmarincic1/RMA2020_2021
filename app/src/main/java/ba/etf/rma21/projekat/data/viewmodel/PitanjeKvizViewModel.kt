package ba.etf.rma21.projekat.data.viewmodel


import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.AccountRepository
import ba.etf.rma21.projekat.data.repositories.OdgovorRepository

import ba.etf.rma21.projekat.data.repositories.PitanjeKvizRepository
import ba.etf.rma21.projekat.data.view.KvizListAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PitanjeKvizViewModel {

    fun getPitanja(idKviza: Int,
                   onSuccess: (pitanja: List<Pitanje>) -> Unit,
                   onError: () -> Unit
    ){
        GlobalScope.launch{
            //val pitanja = PitanjeKvizRepository.getPitanja(idKviza)
            val pitanja = PitanjeKvizRepository.getPitanjaDb(idKviza)
            when(pitanja){
                is List<Pitanje> -> onSuccess?.invoke(pitanja)
                else -> onError?.invoke()
            }
        }
    }

    fun postaviOdgovorKviz(idKvizTaken:Int, idPitanje:Int, odgovor:Int, onSuccess: (Int) -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            val rezultat = OdgovorRepository.postaviOdgovorKviz(idKvizTaken, idPitanje, odgovor)
            when(rezultat){
                is Int -> onSuccess?.invoke(rezultat)
                else -> onError?.invoke()
            }
        }
    }

    fun getOdgovorApp(idKvizTaken:Int, idPitanje:Int,
    onSuccess: (odgovor: Int) -> Unit,
    onError: () -> Unit){
        GlobalScope.launch{
            val odgovor = OdgovorRepository.getOdgovorKviz(idKvizTaken, idPitanje)
            when(odgovor){
                is Int -> onSuccess?.invoke(odgovor)
                else -> onError?.invoke()
            }
        }
    }

    fun getOdgovorAppZaPokusaj(idKvizTaken:Int, idPitanje:Int, index: Int ,
                      onSuccess: (odgovor: Int, i: Int) -> Unit,
                      onError: () -> Unit){
        GlobalScope.launch{
            val odgovor = OdgovorRepository.getOdgovorKviz(idKvizTaken, idPitanje)
            when(odgovor){
                is Int -> onSuccess?.invoke(odgovor, index)
                else -> onError?.invoke()
            }
        }
    }

    fun getRezultat(kvizTaken: Int,
    onSuccess: (rezultat: Int) -> Unit,
    onError: () -> Unit){
        GlobalScope.launch {
            val rezultat = PitanjeKvizRepository.getRezultatSaNeta(kvizTaken)
            when(rezultat){
                is Int -> onSuccess?.invoke(rezultat)
                else -> onError?.invoke()
            }
        }
    }

    fun getZavrsenKviz(kvizTaken: KvizTaken,
                    onSuccess: (rezultat: Boolean) -> Unit,
                    onError: () -> Unit){
        GlobalScope.launch {
            val rezultat = PitanjeKvizRepository.getZavrsenKviz(kvizTaken)
            when(rezultat){
                is Boolean -> onSuccess?.invoke(rezultat)
                else -> onError?.invoke()
            }
        }
    }

    fun getZavrsenKvizZaSveOdgovorene(kvizTaken: KvizTaken, rez: Int,
                       onSuccess: (rezultat: Boolean, rez: Int) -> Unit,
                       onError: () -> Unit){
        GlobalScope.launch {
            val rezultat = PitanjeKvizRepository.getZavrsenKviz(kvizTaken)
            when(rezultat){
                is Boolean -> onSuccess?.invoke(rezultat, rez)
                else -> onError?.invoke()
            }
        }
    }


    fun setOdabranaGodina(odabranaGodina: Int){
        PitanjeKvizRepository.odabranaGodina = odabranaGodina
    }
    fun getOdabranaGodina(): Int{
        return PitanjeKvizRepository.odabranaGodina
    }

    fun setOdabraniPredmet(odabraniPredmet: Int){
        PitanjeKvizRepository.odabraniPredmet = odabraniPredmet
    }
    fun getOdabraniPredmet(): Int{
        return PitanjeKvizRepository.odabraniPredmet
    }

    fun setOdabranaGrupa(odabanaGrupa: Int){
        PitanjeKvizRepository.odabranaGrupa = odabanaGrupa
    }
    fun getOdabranaGrupa(): Int{
        return PitanjeKvizRepository.odabranaGrupa
    }

    fun setIndexPitanja(indexPitanja: String){
        PitanjeKvizRepository.indexPitanja = indexPitanja
    }
    fun getIndexPitanja(): String{
        return PitanjeKvizRepository.indexPitanja
    }
}