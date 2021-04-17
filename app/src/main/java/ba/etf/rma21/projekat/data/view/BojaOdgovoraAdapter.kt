package ba.etf.rma21.projekat.data.view

import android.graphics.Color
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.view.children
import ba.etf.rma21.projekat.MainActivity
import ba.etf.rma21.projekat.data.fragment.FragmentPokusaj
import ba.etf.rma21.projekat.data.models.Pitanje


class BojaOdgovoraAdapter(val pitanje: Pitanje): AdapterView.OnItemClickListener {
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(position == pitanje.tacan){
            val odgovorTacan = parent?.getChildAt(position) as TextView
            odgovorTacan.setTextColor(Color.parseColor("#3DDC84"))
            FragmentPokusaj.brojTacnih++
        }else{
            val odgovorTacan = parent?.getChildAt(pitanje.tacan) as TextView
            odgovorTacan.setTextColor(Color.parseColor("#3DDC84"))
            val odgovorPogresan = parent?.getChildAt(position) as TextView
            odgovorPogresan.setTextColor(Color.parseColor("#DB4F3D"))
        }
        for(odabir in parent.children){
            odabir.isEnabled = false
            odabir.setOnClickListener(null)
        }
    }
}