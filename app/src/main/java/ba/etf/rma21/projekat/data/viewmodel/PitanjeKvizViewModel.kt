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


}