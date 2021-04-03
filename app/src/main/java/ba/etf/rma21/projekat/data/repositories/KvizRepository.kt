package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.quizzes
import java.util.*
import java.util.stream.Collectors

class KvizRepository {

    companion object {
        private lateinit var mojiKvizovi: MutableList<Kviz>

        // pomocna fija kako bi mogao sve testove odjednom pokrenuti
        fun ispisiSve(): Unit{
            mojiKvizovi = mutableListOf()
            addMojiKvizovi("DONE", "G1")
        }

        // pomocna za dodavanje Kviza
        fun getKvizSaImenomIGrupom(predmet: String, grupa: String): Kviz{
            return getAll().stream().filter{kviz -> kviz.nazivPredmeta == predmet && kviz.nazivGrupe == grupa}.findFirst().get()
        }

        fun addMojiKvizovi(predmet: String, grupa: String): Unit{
            mojiKvizovi.add(getKvizSaImenomIGrupom(predmet, grupa))
        }

        init {
            mojiKvizovi = mutableListOf()
            addMojiKvizovi("DONE", "G1")
        }

        fun getMyKvizes(): List<Kviz> {
            if(mojiKvizovi.size == 0)
                return emptyList()
            return mojiKvizovi
        }

        fun getAll(): List<Kviz> {
            return quizzes()
        }

        fun getDone(): List<Kviz> {
            val pom = mojiKvizovi.stream().filter { kviz -> dajStatus(kviz) == "plava" }.collect(
                Collectors.toList())
            if(pom.size == 0)
                return emptyList()
            return pom
        }

        fun getFuture(): List<Kviz> {
            val pom = mojiKvizovi.stream().filter { kviz -> dajStatus(kviz) == "zuta" }.collect(
                Collectors.toList())
            if(pom.size == 0)
                return emptyList()
            return pom
        }

        fun getNotTaken(): List<Kviz> {
            val pom = mojiKvizovi.stream().filter { kviz -> dajStatus(kviz) == "crvena" }.collect(
                Collectors.toList())
            if(pom.size == 0)
                return emptyList()
            return pom
        }

        private fun dajStatus(kviz: Kviz): String {
            // kviz nije uradjen
            if(kviz.datumRada == null){
                var datumPocetka = uporediSaTrenutnimDatumom(kviz.datumPocetka)
                var datumKraja = uporediSaTrenutnimDatumom(kviz.datumKraj)
                // kviz nije otvoren
                if(datumPocetka == 1){
                    return "zuta"
                }
                // kviz aktivan
                else if(datumPocetka == 2 && datumKraja == 1){
                    return "zelena"
                }
                // kviz nije uradjen i nije aktivan
                else if(datumPocetka == 2 && datumKraja == 2){
                    return "crvena"
                }
            }
            return "plava"
        }

        private fun uporediSaTrenutnimDatumom(datum1: Date): Int{
            var godina = Calendar.getInstance().get(Calendar.YEAR)
            var mjesec = Calendar.getInstance().get(Calendar.MONTH) + 1
            var dan = Calendar.getInstance().get(Calendar.DATE)
            if(datum1.getYear() > godina) return 1
            else if(godina > datum1.getYear()) return 2;
            else if(datum1.getMonth() > mjesec) return 1;
            else if(mjesec > datum1.getMonth()) return 2;
            else if(datum1.getDate() > dan) return 1;
            else if(dan > datum1.getDate()) return 2;
            return 0;
        }

    }
}