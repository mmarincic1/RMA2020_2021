package ba.etf.rma21.projekat

import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.repositories.PitanjeKvizRepository
import ba.etf.rma21.projekat.data.view.KvizListAdapter
import ba.etf.rma21.projekat.data.viewmodel.KvizListViewModel
import ba.etf.rma21.projekat.data.viewmodel.PitanjeKvizViewModel
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasProperty
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as Is


class KvizFilterTest {

    var kvizModel: KvizListViewModel = KvizListViewModel()
    var pitanjeKvizModel: PitanjeKvizViewModel = PitanjeKvizViewModel()

    // NOVI TEST
    @Test
    fun noviTest(){
        val kvizovi = pitanjeKvizModel.getPitanja("kviz0", "DONE")
        assertEquals(3, kvizovi.size)
        //assertEquals(3, PitanjeKvizRepository.getSize())

    }
    // NOVI TEST

    @Test
    fun testSviKvizovi(){
        val kvizovi = kvizModel.getQuizzes()
        assertEquals(19, kvizovi.size)
        // jedan koji nema bodova
        assertThat(kvizovi, hasItem<Kviz>(hasProperty("nazivPredmeta", Is("RMA"))))
        assertThat(kvizovi, hasItem<Kviz>(hasProperty("trajanje", Is(7))))
        // jedan da ne postoji
        assertThat(kvizovi, not(hasItem<Kviz>(hasProperty("nazivPredmeta", Is("NEMA-GA")))))
        // jedan sa bodovima
        assertThat(kvizovi, hasItem<Kviz>(hasProperty("nazivPredmeta", Is("DONE"))))
        assertThat(kvizovi, hasItem<Kviz>(hasProperty("osvojeniBodovi", Is(10F))))
        assertThat(kvizovi, hasItem<Kviz>(hasProperty("nazivGrupe", Is("G1"))))
    }

    @Test
    fun testMojiKvizovi(){
        var kvizovi = kvizModel.getMyQuizzes()
        assertEquals(1, kvizovi.size)
        assertThat(kvizovi, hasItem<Kviz>(hasProperty("nazivPredmeta", Is("DONE"))))
        assertThat(kvizovi, hasItem<Kviz>(hasProperty("trajanje", Is(17))))
        assertThat(kvizovi, not(hasItem<Kviz>(hasProperty("nazivPredmeta", Is("NEMA-GA")))))
        // upisati jos 2
        val sviKvizovi = KvizRepository.getAll()
        KvizRepository.addMojiKvizovi(sviKvizovi.get(0).nazivPredmeta, sviKvizovi.get(0).nazivGrupe)
        KvizRepository.addMojiKvizovi(sviKvizovi.get(1).nazivPredmeta, sviKvizovi.get(1).nazivGrupe)
        kvizovi = KvizRepository.getMyKvizes()
        assertEquals(3, kvizovi.size)
        KvizRepository.ispisiSve()
    }

    @Test
    fun testBuduciKvizovi(){
        // na pocetku 0 jer nije upisan ni na jedan buduci kviz samo na jedan koji je uradio
        var kvizovi = kvizModel.getFutureQuizzes()
        assertEquals(0, kvizovi.size)
        // upisujem ga na 3 buduca i 1 prosli da vidim da li dobro filtrira sve
        KvizRepository.addMojiKvizovi("TP", "G1") // buduci
        KvizRepository.addMojiKvizovi("IM", "G2") // prosli
        KvizRepository.addMojiKvizovi("UUP", "G3") // buduci
        KvizRepository.addMojiKvizovi("OOAD", "G1") // buduci

        kvizovi = kvizModel.getFutureQuizzes()
        assertEquals(3, kvizovi.size)

        assertThat(kvizovi, hasItem<Kviz>(hasProperty("nazivPredmeta", Is("OOAD"))))
        assertThat(kvizovi, hasItem<Kviz>(hasProperty("nazivPredmeta", Is("TP"))))
        assertThat(kvizovi, hasItem<Kviz>(hasProperty("nazivPredmeta", Is("UUP"))))
        assertThat(kvizovi, not(hasItem<Kviz>(hasProperty("nazivPredmeta", Is("IM"))))) // trebalo bi da nema ovog predmeta

        KvizRepository.ispisiSve()
    }

    @Test
    fun testUradjeniKvizovi(){
        // NAPOMENA - dodao sam jedan kviz uradjeni koji je prosao i ima bodove ali nije na pocetku
        // korisnik upisan na njega cisto da se vidi da radi i tako, dok je kviz "DONE" taj kviz
        // na koji je vec upisan

        // ovdje treba da je vec 1 kviz jer je postavkom definisano da treba biti bar jedan kviz u mockup-u
        var kvizovi = kvizModel.getDoneQuizzes()
        assertEquals(1, kvizovi.size)

        assertThat(kvizovi, hasItem<Kviz>(hasProperty("nazivPredmeta", Is("DONE"))))
        assertThat(kvizovi, hasItem<Kviz>(hasProperty("naziv", Is("Kviz 0"))))
        assertThat(kvizovi, hasItem<Kviz>(hasProperty("osvojeniBodovi", Is(10F))))

        // upisujem ga na 1 uradjeni, 1 prosli i 1 buduci
        KvizRepository.addMojiKvizovi("TP", "G1") // buduci
        KvizRepository.addMojiKvizovi("IM", "G2") // prosli
        KvizRepository.addMojiKvizovi("RPR", "G3") // uradjeni

        kvizovi = kvizModel.getDoneQuizzes()
        assertEquals(2, kvizovi.size)

        assertThat(kvizovi, hasItem<Kviz>(hasProperty("nazivPredmeta", Is("RPR"))))
        assertThat(kvizovi, hasItem<Kviz>(hasProperty("naziv", Is("Kviz 2"))))
        assertThat(kvizovi, hasItem<Kviz>(hasProperty("osvojeniBodovi", Is(1F))))

        KvizRepository.ispisiSve()
    }


    @Test
    fun testProsliKvizovi(){
        // treba na pocetku da je 0 jer nije upisan ni u jedan prosli kviz
        var kvizovi = kvizModel.getPastQuizzes()
        assertEquals(0, kvizovi.size)

        // upisujem ga na 1 uradjeni, 1 prosli i 1 buduci
        KvizRepository.addMojiKvizovi("TP", "G1") // buduci
        KvizRepository.addMojiKvizovi("IM", "G2") // prosli
        KvizRepository.addMojiKvizovi("RPR", "G3") // uradjeni

        kvizovi = kvizModel.getPastQuizzes()
        assertEquals(1, kvizovi.size)

        assertThat(kvizovi, hasItem<Kviz>(hasProperty("nazivPredmeta", Is("IM"))))
        assertThat(kvizovi, hasItem<Kviz>(hasProperty("naziv", Is("Kviz 0"))))
        assertThat(kvizovi, hasItem<Kviz>(hasProperty("trajanje", Is(30))))
        assertThat(kvizovi, hasItem<Kviz>(hasProperty("nazivGrupe", Is("G2"))))

        KvizRepository.ispisiSve()
    }
}