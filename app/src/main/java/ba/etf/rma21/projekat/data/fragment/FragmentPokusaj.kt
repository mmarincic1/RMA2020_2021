package ba.etf.rma21.projekat.data.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.MainActivity
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.fragment.FragmentKvizovi.Companion.uradjeniKviz
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.viewmodel.PitanjeKvizViewModel
import com.google.android.material.navigation.NavigationView

class FragmentPokusaj(private var listaPitanja: List<Pitanje>) : Fragment() {

    companion object{
        var brojPitanja: Int = 0
        var odgovor = -1
        lateinit var navigationView: NavigationView
        var indexPitanja = ""
    }
    private var pitanjeKvizViewModel = PitanjeKvizViewModel()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_pokusaj, container, false)
        napraviBottomNav()

        navigationView = view.findViewById(R.id.navigacijaPitanja)
        napraviNavView()

        navigationView.setNavigationItemSelectedListener { item ->
            when(item.toString()){
                // Po postaci pise da trebaju biti samo 3 pitanja iako je nekada u buducnosti moguce da ce biti vise
                // tada bi trebao naci nacin da dinamicki ovo pravim
                "1" -> {
                    odgovor = pitanjeKvizViewModel.getOdgovorNaPitanje(uradjeniKviz, FragmentKvizovi.uradjeniPredmet, listaPitanja.get(0).naziv)
                    val fragment = FragmentPitanje(listaPitanja.get(0))
                    indexPitanja = "1"
                    openFragment(fragment)
                }
                "2" -> {
                    odgovor = pitanjeKvizViewModel.getOdgovorNaPitanje(uradjeniKviz, FragmentKvizovi.uradjeniPredmet, listaPitanja.get(1).naziv)
                    val fragment = FragmentPitanje(listaPitanja.get(1))
                    indexPitanja = "2"
                    openFragment(fragment)
                }
                "3" -> {
                    odgovor = pitanjeKvizViewModel.getOdgovorNaPitanje(uradjeniKviz, FragmentKvizovi.uradjeniPredmet, listaPitanja.get(2).naziv)
                    val fragment = FragmentPitanje(listaPitanja.get(2))
                    indexPitanja = "3"
                    openFragment(fragment)
                }
                "4" -> {
                    odgovor = pitanjeKvizViewModel.getOdgovorNaPitanje(uradjeniKviz, FragmentKvizovi.uradjeniPredmet, listaPitanja.get(3).naziv)
                    val fragment = FragmentPitanje(listaPitanja.get(3))
                    indexPitanja = "4"
                    openFragment(fragment)
                }
                "5" -> {
                    odgovor = pitanjeKvizViewModel.getOdgovorNaPitanje(uradjeniKviz, FragmentKvizovi.uradjeniPredmet, listaPitanja.get(4).naziv)
                    val fragment = FragmentPitanje(listaPitanja.get(4))
                    indexPitanja = "5"
                    openFragment(fragment)
                }
                "Rezultat" ->{
                    val rezultat = pitanjeKvizViewModel.getRezultat(uradjeniKviz, FragmentKvizovi.uradjeniPredmet)
                    val kvizoviFragments = FragmentPoruka.newInstance(
                        "Završili ste kviz " + uradjeniKviz + " sa tačnosti " +
                                rezultat
                    )
                    indexPitanja = ""
                    openFragment(kvizoviFragments)
                }

            }
            false
        }

        // PITAJ TREBA LI OVO????
//        val fragment = FragmentPitanje(listaPitanja.get(0))
//        openFragment(fragment)

        brojPitanja = listaPitanja.size

        return view
    }

    private fun napraviNavView() {
        val brojPitanja: Int = listaPitanja.size
        for(i in 1 .. brojPitanja)
            navigationView.menu.add(i.toString())

        if(pitanjeKvizViewModel.getZavrsenKviz(uradjeniKviz, FragmentKvizovi.uradjeniPredmet)){
            navigationView.menu.add("Rezultat")
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
            val rezultat =  pitanjeKvizViewModel.getRezultat(uradjeniKviz, FragmentKvizovi.uradjeniPredmet)
            val kvizoviFragments = FragmentPoruka.newInstance(
                            "Završili ste kviz " + uradjeniKviz + " sa tačnosti " +
                                    rezultat
                        )
            // jer ima samo 3 pitanja svaki kviz ZA SADA (to je po postavci)
            if(navigationView.menu.size() < 4){
                pitanjeKvizViewModel.zavrsiKviz(uradjeniKviz, FragmentKvizovi.uradjeniPredmet)
                navigationView.menu.add("Rezultat")
                openFragment(kvizoviFragments)
            }
            return@OnMenuItemClickListener true
        }

        MainActivity.bottomNavigation.menu.findItem(R.id.predajKviz).setOnMenuItemClickListener(predajKvizItemClickListener)
        MainActivity.bottomNavigation.menu.findItem(R.id.zaustaviKviz).setOnMenuItemClickListener(zaustaviKvizItemClickListener)
    }


}
