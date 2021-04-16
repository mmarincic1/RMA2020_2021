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
import ba.etf.rma21.projekat.data.viewmodel.GroupViewModel
import ba.etf.rma21.projekat.data.viewmodel.KvizListViewModel
import ba.etf.rma21.projekat.data.viewmodel.PredmetViewModel


class UpisPredmet : AppCompatActivity() {
//
//    private lateinit var odabirGodine: Spinner
//    private lateinit var odabirPredmeta: Spinner
//    private lateinit var odabirGrupe: Spinner
//    private lateinit var upisiMe: Button
//    private var quizListViewModel = KvizListViewModel()
//    private var predmetViewModel = PredmetViewModel()
//    private var grupaViewModel = GroupViewModel()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_upis_predmet)
//
//        odabirGodine = findViewById(R.id.odabirGodina)
//        odabirPredmeta = findViewById(R.id.odabirPredmet)
//        odabirGrupe = findViewById(R.id.odabirGrupa)
//
//        ArrayAdapter.createFromResource(
//            this,
//            R.array.izbor_za_godine,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//
//            odabirGodine.adapter = adapter
//        }
//
//        odabirGodine.setSelection(MainActivity.godina)
//
//        odabirGodine.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
//                if(odabirGodine.selectedItem.toString() != "") {
//                    odabirPredmeta.isEnabled = true
//                    updatePredmete(odabirPredmeta)
//                    updateGrupe(odabirGrupe)
//                }
//                else {
//                    odabirPredmeta.isEnabled = false
//                    odabirGrupe.isEnabled = false
//                }
//                updateClickerUpis()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>){
//                updatePredmete(odabirPredmeta)
//                updateGrupe(odabirGrupe)
//            }
//        }
//
//        odabirPredmeta.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
//                if(odabirPredmeta.selectedItem.toString() != "") {
//                    odabirGrupe.isEnabled = true
//                    updateGrupe(odabirGrupe)
//                }
//                else {
//                    odabirGrupe.isEnabled = false
//                }
//                updateClickerUpis()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>){
//                updateGrupe(odabirGrupe)
//            }
//        }
//
//        odabirGrupe.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
//                updateClickerUpis()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>){
//
//            }
//        }
//
//        upisiMe = findViewById(R.id.dodajPredmetDugme)
//        upisiMe.setOnClickListener {
//            quizListViewModel.addMojKviz(odabirPredmeta.selectedItem.toString(), odabirGrupe.selectedItem.toString())
//            predmetViewModel.addUpisani(odabirGodine.selectedItem.toString().toInt(), odabirPredmeta.selectedItem.toString())
//            MainActivity.godina = odabirGodine.selectedItemPosition
//            this.finish()
//        }
//
//    }
//
//    private fun updatePredmete(spinner1: Spinner): Unit{
//        var predmeti = mutableListOf<String>()
//        predmeti.add("")
//        val predmeti1 = predmetViewModel.getFromYear(odabirGodine.selectedItem.toString().toInt())
//
//        for(predmetic in predmeti1)
//            predmeti.add(predmetic)
//
//        val adapter = ArrayAdapter(
//            this,
//            android.R.layout.simple_spinner_item,
//            predmeti
//        )
//
//        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
//
//        spinner1.adapter = adapter
//    }
//
//    private fun updateGrupe(spinner2: Spinner): Unit{
//        if((odabirPredmeta.selectedItem == null)){
//            val grupe = emptyArray<String>()
//            val adapter1 = ArrayAdapter(
//                this, // Context
//                android.R.layout.simple_spinner_item, // Layout
//                grupe // Array
//            )
//
//            adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
//
//            spinner2.adapter = adapter1
//        }
//        else {
//            var grupe = mutableListOf<String>()
//            grupe.add("")
//            var grupe1 = grupaViewModel.getGroupsByPredmetString(odabirPredmeta.selectedItem.toString())
//
//            for(grupica in grupe1)
//                grupe.add(grupica)
//
//            val adapter1 = ArrayAdapter(
//                this, // Context
//                android.R.layout.simple_spinner_item, // Layout
//                grupe // Array
//            )
//
//            adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
//
//            spinner2.adapter = adapter1
//        }
//    }
//
//    private fun updateClickerUpis(){
//        upisiMe.isClickable = odabirGodine.selectedItem.toString() != ""
//                && odabirPredmeta.selectedItem.toString() != ""
//                && odabirGrupe.selectedItem.toString() != ""
//    }
}
