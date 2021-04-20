package ba.etf.rma21.projekat.data.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.MainActivity
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.fragment.FragmentKvizovi.Companion.uradjeniKviz
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.viewmodel.KvizListViewModel
import ba.etf.rma21.projekat.data.viewmodel.PitanjeKvizViewModel
import com.google.android.material.navigation.NavigationView
import java.util.*

class FragmentPokusaj(private var listaPitanja: List<Pitanje>) : Fragment() {

    companion object{
        var brojPitanja: Int = 0
        var odgovor = -1
        lateinit var navigationView: NavigationView
        var indexPitanja = ""
    }
    private var pitanjeKvizViewModel = PitanjeKvizViewModel()
    private var kvizViewModel = KvizListViewModel()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_pokusaj, container, false)
        napraviBottomNav()

        navigationView = view.findViewById(R.id.navigacijaPitanja)
        napraviNavView()

        navigationView.setNavigationItemSelectedListener { item ->
            if(item.toString() == "Rezultat"){
                var rezultat = pitanjeKvizViewModel.getRezultat(uradjeniKviz, FragmentKvizovi.uradjeniPredmet)
                if(rezultat == -1)
                    rezultat = 0
                val kvizoviFragments = FragmentPoruka.newInstance(
                        "Završili ste kviz " + uradjeniKviz + " sa tačnosti " +
                                rezultat
                )
                indexPitanja = ""
                openFragment(kvizoviFragments)
            }
            else {
                when (item.toString().toInt()) {
                    in 1..listaPitanja.size -> {
                        odgovor = pitanjeKvizViewModel.getOdgovorNaPitanje(uradjeniKviz, FragmentKvizovi.uradjeniPredmet, listaPitanja.get(item.toString().toInt() - 1).naziv)
                        val fragment = FragmentPitanje(listaPitanja.get(item.toString().toInt() - 1))
                        indexPitanja = (item.toString().toInt()).toString()
                        openFragment(fragment)
                    }

                }
            }
            false
        }

        //prvo pitanje se odmah prikazuje
        navigationView.setCheckedItem(0)
        indexPitanja = "1"
        odgovor = pitanjeKvizViewModel.getOdgovorNaPitanje(uradjeniKviz, FragmentKvizovi.uradjeniPredmet, listaPitanja.get(0).naziv)
        val fragment = FragmentPitanje(listaPitanja.get(0))
        openFragment(fragment)
        //
        brojPitanja = listaPitanja.size

        return view
    }

    private fun napraviNavView() {
        val brojPitanja: Int = listaPitanja.size
        for(i in 1 .. brojPitanja)
            navigationView.menu.add(123456, i-1, i-1, (i).toString())

        for (i in 0 until listaPitanja.size) {
            odgovor = pitanjeKvizViewModel.getOdgovorNaPitanje(uradjeniKviz, FragmentKvizovi.uradjeniPredmet, listaPitanja.get(i).naziv)
            // zavrsen kviz i nije dao odgovor znaci crvena boja ili dao pogresan odgovor
            if((odgovor == -1 &&
                        (pitanjeKvizViewModel.getZavrsenKviz(uradjeniKviz, FragmentKvizovi.uradjeniPredmet)
                                || kvizViewModel.getStatus(FragmentKvizovi.uradjeniPredmet, uradjeniKviz) == "crvena"))
                || (odgovor != -1 && odgovor != listaPitanja.get(i).tacan)){
                val menuItem: MenuItem = navigationView.menu.getItem(i)
                val spanString =
                    SpannableString(menuItem.getTitle().toString())
                spanString.setSpan(
                    ForegroundColorSpan(Color.parseColor("#DB4F3D")),
                    0,
                    spanString.length,
                    0
                )
                menuItem.setTitle(spanString)
            }
            // tacan odgovor
            else if(odgovor == listaPitanja.get(i).tacan) {
                val menuItem: MenuItem = navigationView.menu.getItem(i)
                val spanString =
                    SpannableString(menuItem.getTitle().toString())
                spanString.setSpan(
                    ForegroundColorSpan(Color.parseColor("#3DDC84")),
                    0,
                    spanString.length,
                    0
                )
                menuItem.setTitle(spanString)
            }
        }

        if(pitanjeKvizViewModel.getZavrsenKviz(uradjeniKviz, FragmentKvizovi.uradjeniPredmet)){
            navigationView.menu.add(123456, brojPitanja, brojPitanja, "Rezultat")
        }
    }

    private fun openFragment(fragment: Fragment) {
        var fr = getFragmentManager()?.beginTransaction()
        fr?.replace(R.id.framePitanje, fragment)
        fr?.commit()
    }

    private fun openFragment1(fragment: Fragment) {
        var fr = getFragmentManager()?.beginTransaction()
        fr?.replace(R.id.container, fragment)
        fr?.commit()
    }

    private fun napraviBottomNav() {
        MainActivity.bottomNavigation.menu.findItem(R.id.kvizovi).isVisible = false
        MainActivity.bottomNavigation.menu.findItem(R.id.predmeti).isVisible = false
        MainActivity.bottomNavigation.menu.findItem(R.id.predajKviz).isVisible = true
        MainActivity.bottomNavigation.menu.findItem(R.id.zaustaviKviz).isVisible = true

        val zaustaviKvizItemClickListener = MenuItem.OnMenuItemClickListener {
            val kvizoviFragments = FragmentKvizovi.newInstance()
            openFragment1(kvizoviFragments)
            return@OnMenuItemClickListener true
        }

        val predajKvizItemClickListener = MenuItem.OnMenuItemClickListener {
            // MOZE SAMO PREDATI KVIZ AKO JE KVIZ AKTIVAN
            if(kvizViewModel.getStatus(FragmentKvizovi.uradjeniPredmet, uradjeniKviz) != "crvena") {
                var rezultat =
                    pitanjeKvizViewModel.getRezultat(uradjeniKviz, FragmentKvizovi.uradjeniPredmet)
                if (rezultat == -1)
                    rezultat = 0
                val kvizoviFragments = FragmentPoruka.newInstance(
                    "Završili ste kviz " + uradjeniKviz + " sa tačnosti " +
                            rezultat
                )
                // jer ima manje nego broj pitanja + 1 a to je rezultat
                if (navigationView.menu.size() <= listaPitanja.size) {
                    var godina = Calendar.getInstance().get(Calendar.YEAR)
                    var mjesec = Calendar.getInstance().get(Calendar.MONTH) + 1
                    var dan = Calendar.getInstance().get(Calendar.DATE)
                    kvizViewModel.zavrsiKviz(
                        Date(godina, mjesec, dan),
                        FragmentKvizovi.uradjeniPredmet,
                        uradjeniKviz,
                        rezultat
                    )
                    pitanjeKvizViewModel.zavrsiKviz(uradjeniKviz, FragmentKvizovi.uradjeniPredmet)
                    // ovo je ako nije odgovorio na neka pitanja a odmah otisao da preda kviz, oni moraju da pocrvene
                    navigationView.menu.clear()
                    napraviNavView()

                    // izbacuje jedan visak "Rezultat"
                    if(navigationView.menu.findItem(listaPitanja.size) != null){
                        navigationView.menu.removeItem(listaPitanja.size)
                    }

                    if (pitanjeKvizViewModel.getZavrsenKviz(
                            uradjeniKviz,
                            FragmentKvizovi.uradjeniPredmet
                        )
                    ) {
                        navigationView.menu.add(
                            123456,
                            listaPitanja.size,
                            listaPitanja.size,
                            "Rezultat"
                        )
                    }
                    openFragment(kvizoviFragments)
                }
            }
            return@OnMenuItemClickListener true
        }

        MainActivity.bottomNavigation.menu.findItem(R.id.predajKviz).setOnMenuItemClickListener(predajKvizItemClickListener)
        MainActivity.bottomNavigation.menu.findItem(R.id.zaustaviKviz).setOnMenuItemClickListener(zaustaviKvizItemClickListener)
    }
}
