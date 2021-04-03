package ba.etf.rma21.projekat

import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.repositories.GrupaRepository
import ba.etf.rma21.projekat.data.repositories.PredmetRepository
import junit.framework.Assert
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.hasProperty
import org.junit.Test

class PredmetIGroupTest {
    @Test
    fun testSviPredmeti(){
        val predmeti = PredmetRepository.getAll()
        Assert.assertEquals(predmeti.size, 7)

        val predmetiPrva = PredmetRepository.getPredmetsByGodina(1)
        Assert.assertEquals(predmetiPrva.size, 4)

        val predmetiDruga = PredmetRepository.getPredmetsByGodina(2)
        Assert.assertEquals(predmetiDruga.size, 3)

        MatcherAssert.assertThat(predmeti, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("OOAD"))))
        MatcherAssert.assertThat(predmetiDruga, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("RMA"))))
        MatcherAssert.assertThat(predmetiPrva, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("UUP"))))
    }

    @Test
    fun testGodineBezPredmeta(){
        // nisam ubacio predmete na 3., 4. i 5. godinu
        val predmetiTreca = PredmetRepository.getPredmetsByGodina(3)
        Assert.assertEquals(predmetiTreca.size, 0)

        val predmetiCetvrta = PredmetRepository.getPredmetsByGodina(4)
        Assert.assertEquals(predmetiCetvrta.size, 0)

        val predmetiPeta = PredmetRepository.getPredmetsByGodina(5)
        Assert.assertEquals(predmetiPeta.size, 0)

        MatcherAssert.assertThat(predmetiTreca, CoreMatchers.not(CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("NEMA-GA")))))
        MatcherAssert.assertThat(predmetiTreca, CoreMatchers.not(CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("NEMA-GA")))))
        MatcherAssert.assertThat(predmetiTreca, CoreMatchers.not(CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("NEMA-GA")))))

    }

    @Test
    fun testMojiPredmeti(){
        // moj jedan predmet koji je po defaultu upisan
        val predmeti = PredmetRepository.getUpisani()
        Assert.assertEquals(predmeti.size, 1)
        // upisujem jos jedan
        PredmetRepository.addUpisani(PredmetRepository.getAll()[0].godina, PredmetRepository.getAll()[0].naziv)
        Assert.assertEquals(predmeti.size, 2)
    }

    @Test
    fun testPredmetiGrupe(){
        // svi imaju po 3 kviza jer je svaki kviz drugaciji za svaku grupu
        val grupeZaIM = GrupaRepository.getGroupsByPredmet("IM")
        Assert.assertEquals(grupeZaIM.size, 3)
        MatcherAssert.assertThat(grupeZaIM, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("G1"))))

        val grupeZaOOAD = GrupaRepository.getGroupsByPredmet("OOAD")
        Assert.assertEquals(grupeZaOOAD.size, 3)
        MatcherAssert.assertThat(grupeZaOOAD, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("G2"))))

        val grupeZaRPR = GrupaRepository.getGroupsByPredmet("RPR")
        Assert.assertEquals(grupeZaRPR.size, 3)
        MatcherAssert.assertThat(grupeZaRPR, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("G3"))))

        val grupeZaRMA = GrupaRepository.getGroupsByPredmet("RMA")
        Assert.assertEquals(grupeZaRMA.size, 3)
        MatcherAssert.assertThat(grupeZaRMA, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("G1"))))

        val grupeZaUUP = GrupaRepository.getGroupsByPredmet("UUP")
        Assert.assertEquals(grupeZaUUP.size, 3)
        MatcherAssert.assertThat(grupeZaUUP, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("G2"))))

        val grupeZaTP = GrupaRepository.getGroupsByPredmet("TP")
        Assert.assertEquals(grupeZaTP.size, 3)
        MatcherAssert.assertThat(grupeZaTP, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("G3"))))

        // jedna grupa za predefinisani predmet
        val grupaDONE = GrupaRepository.getGroupsByPredmet("DONE")
        Assert.assertEquals(grupaDONE.size, 1)
        MatcherAssert.assertThat(grupaDONE, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("G1"))))
    }
}