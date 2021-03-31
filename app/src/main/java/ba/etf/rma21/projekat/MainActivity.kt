package ba.etf.rma21.projekat


import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner

import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

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


    }
}

