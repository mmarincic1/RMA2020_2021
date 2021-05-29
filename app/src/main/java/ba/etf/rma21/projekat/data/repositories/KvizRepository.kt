package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.Api
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.quizzes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import java.util.stream.Collectors
import kotlin.math.roundToInt

class KvizRepository {

    companion object {
        private var mojiKvizovi: MutableList<Kviz>
        lateinit var pokrenutiKviz: Kviz
        var radjeniKviz: KvizTaken? = null

        // pomocna fija kako bi mogao sve testove odjednom pokrenuti
        fun ispisiSve(): Unit{
            mojiKvizovi = mutableListOf()
            addMojiKvizovi("DONE", "G1")
        }

        // pomocna za dodavanje Kviza
        fun getKvizSaImenomIGrupom(predmet: String, grupa: String): Kviz{
            return getAll1().stream().filter{kviz -> kviz.nazivPredmeta == predmet && kviz.nazivGrupe == grupa}.findFirst().get()
        }

        fun addMojiKvizovi(predmet: String, grupa: String): Unit{
            var pom = getKvizSaImenomIGrupom(predmet, grupa)
            pom.setStatus(dajStatus(pom))
            mojiKvizovi.add(pom)
        }

        init {
            mojiKvizovi = mutableListOf()
        }

        fun getMyKvizes(): List<Kviz> {
            if(mojiKvizovi.size == 0)
                return emptyList()
            updateStatuseZaSvakiSlucaj()
            return mojiKvizovi
        }

       fun getAll1(): List<Kviz> {
            return quizzes()
        }

        fun getDone(): List<Kviz> {
            updateStatuseZaSvakiSlucaj()
            val pom = mojiKvizovi.stream().filter { kviz -> kviz.getStatus() == "plava" }.collect(
                Collectors.toList())
            if(pom.size == 0)
                return emptyList()
            return pom
        }

        fun getFuture(): List<Kviz> {
            updateStatuseZaSvakiSlucaj()
            val pom = mojiKvizovi.stream().filter { kviz -> kviz.getStatus() == "zuta" }.collect(
                Collectors.toList())
            if(pom.size == 0)
                return emptyList()
            return pom
        }

        fun getNotTaken(): List<Kviz> {
            updateStatuseZaSvakiSlucaj()
            val pom = mojiKvizovi.stream().filter { kviz -> kviz.getStatus() == "crvena" }.collect(
                Collectors.toList())
            if(pom.size == 0)
                return emptyList()
            return pom
        }

        private fun dajStatus(kviz: Kviz): String {
            // kviz nije uradjen
            if(kviz.datumRada == null){
                var datumPocetka = uporediSaTrenutnimDatumom(kviz.datumPocetka)
                var datumKraja = kviz.datumKraj?.let { uporediSaTrenutnimDatumom(it) }
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

        fun zavrsiKviz(datum: Date, predmet: String, kvizz: String, bodovi: Int){
            mojiKvizovi.stream().filter{kviz -> kviz.nazivPredmeta == predmet && kviz.naziv == kvizz}.findFirst().get().datumRada = datum
            mojiKvizovi.stream().filter{kviz -> kviz.nazivPredmeta == predmet && kviz.naziv == kvizz}.findFirst().get().osvojeniBodovi = bodovi.toFloat()
            mojiKvizovi.stream().filter{kviz -> kviz.nazivPredmeta == predmet && kviz.naziv == kvizz}.findFirst().get().setStatus("plava")
        }

        fun getStatus(kviz: Kviz): String{
            return dajStatus(kviz)
        }


        private fun updateStatuseZaSvakiSlucaj(){
            mojiKvizovi.stream().forEach { kviz -> kviz.setStatus(dajStatus(kviz))}
        }

        // nova
        suspend fun getAll():List<Kviz>{
            return withContext(Dispatchers.IO){
                var pom = ApiAdapter.retrofit.getAll()
                var rezultat = mutableListOf<Kviz>()
                for(kviz in pom){
                    val pom1 = ApiAdapter.retrofit.getGrupeZaKviz(kviz.id)
                    val kvizZaUbaciti = kviz
                    var listaNaziva = mutableListOf<String>()
                    for(kviz1 in pom1){
                        //kvizZaUbaciti.nazivGrupe = kviz1.nazivGrupe
                        val naziv = ApiAdapter.retrofit.getPredmetId(kviz1.predmetId).naziv
                        if(!listaNaziva.contains(naziv)) {
                            if (kvizZaUbaciti.nazivPredmeta == null)
                                kvizZaUbaciti.nazivPredmeta =
                                    naziv
                            else kvizZaUbaciti.nazivPredmeta += "," + naziv
                            listaNaziva.add(naziv)
                        }
                    }
                    rezultat.add(kvizZaUbaciti)
                }
                return@withContext rezultat
            }
        }

        suspend fun getById(id:Int): Kviz{
            return withContext(Dispatchers.IO){
                return@withContext ApiAdapter.retrofit.getById(id)
            }
        }

        // svi kvizovi za grupe u kojima je student upisan
        suspend fun getUpisani():List<Kviz>{
            return withContext(Dispatchers.IO){
                val acc = AccountRepository()
                var grupe = ApiAdapter.retrofit.getUpisaneGrupe(acc.getHash())
                var rezultat = mutableListOf<Kviz>()
                for(grupa in grupe){
                    var pom = ApiAdapter.retrofit.getUpisani(grupa.id)
                    val nazivPredmeta = ApiAdapter.retrofit.getPredmetId(grupa.predmetId).naziv
                    pom.stream().forEach { x -> x.nazivGrupe = grupa.naziv; x.nazivPredmeta = nazivPredmeta}
                    rezultat.addAll(pom)
                }
                return@withContext rezultat
            }
        }

        suspend fun zavrsiKviz(idKviza: KvizTaken){
            return withContext(Dispatchers.IO){
                val pitanja = ApiAdapter.retrofit.getPitanja(pokrenutiKviz.id)
                val odgovori = OdgovorRepository.getOdgovoriKviz(idKviza.id)
                for(pitanje in pitanja){
                    if(odgovori.stream().noneMatch{ x -> x.pitanjeId == pitanje.id })
                        OdgovorRepository.postaviOdgovorKviz(idKviza.id, pitanje.id, pitanje.opcije.size)
                }
                return@withContext
            }
        }

        suspend fun getZavrsenKviz(kviz: Kviz): Boolean{
            return withContext(Dispatchers.IO){
                val acc = AccountRepository()
                val pokrenutiKvizovi = ApiAdapter.retrofit.getPocetiKvizovi(acc.getHash())
                var imaGa = false
                lateinit var pKvizi: KvizTaken
                for(pKviz in pokrenutiKvizovi){
                    if(pKviz.KvizId == kviz.id){
                        pKvizi = pKviz
                        imaGa = true
                        break
                    }
                }
                if(imaGa){
                    return@withContext PitanjeKvizRepository.getZavrsenKviz(pKvizi)
                }
                return@withContext false
            }
        }

    }
}