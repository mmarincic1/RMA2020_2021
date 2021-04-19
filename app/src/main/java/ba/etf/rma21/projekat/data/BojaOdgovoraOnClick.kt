package ba.etf.rma21.projekat.data

import android.R
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.view.children
import ba.etf.rma21.projekat.data.fragment.FragmentKvizovi
import ba.etf.rma21.projekat.data.fragment.FragmentPokusaj
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.viewmodel.PitanjeKvizViewModel


class BojaOdgovoraOnClick(val pitanje: Pitanje): AdapterView.OnItemClickListener {

    private var pitanjeKvizViewModel = PitanjeKvizViewModel()
    private val menu: Menu = FragmentPokusaj.navigationView.getMenu()
    private var rezultat: Double = 0.toDouble()
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(position == pitanje.tacan){
            if(FragmentPokusaj.odgovor == -1)
                rezultat =  ((1.toDouble()/FragmentPokusaj.brojPitanja.toDouble())*100)
            pitanjeKvizViewModel.dodajRezultat(FragmentKvizovi.uradjeniKviz, FragmentKvizovi.uradjeniPredmet, rezultat)
            val odgovorTacan = parent?.getChildAt(position) as TextView
            odgovorTacan.setTextColor(Color.parseColor("#3DDC84"))
            if(FragmentPokusaj.indexPitanja != "") {
                for (i in 0 until menu.size()) {
                    val menuItem: MenuItem = menu.getItem(i)
                    if (menuItem.getTitle().equals(FragmentPokusaj.indexPitanja)) {
                        val spanString =
                            SpannableString(menuItem.getTitle().toString())
                        spanString.setSpan(
                            ForegroundColorSpan(Color.parseColor("#3DDC84")),
                            0,
                            spanString.length,
                            0
                        )
                        menuItem.setTitle(spanString)
                    }
                }
            }
        }else{
            if(FragmentPokusaj.odgovor == -1)
            pitanjeKvizViewModel.dodajRezultat(FragmentKvizovi.uradjeniKviz, FragmentKvizovi.uradjeniPredmet, 0.toDouble())
            val odgovorTacan = parent?.getChildAt(pitanje.tacan) as TextView
            odgovorTacan.setTextColor(Color.parseColor("#3DDC84"))
            val odgovorPogresan = parent?.getChildAt(position) as TextView
            odgovorPogresan.setTextColor(Color.parseColor("#DB4F3D"))
            if(FragmentPokusaj.indexPitanja != "") {
                for (i in 0 until menu.size()) {
                    val menuItem: MenuItem = menu.getItem(i)
                    if (menuItem.getTitle().equals(FragmentPokusaj.indexPitanja)) {
                        val spanString =
                            SpannableString(menuItem.getTitle().toString())
                        spanString.setSpan(
                            ForegroundColorSpan(Color.parseColor("#DB4F3D")),
                            0,
                            spanString.length,
                            0
                        )
                        menuItem.setTitle(spanString)
                    }
                }
            }
        }

        for(odabir in parent.children){
            odabir.isEnabled = false
            odabir.setOnClickListener(null)
        }
        // evidentiraj odgovor
        if(FragmentPokusaj.odgovor == -1)
            pitanjeKvizViewModel.odgovoriNaPitanje(position, FragmentKvizovi.uradjeniKviz, FragmentKvizovi.uradjeniPredmet, pitanje.naziv)
        //
    }
}