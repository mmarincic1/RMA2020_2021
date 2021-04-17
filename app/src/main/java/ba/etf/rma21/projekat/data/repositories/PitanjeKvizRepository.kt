package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.pitanjaKvizovi
import java.util.stream.Collectors
import ba.etf.rma21.projekat.data.getPitanja

// - getPitanja(nazivKviza: String, nazivPredmeta: String): List<Pitanje>

class PitanjeKvizRepository {

    companion object{

        // nisam iskorisio nazivPredmeta jer ne bi ovo pozivao da nije kliknuo na vec neki odabrani kviz
        // koji se nalazi u kvizRepository-u

         fun getPitanja(nazivKviza: String, nazivPredmeta: String): List<Pitanje>{
            var listaPitanja = pitanjaKvizovi().stream().filter{x -> x.kviz == nazivKviza}.collect(
                Collectors.toList())
            var pomocnaPitanja = getPitanja()
            var rezultatnaPitanja = mutableListOf<Pitanje>()
            for (pitanje in pomocnaPitanja){
                for(kviz in listaPitanja){
                    if(pitanje.naziv == kviz.naziv)
                        rezultatnaPitanja.add(pitanje)
                }
            }
            return rezultatnaPitanja
        }

    }

}