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

    private var prviPutPredmet = true
    private var prviPutGrupa = true

    private lateinit var view1: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view1 = inflater.inflate(R.layout.fragment_upis, container, false)
        odabirGodine = view1.findViewById(R.id.odabirGodina)
        odabirPredmeta = view1.findViewById(R.id.odabirPredmet)
        odabirGrupe = view1.findViewById(R.id.odabirGrupa)

        ArrayAdapter.createFromResource(
            view1.context,
            R.array.izbor_za_godine,
            android.R.layout.simple_spinner_item
        ).also { adapter ->

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            odabirGodine.adapter = adapter
        }

        odabirGodine.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                if(odabirGodine.selectedItem.toString() != "") {
                    odabirPredmeta.isEnabled = true
                    updatePredmete(odabirPredmeta)
                    if(!prviPutGrupa) {
                        updateGrupe(odabirGrupe)
                        FragmentKvizovi.odabranaGrupa = -1
                    }
                    FragmentKvizovi.odabranaGodina = odabirGodine.selectedItemPosition

                    FragmentKvizovi.odabraniPredmet = -1

                }
                else {
                    odabirPredmeta.isEnabled = false
                    odabirGrupe.isEnabled = false
                }
                updateClickerUpis()
            }

            override fun onNothingSelected(parent: AdapterView<*>){
            }
        }

        if(FragmentKvizovi.odabranaGodina != -1) odabirGodine.setSelection(FragmentKvizovi.odabranaGodina)

        odabirPredmeta.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                if(odabirPredmeta.selectedItem.toString() != "") {
                    odabirGrupe.isEnabled = true
                    updateGrupe(odabirGrupe)
                    FragmentKvizovi.odabraniPredmet = odabirPredmeta.selectedItemPosition

                    FragmentKvizovi.odabranaGrupa = -1
                }
                else {
                    odabirGrupe.isEnabled = false
                }
                updateClickerUpis()
            }

            override fun onNothingSelected(parent: AdapterView<*>){

            }
        }

        odabirGrupe.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                updateClickerUpis()
                FragmentKvizovi.odabranaGrupa = odabirGrupe.selectedItemPosition
            }

            override fun onNothingSelected(parent: AdapterView<*>){

            }
        }

        upisiMe = view1.findViewById(R.id.dodajPredmetDugme)
        upisiMe.setOnClickListener {
            quizListViewModel.addMojKviz(odabirPredmeta.selectedItem.toString(), odabirGrupe.selectedItem.toString())
            predmetViewModel.addUpisani(odabirGodine.selectedItem.toString().toInt(), odabirPredmeta.selectedItem.toString())
            FragmentKvizovi.odabranaGodina = -1

            FragmentKvizovi.odabraniPredmet = -1

            FragmentKvizovi.odabranaGrupa = -1
        }

        return view1
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
            view1.context,
            android.R.layout.simple_spinner_item,
            predmeti
        )

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        spinner1.adapter = adapter
        if(prviPutPredmet && FragmentKvizovi.odabraniPredmet != -1){
            odabirPredmeta.setSelection(FragmentKvizovi.odabraniPredmet)
            prviPutPredmet = false
        }
    }

    private fun updateGrupe(spinner2: Spinner): Unit{
        if ((odabirPredmeta.selectedItem == null)) {
            val grupe = emptyArray<String>()
            val adapter1 = ArrayAdapter(
                view1.context, // Context
                android.R.layout.simple_spinner_item, // Layout
                grupe // Array
            )

            adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

            spinner2.adapter = adapter1
        } else {
            var grupe = mutableListOf<String>()
            grupe.add("")
            var grupe1 =
                grupaViewModel.getGroupsByPredmetString(odabirPredmeta.selectedItem.toString())

            for (grupica in grupe1)
                grupe.add(grupica)

            val adapter1 = ArrayAdapter(
                view1.context, // Context
                android.R.layout.simple_spinner_item, // Layout
                grupe // Array
            )

            adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

            spinner2.adapter = adapter1
        }
        if (prviPutGrupa && FragmentKvizovi.odabranaGrupa != -1) {
            odabirGrupe.setSelection(FragmentKvizovi.odabranaGrupa)
            prviPutGrupa = false
        }
    }

    private fun updateClickerUpis(){
        if(odabirGodine.selectedItem != null && odabirPredmeta.selectedItem != null && odabirGrupe.selectedItem != null){
        upisiMe.isClickable = odabirGodine.selectedItem.toString() != ""
                && odabirPredmeta.selectedItem.toString() != ""
                && odabirGrupe.selectedItem.toString() != ""
        }
        else upisiMe.isClickable = false
    }


}