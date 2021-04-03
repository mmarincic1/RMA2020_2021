package ba.etf.rma21.projekat



import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma21.projekat.data.view.KvizListAdapter
import ba.etf.rma21.projekat.data.viewmodel.KvizListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    companion object{
        var godina: Int = 0
    }

    private lateinit var quizzes: RecyclerView
    private lateinit var quizzesAdapter: KvizListAdapter
    private var quizListViewModel = KvizListViewModel()
    private lateinit var newAction: FloatingActionButton
    private lateinit var filterKvizova: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        delegate.applyDayNight()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        filterKvizova = findViewById(R.id.filterKvizova)

        ArrayAdapter.createFromResource(
            this,
            R.array.izbor_za_spinner,
            android.R.layout.simple_spinner_item
        ).also { adapter ->

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            filterKvizova.adapter = adapter
        }
        quizzes = findViewById(R.id.listaKvizova)

        quizzes.setLayoutManager( GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false))
        quizzesAdapter = KvizListAdapter(arrayListOf())//{
            //showUpisPredmet()
        //}

        quizzes.adapter = quizzesAdapter

        napraviListenerZaSpinner()


        newAction = findViewById(R.id.upisDugme)
        newAction.setOnClickListener{
            showUpisPredmet()
//            .apply {
//                updateQuizzes()
//            }
//            updateQuizzes()
        }

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

    private fun showUpisPredmet() {
        val intent = Intent(this, UpisPredmet::class.java)
        //intent.putExtra("godina", godina)
            startActivity(intent)

    }

    override fun onResume() {
        super.onResume()
        updateQuizzes()
    }

    // OVE NISUUU
//    override fun onActivityReenter(resultCode: Int, data: Intent?) {
//        super.onActivityReenter(resultCode, data)
//        updateQuizzes()
//    }

    //    override fun onRestart() {
//        super.onRestart()
//        updateQuizzes()
//    }
//

}

