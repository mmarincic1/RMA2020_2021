package ba.etf.rma21.projekat.data.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.MainActivity
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.PitanjeKvizRepository.Companion.getPitanja
import com.google.android.material.navigation.NavigationView

class FragmentPokusaj(private var listaPitanja: List<Pitanje>) : Fragment() {

    private lateinit var navigationView: NavigationView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_pokusaj, container, false)
        napraviBottomNav()

        navigationView = view.findViewById(R.id.navigacijaPitanja)
        napraviNavView()
        return view
    }

    private fun napraviNavView() {
        val brojPitanja: Int = listaPitanja.size
        //navigationView.menu.add(brojPitanja.toString())
        for(i in 1 .. brojPitanja)
            navigationView.menu.add(i.toString())
    }

//    companion object {
//        fun newInstance(kviz: Kviz): FragmentPokusaj = FragmentPokusaj(getPitanja(kviz.naziv, kviz.nazivPredmeta)).apply {
//            arguments = Bundle().apply {
//                // ovdje cemo raditi sa kvizom
//            }
//        }
//    }

    private fun openFragment(fragment: Fragment) {
        var fr = getFragmentManager()?.beginTransaction()
        fr?.replace(R.id.container, fragment)
        fr?.commit()
    }

    private fun napraviBottomNav() {
        MainActivity.bottomNavigation.menu.findItem(R.id.kvizovi).isVisible = false
        MainActivity.bottomNavigation.menu.findItem(R.id.predmeti).isVisible = false
        MainActivity.bottomNavigation.menu.findItem(R.id.predajKviz).isVisible = true
        MainActivity.bottomNavigation.menu.findItem(R.id.zaustaviKviz).isVisible = true
    }
}
