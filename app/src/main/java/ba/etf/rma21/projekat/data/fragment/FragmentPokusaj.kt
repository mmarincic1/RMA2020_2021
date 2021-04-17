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
import com.google.android.material.navigation.NavigationView

class FragmentPokusaj(private var listaPitanja: List<Pitanje>) : Fragment() {

    companion object{
        var brojPitanja: Int = 0
        var brojTacnih = 0
    }

    private lateinit var navigationView: NavigationView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_pokusaj, container, false)
        napraviBottomNav()

        navigationView = view.findViewById(R.id.navigacijaPitanja)
        napraviNavView()

        navigationView.setNavigationItemSelectedListener { item ->
            when(item.toString()){
                "1" -> { val fragment = FragmentPitanje(listaPitanja.get(0))
                    openFragment(fragment)
                }
                "2" -> {
                    val fragment = FragmentPitanje(listaPitanja.get(1))
                    openFragment(fragment)
                }
                "3" -> {
                    val fragment = FragmentPitanje(listaPitanja.get(2))
                    openFragment(fragment)
                }
                "4" -> {
                    val fragment = FragmentPitanje(listaPitanja.get(3))
                    openFragment(fragment)
                }
                "5" -> {
                    val fragment = FragmentPitanje(listaPitanja.get(4))
                    openFragment(fragment)
                }
            }
            false
        }

        // PITAJ TREBA LI OVO????
        val fragment = FragmentPitanje(listaPitanja.get(0))
        openFragment(fragment)

        brojPitanja = listaPitanja.size

        return view
    }

    private fun napraviNavView() {
        val brojPitanja: Int = listaPitanja.size
        for(i in 1 .. brojPitanja)
            navigationView.menu.add(i.toString())
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
            val kvizoviFragments = FragmentPoruka.newInstance(
                            "Završili ste kviz " + uradjeniKviz + " sa tačnosti " +
                                    ((brojTacnih.toDouble() / brojPitanja.toDouble())*100).toInt()
                        )
            brojTacnih = 0
            openFragment(kvizoviFragments)
            return@OnMenuItemClickListener true
        }

        MainActivity.bottomNavigation.menu.findItem(R.id.predajKviz).setOnMenuItemClickListener(predajKvizItemClickListener)
        MainActivity.bottomNavigation.menu.findItem(R.id.zaustaviKviz).setOnMenuItemClickListener(zaustaviKvizItemClickListener)
    }


}
