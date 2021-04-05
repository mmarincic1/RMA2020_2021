package ba.etf.rma21.projekat

import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.repositories.GrupaRepository
import ba.etf.rma21.projekat.data.repositories.PredmetRepository
import ba.etf.rma21.projekat.data.viewmodel.GroupViewModel
import ba.etf.rma21.projekat.data.viewmodel.PredmetViewModel
import junit.framework.Assert
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.hasProperty
import org.junit.Test

class PredmetIGroupTest {
    val predmetiView: PredmetViewModel = PredmetViewModel()
    val grupaView: GroupViewModel = GroupViewModel()
    @Test
    fun testSviPredmeti(){
        val predmeti = predmetiView.getAll()
        Assert.assertEquals(7, predmeti.size)

        val predmetiPrva = predmetiView.getPredmetsByGodina(1)
        Assert.assertEquals(4, predmetiPrva.size)

        val predmetiDruga = predmetiView.getPredmetsByGodina(2)
        Assert.assertEquals(3, predmetiDruga.size)

        MatcherAssert.assertThat(predmeti, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("OOAD"))))
        MatcherAssert.assertThat(predmetiDruga, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("RMA"))))
        MatcherAssert.assertThat(predmetiPrva, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("UUP"))))
    }

    @Test
    fun testGodineBezPredmeta(){
        // nisam ubacio predmete na 3., 4. i 5. godinu
        val predmetiTreca = predmetiView.getPredmetsByGodina(3)
        Assert.assertEquals(0, predmetiTreca.size)

        val predmetiCetvrta = predmetiView.getPredmetsByGodina(4)
        Assert.assertEquals(0, predmetiCetvrta.size)

        val predmetiPeta = predmetiView.getPredmetsByGodina(5)
        Assert.assertEquals(0, predmetiPeta.size)

        MatcherAssert.assertThat(predmetiTreca, CoreMatchers.not(CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("NEMA-GA")))))
        MatcherAssert.assertThat(predmetiTreca, CoreMatchers.not(CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("NEMA-GA")))))
        MatcherAssert.assertThat(predmetiTreca, CoreMatchers.not(CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("NEMA-GA")))))

    }

    @Test
    fun testMojiPredmeti(){
        // moj jedan predmet koji je po defaultu upisan
        val predmeti = predmetiView.getUpisani()
        Assert.assertEquals(1, predmeti.size)
        // upisujem jos jedan
        predmetiView.addUpisani(predmetiView.getAll()[0].godina, predmetiView.getAll()[0].naziv)
        Assert.assertEquals(2, predmeti.size)
    }

    @Test
    fun testPredmetiGrupe(){
        // svi imaju po 3 kviza jer je svaki kviz drugaciji za svaku grupu
        val grupeZaIM = grupaView.getGroupsByPredmet("IM")
        Assert.assertEquals(3, grupeZaIM.size)
        MatcherAssert.assertThat(grupeZaIM, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("G1"))))

        val grupeZaOOAD = grupaView.getGroupsByPredmet("OOAD")
        Assert.assertEquals(3, grupeZaOOAD.size)
        MatcherAssert.assertThat(grupeZaOOAD, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("G2"))))

        val grupeZaRPR = grupaView.getGroupsByPredmet("RPR")
        Assert.assertEquals(3, grupeZaRPR.size)
        MatcherAssert.assertThat(grupeZaRPR, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("G3"))))

        val grupeZaRMA = grupaView.getGroupsByPredmet("RMA")
        Assert.assertEquals(3, grupeZaRMA.size)
        MatcherAssert.assertThat(grupeZaRMA, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("G1"))))

        val grupeZaUUP = grupaView.getGroupsByPredmet("UUP")
        Assert.assertEquals(3, grupeZaUUP.size)
        MatcherAssert.assertThat(grupeZaUUP, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("G2"))))

        val grupeZaTP = grupaView.getGroupsByPredmet("TP")
        Assert.assertEquals(3, grupeZaTP.size)
        MatcherAssert.assertThat(grupeZaTP, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("G3"))))

        // jedna grupa za predefinisani predmet
        val grupaDONE = grupaView.getGroupsByPredmet("DONE")
        Assert.assertEquals(1, grupaDONE.size)
        MatcherAssert.assertThat(grupaDONE, CoreMatchers.hasItem<Kviz>(hasProperty("naziv", CoreMatchers.`is`("G1"))))
    }
}