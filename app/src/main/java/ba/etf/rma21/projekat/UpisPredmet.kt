package ba.etf.rma21.projekat


import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import ba.etf.rma21.projekat.data.repositories.GrupaRepository
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.repositories.PredmetRepository


class UpisPredmet : AppCompatActivity() {

    private lateinit var odabirGodine: Spinner
    private lateinit var odabirPredmeta: Spinner
    private lateinit var odabirGrupe: Spinner
    private lateinit var upisiMe: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upis_predmet)

        odabirGodine = findViewById(R.id.odabirGodina)
        odabirPredmeta = findViewById(R.id.odabirPredmet)
        odabirGrupe = findViewById(R.id.odabirGrupa)

        ArrayAdapter.createFromResource(
            this,
            R.array.izbor_za_godine,
            android.R.layout.simple_spinner_item
        ).also { adapter ->

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            odabirGodine.adapter = adapter
        }

        odabirGodine.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                updatePredmete(odabirPredmeta)
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                updatePredmete(odabirPredmeta)
            }
        }

        odabirPredmeta.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                updateGrupe(odabirGrupe)
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                updateGrupe(odabirGrupe)
            }
        }
        upisiMe = findViewById(R.id.dodajPredmetDugme)

        upisiMe.setOnClickListener {
            KvizRepository.addMojiKvizovi(odabirPredmeta.selectedItem.toString(), odabirGrupe.selectedItem.toString())
            PredmetRepository.addUpisani(odabirGodine.selectedItem.toString().toInt(), odabirPredmeta.selectedItem.toString())
            this.finish()
        }

    }

    private fun updatePredmete(spinner1: Spinner): Unit{
        // Initializing a String Array
        val predmeti = PredmetRepository.getFromYear(odabirGodine.selectedItem.toString().toInt())

        // Initializing an ArrayAdapter
        val adapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            predmeti // Array
        )

        // Set the drop down view resource
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        // Finally, data bind the spinner object with dapter
        spinner1.adapter = adapter
    }

    private fun updateGrupe(spinner2: Spinner): Unit{
        val grupe = GrupaRepository.getGroupsByPredmet(odabirPredmeta.selectedItem.toString())


        // Initializing an ArrayAdapter
        val adapter1 = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            grupe // Array
        )

        // Set the drop down view resource
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        // Finally, data bind the spinner object with dapter
        spinner2.adapter = adapter1
    }
}
