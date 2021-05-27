package ba.etf.rma21.projekat.data.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma21.projekat.MainActivity
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.view.KvizListAdapter
import ba.etf.rma21.projekat.data.viewmodel.KvizListViewModel
import ba.etf.rma21.projekat.data.viewmodel.PitanjeKvizViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentKvizovi : Fragment() {
    private lateinit var quizzes: RecyclerView
    private lateinit var quizzesAdapter: KvizListAdapter
    private var quizListViewModel = KvizListViewModel()
    private var pitanjaKvizViewModel = PitanjeKvizViewModel()
    private lateinit var filterKvizova: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_kvizovi, container, false)

        filterKvizova = view.findViewById(R.id.filterKvizova)

        ArrayAdapter.createFromResource(
            view.context,
            R.array.izbor_za_spinner,
            android.R.layout.simple_spinner_item
        ).also { adapter ->

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            filterKvizova.adapter = adapter
        }
        quizzes = view.findViewById(R.id.listaKvizova)

        quizzes.setLayoutManager( GridLayoutManager(view.context, 2, GridLayoutManager.VERTICAL, false))



        quizzesAdapter = KvizListAdapter(arrayListOf()) { kviz ->
            showKviz(kviz)
        }

        quizzes.adapter = quizzesAdapter


        napraviListenerZaSpinner()
        napraviBottomNav()
        return view
    }
    companion object {
        fun newInstance(): FragmentKvizovi = FragmentKvizovi()
    }

    private fun napraviListenerZaSpinner() {
        filterKvizova.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                updateQuizzes()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateQuizzes()
            }
        }
    }

    private fun updateQuizzes(){
        if(filterKvizova.selectedItem.toString() == "Svi kvizovi")
            quizListViewModel.getQuizzes(
                onSuccess = ::onSuccess,
                onError = ::onError)
        else if(filterKvizova.selectedItem.toString() == "Svi moji kvizovi")
            quizListViewModel.getMyQuizzes(
                onSuccess = ::onSuccess,
                onError = ::onError)
        else if(filterKvizova.selectedItem.toString() == "Urađeni kvizovi")
            quizzesAdapter.updateQuizes(quizListViewModel.getDoneQuizzes())
        else if(filterKvizova.selectedItem.toString() == "Budući kvizovi")
            quizzesAdapter.updateQuizes(quizListViewModel.getFutureQuizzes())
        else quizzesAdapter.updateQuizes(quizListViewModel.getPastQuizzes())
    }

    override fun onResume() {
        super.onResume()
        updateQuizzes()
    }

    private fun showKviz(kviz: Kviz) {
        if(filterKvizova.selectedItem.toString() != "Svi kvizovi" &&
                quizListViewModel.getStatus(kviz) != "zuta") {

            pitanjaKvizViewModel.setUradjeniKviz(kviz.naziv)

            kviz.nazivPredmeta?.let { pitanjaKvizViewModel.setUradjeniPredmet(it) }

            KvizRepository.pokrenutiKviz = kviz
            pitanjaKvizViewModel.getPitanja(kviz.id, onSuccess = ::onSuccess1, onError = ::onError)

        }
    }

    private fun napraviBottomNav() {
        MainActivity.bottomNavigation.menu.findItem(R.id.kvizovi).isVisible = true
        MainActivity.bottomNavigation.menu.findItem(R.id.predmeti).isVisible = true
        MainActivity.bottomNavigation.menu.findItem(R.id.predajKviz).isVisible = false
        MainActivity.bottomNavigation.menu.findItem(R.id.zaustaviKviz).isVisible = false
    }

    fun onSuccess(quizzes:List<Kviz>){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                quizzesAdapter.updateQuizes(quizzes)
            }
        }
    }

    fun onSuccess1(pitanja:List<Pitanje>){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                val fragment =
                    FragmentPokusaj(pitanja)

                var fr = getFragmentManager()?.beginTransaction()
                if (fragment != null) {
                    fr?.replace(R.id.container, fragment)
                }
                fr?.commit()
            }
        }
    }


    fun onError() {
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                val toast = Toast.makeText(context, "Search error", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

}