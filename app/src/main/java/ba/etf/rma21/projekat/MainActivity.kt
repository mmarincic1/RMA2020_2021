package ba.etf.rma21.projekat


import android.content.Intent
import android.os.Bundle
import android.view.View
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

    private lateinit var quizzes: RecyclerView
    private lateinit var quizzesAdapter: KvizListAdapter
    private var quizListViewModel = KvizListViewModel()
    private lateinit var newAction: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        delegate.applyDayNight()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner: Spinner = findViewById(R.id.filterKvizova)

        ArrayAdapter.createFromResource(
            this,
            R.array.izbor_za_spinner,
            android.R.layout.simple_spinner_item
        ).also { adapter ->

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinner.adapter = adapter
        }
        quizzes = findViewById(R.id.listaKvizova)

        quizzes.setLayoutManager( GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false))
        quizzesAdapter = KvizListAdapter(arrayListOf())//{
            //showUpisPredmet()
        //}

        quizzes.adapter = quizzesAdapter
        quizzesAdapter.updateQuizes(quizListViewModel.getQuizzes())

        newAction = findViewById(R.id.upisDugme)
        newAction.setOnClickListener{showUpisPredmet()}
    }


    private fun showUpisPredmet() {
        val intent = Intent(this, UpisPredmet::class.java)
        startActivity(intent)
    }

}

