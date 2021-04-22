package ba.etf.rma21.projekat

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.repositories.PitanjeKvizRepository
import ba.etf.rma21.projekat.data.repositories.PredmetRepository
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matchers
import org.hamcrest.Matchers.anything
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MySpirala2AndroidTest {
    @get:Rule
    val intentsTestRule = IntentsTestRule<MainActivity>(MainActivity::class.java)

    // pomocna fija sa vjezbi
    fun withImage(@DrawableRes id: Int) = object : TypeSafeMatcher<View>(){
        override fun describeTo(description: Description) {
            description.appendText("Drawable does not contain image with id: $id")
        }
        override fun matchesSafely(item: View): Boolean {
            val context: Context = item.context
            val bitmap: Bitmap? = context.getDrawable(id)?.toBitmap()
            return item is ImageView && item.drawable.toBitmap().sameAs(bitmap)
        }
    }

    @Test
    fun prviTest() {
        // otvaram svoje kvizove
        onView(withId(R.id.filterKvizova)).perform(click())
        Espresso.onData(CoreMatchers.allOf(CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)), CoreMatchers.`is`("Svi moji kvizovi"))).perform(click())
        // otvaram upis
        onView(withId(R.id.predmeti)).perform(click())
        // dohvacam neki kviz
        onView(withId(R.id.odabirGodina)).perform(click())
        val nedodjeljeniKvizovi = KvizRepository.getAll().minus(KvizRepository.getMyKvizes())
        val nedodjeljeniPredmeti = PredmetRepository.getAll().minus(PredmetRepository.getUpisani())
        // ovaj dio sam uzeo sa vec postojeceg testa za odabir godine, predmeta i grupe
        var grupaVrijednost = ""
        var predmetNaziv = ""
        var godinaVrijednost = -1
        for (nk in nedodjeljeniKvizovi) {
            for (np in nedodjeljeniPredmeti) {
                if (nk.nazivPredmeta == np.naziv) {
                    grupaVrijednost = nk.nazivGrupe
                    godinaVrijednost = np.godina
                    predmetNaziv = np.naziv

                }
            }
        }

        Espresso.onData(CoreMatchers.allOf(CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)), CoreMatchers.`is`(godinaVrijednost.toString()))
        ).perform(click())
        onView(withId(R.id.odabirPredmet)).perform(click())
        Espresso.onData(CoreMatchers.allOf(
                CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)),
                CoreMatchers.`is`(predmetNaziv))
        ).perform(click())
        onView(withId(R.id.odabirGrupa)).perform(click())
        Espresso.onData(CoreMatchers.allOf(
                CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)),
                CoreMatchers.`is`(grupaVrijednost))
        ).perform(click())
        // poslije odabira godine, predmeta i grupe cu se vratiti na kvizove pa opet na upis da vidim da li se sve spasilo
        onView(withId(R.id.kvizovi)).perform(click())
        onView(withId(R.id.predmeti)).perform(click())
        // ako je sve ok trebalo bi cim pritisnem dodajPredmetDugme da se upise bez problema i provjeri da li se ispise tekst po postavci
        onView(withId(R.id.dodajPredmetDugme)).perform(click())
        onView(withSubstring("Uspješno ste upisani u grupu"))
        onView(withId(R.id.kvizovi)).perform(click())
        onView(withId(R.id.filterKvizova)).perform(click())
        // vracam se na svi moji kvizovi da vidimo da li se upisao predmet
        Espresso.onData(
                CoreMatchers.allOf(
                        CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)),
                        CoreMatchers.`is`("Svi moji kvizovi")
                )
        ).perform(click())
        val kvizoviPoslije = KvizRepository.getMyKvizes()

        onView(withId(R.id.listaKvizova)).check(UtilTestClass.hasItemCount(kvizoviPoslije.size))
        for (kviz in kvizoviPoslije) {
            UtilTestClass.itemTest(R.id.listaKvizova, kviz)
        }
        KvizRepository.ispisiSve()
    }

    @Test
    fun drugiTest() {
        // u ovom testu se prvo moram upisati na jedan aktivni kviz, odgovoriti na pitanja, izaci, vratiti se i predati kviz.
        // tako da cu iskoristiti kod iz prvog testa da se upisem na predmet za koji znam da je aktivan kviz a to je "RMA"
        // otvaram upis
        onView(withId(R.id.predmeti)).perform(click())
        // dohvacam neki kviz
        onView(withId(R.id.odabirGodina)).perform(click())
        // moze biti bilo koja grupa recimo G2
        var grupaVrijednost = "G2"
        var predmetNaziv = "RMA"
        var godinaVrijednost = 2
        Espresso.onData(CoreMatchers.allOf(CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)), CoreMatchers.`is`(godinaVrijednost.toString()))
        ).perform(click())
        onView(withId(R.id.odabirPredmet)).perform(click())
        Espresso.onData(CoreMatchers.allOf(
                CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)),
                CoreMatchers.`is`(predmetNaziv))
        ).perform(click())
        onView(withId(R.id.odabirGrupa)).perform(click())
        Espresso.onData(CoreMatchers.allOf(
                CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)),
                CoreMatchers.`is`(grupaVrijednost))
        ).perform(click())
        // upisujem se, provjerim da li pise, vracam se na kvizove
        onView(withId(R.id.dodajPredmetDugme)).perform(click())
        onView(withSubstring("Uspješno ste upisani u grupu"))
        onView(withId(R.id.kvizovi)).perform(click())
        // idem na upisane predmete
        onView(withId(R.id.filterKvizova)).perform(click())
        Espresso.onData(CoreMatchers.allOf(CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)), CoreMatchers.`is`("Svi moji kvizovi"))).perform(click())
        // klikam da novododani AKTIVNI RMA kviz
        val kvizovi = KvizRepository.getMyKvizes()
        onView(withId(R.id.listaKvizova)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(CoreMatchers.allOf(hasDescendant(withText(kvizovi[1].naziv)),
                hasDescendant(withText(kvizovi[1].nazivPredmeta))), click()))
        // klikam na prvo pitanje i odgovaram na sva pitanja tacno
        val pitanja = PitanjeKvizRepository.getPitanja(kvizovi[1].naziv, kvizovi[1].nazivPredmeta)
        var indeks = 0
        // odgovoriti cemo na sva pianja tacno
        for (pitanje in pitanja) {
            onView(withId(R.id.navigacijaPitanja)).perform(NavigationViewActions.navigateTo(indeks))
            onView(withId(R.id.tekstPitanja)).check(matches(withText(pitanja[indeks].tekst)))
            Espresso.onData(anything()).inAdapterView(withId(R.id.odgovoriLista)).atPosition(pitanje.tacan).perform(click())
            indeks++
            }
            // zaustavljam kviz
            onView(withId(R.id.zaustaviKviz)).perform(click())
            // ponovo ga pokrecem i predajem kako bi se vidjelo da se sacuvaju odgovori
            onView(withId(R.id.filterKvizova)).perform(click())
            Espresso.onData(CoreMatchers.allOf(CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)), CoreMatchers.`is`("Svi moji kvizovi"))).perform(click())
            // otvaram ponovo kviz
            onView(withId(R.id.listaKvizova)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(CoreMatchers.allOf(hasDescendant(withText(kvizovi[1].naziv)),
                    hasDescendant(withText(kvizovi[1].nazivPredmeta))), click()))
        // proci cemo sva pitanja i provjeriti da li su aktivna
        indeks = 0
        for (pitanje in pitanja) {
            onView(withId(R.id.navigacijaPitanja)).perform(NavigationViewActions.navigateTo(indeks))
            onView(withId(R.id.tekstPitanja)).check(matches(withText(pitanja[indeks].tekst)))
            Espresso.onData(anything()).inAdapterView(withId(R.id.odgovoriLista)).atPosition(pitanje.tacan)
                    .check(matches(Matchers.not(isEnabled())))
            indeks++
        }
        // predajem kviz
        onView(withId(R.id.predajKviz)).perform(click())
        // provjerim da li je korektno ispisao
        onView(withSubstring("Završili ste kviz " + kvizovi[1].naziv + " sa tačnosti 100")).check(matches(isDisplayed()))
        // zaustavljam kviz
        onView(withId(R.id.zaustaviKviz)).perform(click())
        // jos cu provjeriti da li se promijenio status iz zelenog (aktivni) u plavi (uradjeni kviz)
        onView(withId(R.id.filterKvizova)).perform(click())
        Espresso.onData(CoreMatchers.allOf(CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)), CoreMatchers.`is`("Svi moji kvizovi"))).perform(click())
        onView(withId(R.id.listaKvizova))
                .check(matches(hasDescendant(withText(kvizovi[1].naziv)))).check(matches(hasDescendant(withImage(R.drawable.plava))))
    }

}