package ba.etf.rma21.projekat.data.viewmodel


import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.OdgovorRepository

import ba.etf.rma21.projekat.data.repositories.PitanjeKvizRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PitanjeKvizViewModel {

    fun getPitanja(idKviza: Int,
                   onSuccess: (pitanja: List<Pitanje>) -> Unit,
                   onError: () -> Unit
    ){
        GlobalScope.launch{
            val pitanja = PitanjeKvizRepository.getPitanja(idKviza)
            when(pitanja){
                is List<Pitanje> -> onSuccess?.invoke(pitanja)
                else -> onError?.invoke()
            }
        }
    }

    fun postaviOdgovorKviz(idKvizTaken:Int, idPitanje:Int, odgovor:Int){
        GlobalScope.launch{
            OdgovorRepository.postaviOdgovorKviz(idKvizTaken, idPitanje, odgovor)
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



    fun getOdgovorNaPitanje(nazivKviza: String, nazivPredmeta: String, nazivPitanja: String): Int{
       return PitanjeKvizRepository.getOdgovorNaPitanje(nazivKviza, nazivPredmeta, nazivPitanja)
    }


    fun getRezultat(nazivKviza: String, nazivPredmeta: String): Int{
        return PitanjeKvizRepository.getRezultat(nazivKviza, nazivPredmeta)
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

    fun setUradjeniKviz(uradjeniKviz: String){
        PitanjeKvizRepository.uradjeniKviz = uradjeniKviz
    }
    fun getUradjeniKviz(): String{
        return PitanjeKvizRepository.uradjeniKviz
    }

    fun setUradjeniPredmet(uradjeniPredmet: String){
        PitanjeKvizRepository.uradjeniPredmet = uradjeniPredmet
    }
    fun getUradjeniPredmet(): String{
        return PitanjeKvizRepository.uradjeniPredmet
    }

    fun setBrojPitanja(brojPitanja: Int){
        PitanjeKvizRepository.brojPitanja = brojPitanja
    }
    fun getBrojPitanja(): Int{
        return PitanjeKvizRepository.brojPitanja
    }

    fun setOdgovor(odgovor: Int){
        PitanjeKvizRepository.odgovor = odgovor
    }

    fun getOdgovor(): Int{
        return PitanjeKvizRepository.odgovor
    }

    fun setIndexPitanja(indexPitanja: String){
        PitanjeKvizRepository.indexPitanja = indexPitanja
    }
    fun getIndexPitanja(): String{
        return PitanjeKvizRepository.indexPitanja
    }
}