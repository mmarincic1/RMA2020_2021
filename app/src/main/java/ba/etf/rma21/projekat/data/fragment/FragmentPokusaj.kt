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
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.viewmodel.KvizListViewModel
import ba.etf.rma21.projekat.data.viewmodel.PitanjeKvizViewModel
import com.google.android.material.navigation.NavigationView
import java.util.*

class FragmentPokusaj(private var listaPitanja: List<Pitanje>) : Fragment() {

    companion object{
        lateinit var navigationView: NavigationView
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
                var rezultat = pitanjeKvizViewModel.getRezultat(pitanjeKvizViewModel.getUradjeniKviz(), pitanjeKvizViewModel.getUradjeniPredmet())
                if(rezultat == -1)
                    rezultat = 0
                val kvizoviFragments = FragmentPoruka.newInstance(
                        "Završili ste kviz " + pitanjeKvizViewModel.getUradjeniKviz() + " sa tačnosti " +
                                rezultat
                )
                pitanjeKvizViewModel.setIndexPitanja("")
                openFragment(kvizoviFragments)
            }
            else {
                when (item.toString().toInt()) {
                    in 1..listaPitanja.size -> {

                        pitanjeKvizViewModel.setOdgovor(
                        pitanjeKvizViewModel.
                        getOdgovorNaPitanje(pitanjeKvizViewModel.getUradjeniKviz(),
                                pitanjeKvizViewModel.getUradjeniPredmet(), listaPitanja.get(item.toString().toInt() - 1).naziv))


                        val fragment = FragmentPitanje(listaPitanja.get(item.toString().toInt() - 1))
                        pitanjeKvizViewModel.setIndexPitanja((item.toString().toInt()).toString())
                        openFragment(fragment)
                    }

                }
            }
            false
        }

        //prvo pitanje se odmah prikazuje
        navigationView.setCheckedItem(0)
        pitanjeKvizViewModel.setIndexPitanja("1")
        pitanjeKvizViewModel.setOdgovor(pitanjeKvizViewModel.getOdgovorNaPitanje(pitanjeKvizViewModel.getUradjeniKviz(),
                pitanjeKvizViewModel.getUradjeniPredmet(), listaPitanja.get(0).naziv))
        val fragment = FragmentPitanje(listaPitanja.get(0))
        openFragment(fragment)
        //
        pitanjeKvizViewModel.setBrojPitanja(listaPitanja.size)

        return view
    }

    private fun napraviNavView() {
        val brojPitanja: Int = listaPitanja.size
        for(i in 1 .. brojPitanja)
            navigationView.menu.add(123456, i-1, i-1, (i).toString())

//        for (i in 0 until listaPitanja.size) {
//             pitanjeKvizViewModel.setOdgovor(pitanjeKvizViewModel.getOdgovorNaPitanje(pitanjeKvizViewModel.getUradjeniKviz(),
//                    pitanjeKvizViewModel.getUradjeniPredmet(), listaPitanja.get(i).naziv))
//            val odgovor = pitanjeKvizViewModel.getOdgovor()
//            // zavrsen kviz i nije dao odgovor znaci crvena boja ili dao pogresan odgovor
//            if((odgovor == -1 &&
//                        (pitanjeKvizViewModel.getZavrsenKviz(pitanjeKvizViewModel.getUradjeniKviz(),
//                                pitanjeKvizViewModel.getUradjeniPredmet())
//                                || kvizViewModel.getStatus(KvizRepository.pokrenutiKviz) == "crvena"))
//                || (odgovor != -1 && odgovor != listaPitanja.get(i).tacan)){
//                val menuItem: MenuItem = navigationView.menu.getItem(i)
//                val spanString =
//                    SpannableString(menuItem.getTitle().toString())
//                spanString.setSpan(
//                    ForegroundColorSpan(Color.parseColor("#DB4F3D")),
//                    0,
//                    spanString.length,
//                    0
//                )
//                menuItem.setTitle(spanString)
//            }
//            // tacan odgovor
//            else if(odgovor == listaPitanja.get(i).tacan) {
//                val menuItem: MenuItem = navigationView.menu.getItem(i)
//                val spanString =
//                    SpannableString(menuItem.getTitle().toString())
//                spanString.setSpan(
//                    ForegroundColorSpan(Color.parseColor("#3DDC84")),
//                    0,
//                    spanString.length,
//                    0
//                )
//                menuItem.setTitle(spanString)
//            }
//        }
//
//        if(pitanjeKvizViewModel.getZavrsenKviz(pitanjeKvizViewModel.getUradjeniKviz(), pitanjeKvizViewModel.getUradjeniPredmet())){
//            navigationView.menu.add(123456, brojPitanja, brojPitanja, "Rezultat")
//        }
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
            if(kvizViewModel.getStatus(KvizRepository.pokrenutiKviz) != "crvena") {
                var rezultat =
                    pitanjeKvizViewModel.getRezultat(pitanjeKvizViewModel.getUradjeniKviz(), pitanjeKvizViewModel.getUradjeniPredmet())
                if (rezultat == -1)
                    rezultat = 0
                val kvizoviFragments = FragmentPoruka.newInstance(
                    "Završili ste kviz " + pitanjeKvizViewModel.getUradjeniKviz() + " sa tačnosti " +
                            rezultat
                )
                // jer ima manje nego broj pitanja + 1 a to je rezultat
                if (navigationView.menu.size() <= listaPitanja.size) {
                    var godina = Calendar.getInstance().get(Calendar.YEAR)
                    var mjesec = Calendar.getInstance().get(Calendar.MONTH) + 1
                    var dan = Calendar.getInstance().get(Calendar.DATE)
                    kvizViewModel.zavrsiKviz(
                        Date(godina, mjesec, dan),
                        pitanjeKvizViewModel.getUradjeniPredmet(),
                        pitanjeKvizViewModel.getUradjeniKviz(),
                        rezultat
                    )
                    pitanjeKvizViewModel.zavrsiKviz(pitanjeKvizViewModel.getUradjeniKviz(), pitanjeKvizViewModel.getUradjeniPredmet())
                    // ovo je ako nije odgovorio na neka pitanja a odmah otisao da preda kviz, oni moraju da pocrvene
                    navigationView.menu.clear()
                    napraviNavView()

                    // izbacuje jedan visak "Rezultat"
                    if(navigationView.menu.findItem(listaPitanja.size) != null){
                        navigationView.menu.removeItem(listaPitanja.size)
                    }

                    if (pitanjeKvizViewModel.getZavrsenKviz(
                            pitanjeKvizViewModel.getUradjeniKviz(),
                             pitanjeKvizViewModel.getUradjeniPredmet()
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
