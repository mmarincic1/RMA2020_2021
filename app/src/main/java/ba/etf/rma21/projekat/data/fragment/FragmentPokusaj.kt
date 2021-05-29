package ba.etf.rma21.projekat.data.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.MainActivity
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.viewmodel.KvizListViewModel
import ba.etf.rma21.projekat.data.viewmodel.PitanjeKvizViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                KvizRepository.radjeniKviz?.id?.let { pitanjeKvizViewModel.getRezultat(it, onSuccess = ::onSuccess3, onError = ::onError) }
            }
            else {
                when (item.toString().toInt()) {
                    in 1..listaPitanja.size -> {


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
        val fragment = FragmentPitanje(listaPitanja.get(0))
        openFragment(fragment)
        return view
    }

    private fun napraviNavView() {
        val brojPitanja: Int = listaPitanja.size
        for(i in 1 .. brojPitanja)
            navigationView.menu.add(123456, i-1, i-1, (i).toString())

        for (i in 0 until listaPitanja.size) {
            KvizRepository.radjeniKviz?.id?.let {
                pitanjeKvizViewModel.getOdgovorAppZaPokusaj(
                    it, listaPitanja[i].id, i , onSuccess = ::onSuccess, onError = ::onError)
            }
        }
        KvizRepository.radjeniKviz?.let { pitanjeKvizViewModel.getZavrsenKviz(it, onSuccess = ::onSuccess2, onError = ::onError) }
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
        MainActivity.bottomNavigation.menu.findItem(R.id.predajKviz).isEnabled = true
        MainActivity.bottomNavigation.menu.findItem(R.id.zaustaviKviz).isVisible = true

        val zaustaviKvizItemClickListener = MenuItem.OnMenuItemClickListener {
            val kvizoviFragments = FragmentKvizovi.newInstance()
            openFragment1(kvizoviFragments)
            return@OnMenuItemClickListener true
        }

        val predajKvizItemClickListener = MenuItem.OnMenuItemClickListener {
            // MOZE SAMO PREDATI KVIZ AKO JE KVIZ AKTIVAN
            if(kvizViewModel.getStatus(KvizRepository.pokrenutiKviz) != "crvena") {
                KvizRepository.radjeniKviz?.id?.let { it1 -> pitanjeKvizViewModel.getRezultat(it1, onSuccess = ::onSuccess1, onError = ::onError) }
                MainActivity.bottomNavigation.menu.findItem(R.id.predajKviz).isEnabled = false
            }
            return@OnMenuItemClickListener true
        }

        MainActivity.bottomNavigation.menu.findItem(R.id.predajKviz).setOnMenuItemClickListener(predajKvizItemClickListener)
        MainActivity.bottomNavigation.menu.findItem(R.id.zaustaviKviz).setOnMenuItemClickListener(zaustaviKvizItemClickListener)
    }

    fun onSuccess(odgovor: Int, i :Int) {
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                // tacan odg
                if (odgovor == listaPitanja.get(i).tacan) {
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
                // zavrsen kviz i nije dao odgovor znaci crvena boja ili dao pogresan odgovor
                if ((odgovor != -1 && odgovor != listaPitanja.get(i).tacan)) {
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
            }
        }
    }

    fun onSuccess1(rezultat: Int){
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                // ZAVRSI KVIZ
                KvizRepository.radjeniKviz?.let { kvizViewModel.zavrsiKviz(it, rezultat , onSuccess = ::onSuccess4, onError = ::onError) }
            }
        }
    }

    fun onSuccess2(rezultat: Boolean){
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                // ZAVRSI KVIZ
                if(rezultat){
                    navigationView.menu.add(123456, listaPitanja.size, listaPitanja.size, "Rezultat")
                    MainActivity.bottomNavigation.menu.findItem(R.id.predajKviz).isEnabled = false
                }
            }
        }
    }

    fun onSuccess3(rezultat: Int){
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                val kvizoviFragments = FragmentPoruka.newInstance(
                    "Završili ste kviz " + KvizRepository.pokrenutiKviz.naziv + " sa tačnosti " +
                            rezultat
                )
                    openFragment(kvizoviFragments)
            }
        }
    }

    fun onSuccess4(rezultat: Int){
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                val kvizoviFragments = FragmentPoruka.newInstance(
                    "Završili ste kviz " + KvizRepository.pokrenutiKviz.naziv + " sa tačnosti " +
                            rezultat
                )
                navigationView.menu.clear()
                napraviNavView()
                openFragment(kvizoviFragments)
            }
        }
    }

    fun onError() {
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                val toast = Toast.makeText(context, "Neki error", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
}
