package ba.etf.rma21.projekat.data.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment

import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.viewmodel.GroupViewModel
import ba.etf.rma21.projekat.data.viewmodel.KvizListViewModel
import ba.etf.rma21.projekat.data.viewmodel.PredmetViewModel

class FragmentPredmeti : Fragment() {
    private lateinit var odabirGodine: Spinner
    private lateinit var odabirPredmeta: Spinner
    private lateinit var odabirGrupe: Spinner
    private lateinit var upisiMe: Button
    private var quizListViewModel = KvizListViewModel()
    private var predmetViewModel = PredmetViewModel()
    private var grupaViewModel = GroupViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_upis, container, false)

        odabirGodine = view.findViewById(R.id.odabirGodina)
        odabirPredmeta = view.findViewById(R.id.odabirPredmet)
        odabirGrupe = view.findViewById(R.id.odabirGrupa)

        ArrayAdapter.createFromResource(
            view.context,
            R.array.izbor_za_godine,
            android.R.layout.simple_spinner_item
        ).also { adapter ->

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            odabirGodine.adapter = adapter
        }

        odabirGodine.setSelection(FragmentKvizovi.godina)

        odabirGodine.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                if(odabirGodine.selectedItem.toString() != "") {
                    odabirPredmeta.isEnabled = true
                    updatePredmete(odabirPredmeta)
                    updateGrupe(odabirGrupe)
                }
                else {
                    odabirPredmeta.isEnabled = false
                    odabirGrupe.isEnabled = false
                }
                updateClickerUpis()
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                updatePredmete(odabirPredmeta)
                updateGrupe(odabirGrupe)
            }
        }

        odabirPredmeta.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                if(odabirPredmeta.selectedItem.toString() != "") {
                    odabirGrupe.isEnabled = true
                    updateGrupe(odabirGrupe)
                }
                else {
                    odabirGrupe.isEnabled = false
                }
                updateClickerUpis()
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                updateGrupe(odabirGrupe)
            }
        }

        odabirGrupe.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                updateClickerUpis()
            }

            override fun onNothingSelected(parent: AdapterView<*>){

            }
        }

        upisiMe = view.findViewById(R.id.dodajPredmetDugme)
        upisiMe.setOnClickListener {
            quizListViewModel.addMojKviz(odabirPredmeta.selectedItem.toString(), odabirGrupe.selectedItem.toString())
            predmetViewModel.addUpisani(odabirGodine.selectedItem.toString().toInt(), odabirPredmeta.selectedItem.toString())
            FragmentKvizovi.godina = odabirGodine.selectedItemPosition
        }

        return view
    }
    companion object {
        fun newInstance(): FragmentPredmeti = FragmentPredmeti()
    }
    private fun updatePredmete(spinner1: Spinner): Unit{
        var predmeti = mutableListOf<String>()
        predmeti.add("")
        val predmeti1 = predmetViewModel.getFromYear(odabirGodine.selectedItem.toString().toInt())

        for(predmetic in predmeti1)
            predmeti.add(predmetic)

        val adapter = ArrayAdapter(
            view?.context!!,
            android.R.layout.simple_spinner_item,
            predmeti
        )

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        spinner1.adapter = adapter
    }

    private fun updateGrupe(spinner2: Spinner): Unit{
        if((odabirPredmeta.selectedItem == null)){
            val grupe = emptyArray<String>()
            val adapter1 = ArrayAdapter(
                view?.context!!, // Context
                android.R.layout.simple_spinner_item, // Layout
                grupe // Array
            )

            adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

            spinner2.adapter = adapter1
        }
        else {
            var grupe = mutableListOf<String>()
            grupe.add("")
            var grupe1 = grupaViewModel.getGroupsByPredmetString(odabirPredmeta.selectedItem.toString())

            for(grupica in grupe1)
                grupe.add(grupica)

            val adapter1 = ArrayAdapter(
                view?.context!!, // Context
                android.R.layout.simple_spinner_item, // Layout
                grupe // Array
            )

            adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

            spinner2.adapter = adapter1
        }
    }

    private fun updateClickerUpis(){
        upisiMe.isClickable = odabirGodine.selectedItem.toString() != ""
                && odabirPredmeta.selectedItem.toString() != ""
                && odabirGrupe.selectedItem.toString() != ""
    }
}