package ba.etf.rma21.projekat.data.viewmodel

import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.models.PitanjeKviz
import ba.etf.rma21.projekat.data.repositories.PitanjeKvizRepository

class PitanjeKvizViewModel {

    fun getPitanja(nazivKviza: String, nazivPredmeta: String): List<Pitanje>{
        return PitanjeKvizRepository.getPitanja(nazivKviza, nazivPredmeta)
    }

    fun odgovoriNaPitanje(odgovor: Int, nazivKviza: String, nazivPredmeta: String, nazivPitanja: String){
        return PitanjeKvizRepository.odgovoriNaPitanje(odgovor, nazivKviza, nazivPredmeta, nazivPitanja)
    }

    fun getOdgovorNaPitanje(nazivKviza: String, nazivPredmeta: String, nazivPitanja: String): Int{
       return PitanjeKvizRepository.getOdgovorNaPitanje(nazivKviza, nazivPredmeta, nazivPitanja)
    }

    fun zavrsiKviz(nazivKviza: String, nazivPredmeta: String){
       PitanjeKvizRepository.zavrsiKviz(nazivKviza, nazivPredmeta)
    }

    fun getRezultat(nazivKviza: String, nazivPredmeta: String): Int{
        return PitanjeKvizRepository.getRezultat(nazivKviza, nazivPredmeta)
    }

    fun getZavrsenKviz(nazivKviza: String, nazivPredmeta: String): Boolean{
        return PitanjeKvizRepository.getZavrsenKviz(nazivKviza, nazivPredmeta)
    }

    fun dodajRezultat(uradjeniKviz: String, uradjeniPredmet: String, rezultat: Double) {
        PitanjeKvizRepository.dodajRezultat(uradjeniKviz, uradjeniPredmet, rezultat)
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