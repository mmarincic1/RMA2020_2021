package ba.etf.rma21.projekat


import android.app.Activity
import android.content.Intent
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
        //var godina = intent.getIntExtra("godina", 0)

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

        odabirGodine.setSelection(MainActivity.godina)

        odabirGodine.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                updatePredmete(odabirPredmeta)
                updateGrupe(odabirGrupe)
                MainActivity.godina = odabirGodine.selectedItemPosition
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                updatePredmete(odabirPredmeta)
                updateGrupe(odabirGrupe)
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
            //MainActivity.godina = odabirGodine.selectedItemPosition
            this.finish()
        }

    }

    private fun updatePredmete(spinner1: Spinner): Unit{
        val predmeti = PredmetRepository.getFromYear(odabirGodine.selectedItem.toString().toInt())

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            predmeti
        )

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        spinner1.adapter = adapter
        if(predmeti.size == 0) upisiMe.isClickable = false
        else upisiMe.isClickable = true
    }

    private fun updateGrupe(spinner2: Spinner): Unit{
        if((odabirPredmeta.selectedItem == null)){
            val grupe = emptyArray<String>()
            val adapter1 = ArrayAdapter(
                this, // Context
                android.R.layout.simple_spinner_item, // Layout
                grupe // Array
            )

            adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

            spinner2.adapter = adapter1
            upisiMe.isClickable = false
        }
        else {
            var grupe = GrupaRepository.getGroupsByPredmet(odabirPredmeta.selectedItem.toString())

            val adapter1 = ArrayAdapter(
                this, // Context
                android.R.layout.simple_spinner_item, // Layout
                grupe // Array
            )

            adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

            spinner2.adapter = adapter1
            upisiMe.isClickable = true
        }
    }
}
