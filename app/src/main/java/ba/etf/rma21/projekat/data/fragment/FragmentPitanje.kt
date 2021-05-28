package ba.etf.rma21.projekat.data.fragment


import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.size

import androidx.fragment.app.Fragment


import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.BojaOdgovoraOnClick
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.viewmodel.KvizListViewModel
import ba.etf.rma21.projekat.data.viewmodel.PitanjeKvizViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FragmentPitanje(private val pitanje: Pitanje): Fragment() {

    private lateinit var tekstPitanja: TextView
    private lateinit var odgovori: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private var pitanjeKvizViewModel = PitanjeKvizViewModel()
    private var kvizViewModel = KvizListViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_pitanje, container, false)
        tekstPitanja = view.findViewById(R.id.tekstPitanja)
        odgovori = view.findViewById(R.id.odgovoriLista)
        tekstPitanja.setText(pitanje.tekstPitanja)
        adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, pitanje.opcije)
        odgovori.adapter = adapter
        odgovori.onItemClickListener =
            BojaOdgovoraOnClick(pitanje)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // KONAAAACNO HEHEHEHEHEHHE
        Handler().postDelayed({
            //val odgovor = pitanjeKvizViewModel.getOdgovor()
            while(KvizRepository.radjeniKviz == null);
                KvizRepository.radjeniKviz?.id?.let {
                    pitanjeKvizViewModel.getOdgovorApp(
                        it,
                        pitanje.id,
                        onSuccess = ::onSuccess,
                        onError = ::onError
                    )
                }
            // ovo sam uveo jer sam primijetio da je bio bug kada se pritisne back dugme
//            if(odgovori.getChildAt(0) != null) {
//                if (odgovor != -1) {
//                    // ne moram kliktati mogu direktno bojiti
//                    val odgovorTacan = odgovori?.getChildAt(pitanje.tacan) as TextView
//                    var odgovorPogresan = odgovori?.getChildAt(odgovor) as TextView
//                    odgovorTacan.setTextColor(Color.parseColor("#000000"))
//                    odgovori.getChildAt(pitanje.tacan).setBackgroundColor(Color.parseColor("#3DDC84"))
//                    if (pitanje.tacan != odgovor){
//                        odgovorPogresan.setTextColor(Color.parseColor("#000000"))
//                        odgovori.getChildAt(odgovor).setBackgroundColor(Color.parseColor("#DB4F3D"))
//                    }
//                    for (odabir in odgovori.children) {
//                        odabir.isEnabled = false
//                        odabir.setOnClickListener(null)
//                    }
//                } // OVO ZNACI DA NIJE ODGOVORIO
//                else if (pitanjeKvizViewModel.getZavrsenKviz(pitanjeKvizViewModel.getUradjeniKviz(), pitanjeKvizViewModel.getUradjeniPredmet())
//                        || kvizViewModel.getStatus(KvizRepository.pokrenutiKviz) == "crvena") {
//                    // moram pokazati i tacan odgovor
//                    val odgovorTacan = odgovori?.getChildAt(pitanje.tacan) as TextView
//                    odgovorTacan.setTextColor(Color.parseColor("#000000"))
//                    odgovori.getChildAt(pitanje.tacan).setBackgroundColor(Color.parseColor("#3DDC84"))
//                    for (odabir in odgovori.children) {
//                        odabir.isEnabled = false
//                        odabir.setOnClickListener(null)
//                    }
//                }
//            }
        }, 1)

    }

    fun onSuccess(odgovor: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                if (odgovori.getChildAt(0) != null) {
                    if (odgovor != -1) {
                        // ne moram kliktati mogu direktno bojiti
                        val odgovorTacan = odgovori?.getChildAt(pitanje.tacan) as TextView
                        lateinit var odgovorPogresan: TextView
                        if(odgovor != odgovori.size)
                            odgovorPogresan = odgovori?.getChildAt(odgovor) as TextView
                        odgovorTacan.setTextColor(Color.parseColor("#000000"))
                        odgovori.getChildAt(pitanje.tacan)
                            .setBackgroundColor(Color.parseColor("#3DDC84"))
                        if (pitanje.tacan != odgovor && odgovor != odgovori.size) {
                                odgovorPogresan.setTextColor(Color.parseColor("#000000"))
                            odgovori.getChildAt(odgovor)
                                .setBackgroundColor(Color.parseColor("#DB4F3D"))
                        }
                        for (odabir in odgovori.children) {
                            odabir.isEnabled = false
                            odabir.setOnClickListener(null)
                        }
                    }
                }
            }
        }
    }

    fun onError() {
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                val toast = Toast.makeText(context, "Neki error", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
}
