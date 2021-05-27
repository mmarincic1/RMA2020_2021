package ba.etf.rma21.projekat.data.fragment

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.viewmodel.GroupViewModel
import ba.etf.rma21.projekat.data.viewmodel.KvizListViewModel
import ba.etf.rma21.projekat.data.viewmodel.PitanjeKvizViewModel
import ba.etf.rma21.projekat.data.viewmodel.PredmetViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.acl.Group

// PROVJERI TREBA LI SE OPET PAMTITI STA JE PRITISNUO !!!
class FragmentPredmeti: Fragment() {
    private lateinit var odabirGodine: Spinner
    private lateinit var odabirPredmeta: Spinner
    private lateinit var odabirGrupe: Spinner
    private lateinit var upisiMe: Button
    private var predmetViewModel = PredmetViewModel()
    private var grupaViewModel = GroupViewModel()
    private var pitanjeKvizListViewModel = PitanjeKvizViewModel()
    private var groupViewModel = GroupViewModel()
    private var quizListViewModel = KvizListViewModel()

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
                    updatePredmete()
                    if(!prviPutGrupa) {
                        updateGrupe()
                        pitanjeKvizListViewModel.setOdabranaGrupa(-1)
                    }
                    pitanjeKvizListViewModel.setOdabranaGodina(odabirGodine.selectedItemPosition)

                    pitanjeKvizListViewModel.setOdabraniPredmet(-1)

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

        if(pitanjeKvizListViewModel.getOdabranaGodina() != -1)
            odabirGodine.setSelection(pitanjeKvizListViewModel.getOdabranaGodina())

        odabirPredmeta.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                if(odabirPredmeta.selectedItem.toString() != "") {
                    odabirGrupe.isEnabled = true
                    updateGrupe()
                    pitanjeKvizListViewModel.setOdabraniPredmet(odabirPredmeta.selectedItemPosition)

                    pitanjeKvizListViewModel.setOdabranaGrupa(-1)
                }
                else {
                    updateGrupe()
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
                pitanjeKvizListViewModel.setOdabranaGrupa(odabirGrupe.selectedItemPosition)
            }

            override fun onNothingSelected(parent: AdapterView<*>){

            }
        }

        upisiMe = view1.findViewById(R.id.dodajPredmetDugme)
        upisiMe.setOnClickListener {

            grupaViewModel.upisUGrupu((odabirGrupe.selectedItem as Grupa).id, onSuccess = ::onSuccess2, onError = ::onError)

        }

        return view1
    }

    companion object {
        fun newInstance(): FragmentPredmeti = FragmentPredmeti()
    }

    private fun updatePredmete(): Unit{
        predmetViewModel.getPredmetsByGodina(odabirGodine.selectedItem.toString().toInt(), onSuccess = ::onSuccess, onError = ::onError)
    }

    private fun updateGrupe(){
        if ((odabirPredmeta.selectedItem == null) || (odabirPredmeta.selectedItem as Predmet).naziv == "") {
            val grupe = emptyArray<String>()
            val adapter1 = ArrayAdapter(
                view1.context, // Context
                android.R.layout.simple_spinner_item, // Layout
                grupe // Array
            )

            adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

            odabirGrupe.adapter = adapter1
        } else{
            grupaViewModel.getGroupsByPredmet(odabirPredmeta.selectedItem as Predmet, onSuccess = ::onSuccess1, onError = ::onError)
        }
    }

    private fun updateClickerUpis(){
        if(odabirGodine.selectedItem != null && odabirPredmeta.selectedItem != null && odabirGrupe.selectedItem != null
            && (odabirPredmeta.selectedItem as Predmet).naziv != "" && (odabirGrupe.selectedItem as Grupa).naziv != ""){
            upisiMe.isClickable = odabirGodine.selectedItem.toString() != ""
                    && odabirPredmeta.selectedItem.toString() != ""
                    && odabirGrupe.selectedItem.toString() != ""
        }
        else upisiMe.isClickable = false
    }

    private fun openFragment(fragment: Fragment) {
        var fr = getFragmentManager()?.beginTransaction()
        fr?.replace(R.id.container, fragment)
        fr?.commit()
    }

    fun onSuccess(predmeti1:List<Predmet>){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                val adapter = ArrayAdapter(
                    view1.context,
                    android.R.layout.simple_spinner_item,
                    predmeti1
                )

                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

                odabirPredmeta.adapter = adapter
                if(prviPutPredmet && pitanjeKvizListViewModel.getOdabraniPredmet() != -1){
                    odabirPredmeta.setSelection(pitanjeKvizListViewModel.getOdabraniPredmet())
                    prviPutPredmet = false
                }
            }
        }
    }

    fun onSuccess1(grupe: List<Grupa>) {
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                val adapter1 = ArrayAdapter(
                    view1.context, // Context
                    android.R.layout.simple_spinner_item, // Layout
                    grupe // Array
                )

                adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

                odabirGrupe.adapter = adapter1
            }
            if (prviPutGrupa && pitanjeKvizListViewModel.getOdabranaGrupa() != -1) {
                odabirGrupe.setSelection(pitanjeKvizListViewModel.getOdabranaGrupa())
                prviPutGrupa = false
            }
        }
    }

    fun onSuccess2(upisan: Boolean){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                if(upisan){
                    pitanjeKvizListViewModel.setOdabranaGodina(-1)

                    pitanjeKvizListViewModel.setOdabraniPredmet(-1)

                    pitanjeKvizListViewModel.setOdabranaGrupa(-1)
                    // otvaranje novog fragmenta
                    val nazivGrupe1 = odabirGrupe.selectedItem.toString()
                    val nazivPredmeta1 = odabirPredmeta.selectedItem.toString()
                    val porukicaFragment = FragmentPoruka.newInstance( "Uspješno ste upisani u grupu " + nazivGrupe1 + " predmeta " + nazivPredmeta1 + "!" )
                    openFragment(porukicaFragment)
                }else onError()
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
