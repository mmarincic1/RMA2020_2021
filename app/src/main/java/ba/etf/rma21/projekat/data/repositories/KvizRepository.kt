package ba.etf.rma21.projekat.data.repositories

import android.annotation.SuppressLint
import android.os.Build
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Account
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.KvizTaken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class KvizRepository {

    companion object {
        lateinit var pokrenutiKviz: Kviz
        var radjeniKviz: KvizTaken? = null

        suspend fun getDone(): List<Kviz> {
            return withContext(Dispatchers.IO){
               val kvizovi = getUpisani()
                var rezultat = mutableListOf<Kviz>()
                for(kviz in kvizovi){
                    if(getZavrsenKviz(kviz))
                        rezultat.add(kviz)
                }
                return@withContext rezultat
            }
        }

        suspend fun getFuture(): List<Kviz> {
            return withContext(Dispatchers.IO){
                val kvizovi = getUpisani()
                var rezultat = mutableListOf<Kviz>()
                for(kviz in kvizovi){
                    if(!getZavrsenKviz(kviz) && dajStatus(kviz) == "zuta")
                        rezultat.add(kviz)
                }
                return@withContext rezultat
            }
         }

        suspend fun getNotTaken(): List<Kviz> {
            return withContext(Dispatchers.IO){
                val kvizovi = getUpisani()
                var rezultat = mutableListOf<Kviz>()
                for(kviz in kvizovi){
                    if(!getZavrsenKviz(kviz) && dajStatus(kviz) == "crvena")
                        rezultat.add(kviz)
                }
                return@withContext rezultat
            }
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

        fun getStatus(kviz: Kviz): String{
            return dajStatus(kviz)
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
                    for(kviz in rezultat){
                        if(getZavrsenKvizApi(kviz)){
                            kviz.osvojeniBodovi = PitanjeKvizRepository.getRezultatSaServisa(kviz.id)
                            kviz.predan = true
                        }
                        kviz.datumPocetakDb = getDateFormat(kviz.datumPocetka)
                        if(kviz.datumKraj != null)
                            kviz.datumKrajDb = getDateFormat(kviz.datumKraj!!)
                        else kviz.datumKrajDb = ""
                    }
                }
                return@withContext rezultat
            }
        }

        suspend fun getById(id:Int): Kviz? {
            return withContext(Dispatchers.IO){
                try {
                    val rezultat = ApiAdapter.retrofit.getById(id)
                    return@withContext rezultat
                }catch (e: Exception){
                    return@withContext null
                }
            }
        }

        @SuppressLint("SimpleDateFormat")
        private fun getDateFormat(date: Date): String {
            val format = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
            return format.format(date)
        }

        // svi kvizovi za grupe u kojima je student upisan
        suspend fun getUpisani():List<Kviz>{
            return withContext(Dispatchers.IO){
                var grupe = ApiAdapter.retrofit.getUpisaneGrupe(AccountRepository.getHash())
                var rezultat = mutableListOf<Kviz>()
                for(grupa in grupe){
                    var pom = ApiAdapter.retrofit.getUpisani(grupa.id)
                    val nazivPredmeta = ApiAdapter.retrofit.getPredmetId(grupa.predmetId).naziv
                    pom.stream().forEach { x -> x.nazivGrupe = grupa.naziv;
                        if(x.nazivPredmeta == null)
                            x.nazivPredmeta = nazivPredmeta
                    else x.nazivPredmeta+= nazivPredmeta
                    }
                    rezultat.addAll(pom)
                }
                var izbacuj = mutableListOf<Int>()
                var i = 0
                var j = 0
                while(i < rezultat.size){
                    j = i + 1
                    while(j < rezultat.size){
                        if(rezultat[i].id == rezultat[j].id){
                            rezultat[i].nazivPredmeta+= "," + rezultat[j].nazivPredmeta
                            rezultat.removeAt(j)
                            izbacuj.add(j)
                        }
                        j++
                    }
                    i++
                }
                for(izbaci in izbacuj)
                    rezultat.removeAt(izbaci)
                val kvizTakenZaDatum = TakeKvizRepository.getPocetiKvizovi()
                if (kvizTakenZaDatum != null) {
                    for(kviz in kvizTakenZaDatum){
                        rezultat.stream().forEach { x ->
                            if(x.id == kviz.KvizId){
                                x.datumRada = kviz.datumRada
                                x.datumRadaDb = getDateFormat(kviz.datumRada)
                            }
                        }
                    }
                }
                // novo
                for(kviz in rezultat){
                    if(getZavrsenKvizApi(kviz)){
                        kviz.osvojeniBodovi = PitanjeKvizRepository.getRezultatSaServisa(kviz.id)
                        kviz.predan = true
                    }
                    kviz.datumPocetakDb = getDateFormat(kviz.datumPocetka)
                    if(kviz.datumKraj != null)
                        kviz.datumKrajDb = getDateFormat(kviz.datumKraj!!)
                    else kviz.datumKrajDb = ""
                }
                // novo
                return@withContext rezultat
            }
        }

        suspend fun zavrsiKviz(idKviza: KvizTaken){
            return withContext(Dispatchers.IO){
                val db = AppDatabase.getInstance(AccountRepository.getContext())
                val pitanja = PitanjeKvizRepository.getPitanja(pokrenutiKviz.id)
                val odgovori = db.odgovorDao().getOdgovori(pokrenutiKviz.id)
                for(pitanje in pitanja){
                    // nije odgovoreno pitanje
                    if(odgovori.stream().noneMatch{ x -> x.pitanjeId == pitanje.id }) {
                        OdgovorRepository.postaviOdgovorKviz(idKviza.id, pitanje.id, pitanje.opcije.size)
                        OdgovorRepository.postaviOdgovorKvizApi(idKviza.id, pitanje.id, pitanje.opcije.size)
                    } // jeste odgovoreno pitanje
                    else OdgovorRepository.postaviOdgovorKvizApi(idKviza.id, pitanje.id, odgovori.stream().filter{
                        x -> x.pitanjeId == pitanje.id}.findFirst().get().odgovoreno)
                }
                db.kvizDao().zavrsiKviz(true, pokrenutiKviz.id)
                db.kvizDao().upisiBodove(PitanjeKvizRepository.getRezultatZaKviz(pokrenutiKviz.id), pokrenutiKviz.id)
                return@withContext
            }
        }

        suspend fun getZavrsenKvizApi(kviz: Kviz): Boolean{
            return withContext(Dispatchers.IO){
                val pokrenutiKvizovi = ApiAdapter.retrofit.getPocetiKvizovi(AccountRepository.getHash())
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
                    return@withContext PitanjeKvizRepository.getZavrsenKvizApi(pKvizi)
                }
                return@withContext false
            }
        }

        suspend fun getZavrsenKviz(kviz: Kviz): Boolean{
            return withContext(Dispatchers.IO){
                val pokrenutiKvizovi = ApiAdapter.retrofit.getPocetiKvizovi(AccountRepository.getHash())
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

        private fun stringToDate(value: String?): Date {
            val format = SimpleDateFormat("yyyy-MM-dd")
            return format.parse(value)
        }

        suspend fun getUpisaniDb(): List<Kviz>{
            return withContext(Dispatchers.IO){
                val db = AppDatabase.getInstance(AccountRepository.getContext())
                val rezultat = db.kvizDao().dajSveKvizoveDb()
                rezultat.stream().forEach { x ->
                    x.datumPocetka = stringToDate(x.datumPocetakDb)
                    if(x.datumKrajDb == "")
                        x.datumKraj = null
                    else x.datumKraj = stringToDate(x.datumKrajDb)
                    if(x.datumRadaDb == "")
                        x.datumRada = null
                    else x.datumRada = stringToDate(x.datumRadaDb)
                }

                return@withContext rezultat
            }
        }

    }
}