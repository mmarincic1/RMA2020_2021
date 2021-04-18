package ba.etf.rma21.projekat.data.fragment


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.core.view.children

import androidx.fragment.app.Fragment


import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.BojaOdgovoraOnClick
import ba.etf.rma21.projekat.data.viewmodel.PitanjeKvizViewModel


class FragmentPitanje(private val pitanje: Pitanje): Fragment(){

    private lateinit var tekstPitanja: TextView
    private lateinit var odgovori: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private var pitanjeKvizViewModel = PitanjeKvizViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_pitanje, container, false)
        tekstPitanja = view.findViewById(R.id.tekstPitanja)
        odgovori = view.findViewById(R.id.odgovoriLista)
        tekstPitanja.setText(pitanje.tekst)
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
            if(FragmentPokusaj.odgovor != -1){
                odgovori.performItemClick(odgovori, FragmentPokusaj.odgovor, odgovori.adapter.getItemId(FragmentPokusaj.odgovor))
            } // OVO ZNACI DA NIJE ODGOVORIO, DA LI TREBA DA SE PRIKAZE TACAN ODGOVOR???
            else if(pitanjeKvizViewModel.getZavrsenKviz(FragmentKvizovi.uradjeniKviz, FragmentKvizovi.uradjeniPredmet)){
                for(odabir in odgovori.children){
                    odabir.isEnabled = false
                    odabir.setOnClickListener(null)
                }
            }
        }, 1)

    }
}
