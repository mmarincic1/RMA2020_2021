package ba.etf.rma21.projekat.data.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.UpisPredmet
import ba.etf.rma21.projekat.data.view.KvizListAdapter
import ba.etf.rma21.projekat.data.viewmodel.KvizListViewModel

class FragmentKvizovi : Fragment() {
    private lateinit var quizzes: RecyclerView
    private lateinit var quizzesAdapter: KvizListAdapter
    private var quizListViewModel = KvizListViewModel()
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

        quizzesAdapter = KvizListAdapter(arrayListOf())

        quizzes.adapter = quizzesAdapter

        napraviListenerZaSpinner()

        return view
    }
    companion object {
        fun newInstance(): FragmentKvizovi = FragmentKvizovi()

        var godina: Int = 0

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

    private fun updateQuizzes(): Unit{
        if(filterKvizova.selectedItem.toString() == "Svi kvizovi")
            quizzesAdapter.updateQuizes(quizListViewModel.getQuizzes())
        else if(filterKvizova.selectedItem.toString() == "Svi moji kvizovi")
            quizzesAdapter.updateQuizes(quizListViewModel.getMyQuizzes())
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
}