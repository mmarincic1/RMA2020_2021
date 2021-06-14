package ba.etf.rma21.projekat.data

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import ba.etf.rma21.projekat.data.fragment.FragmentPokusaj
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.AccountRepository
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.repositories.OdgovorRepository
import ba.etf.rma21.projekat.data.viewmodel.KvizListViewModel
import ba.etf.rma21.projekat.data.viewmodel.PitanjeKvizViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class BojaOdgovoraOnClick(val pitanje: Pitanje): AdapterView.OnItemClickListener {

    private var pitanjeKvizViewModel = PitanjeKvizViewModel()
    private val menu: Menu = FragmentPokusaj.navigationView.getMenu()
    private val indexPitanja = pitanjeKvizViewModel.getIndexPitanja()
    private val kvizListViewModel = KvizListViewModel()


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(position == pitanje.tacan){
            val odgovorTacan = parent?.getChildAt(position) as TextView
            odgovorTacan.setTextColor(Color.parseColor("#000000"))
            parent.getChildAt(position).setBackgroundColor(Color.parseColor("#3DDC84"))
            if(indexPitanja != "") {
                for (i in 0 until menu.size()) {
                    val menuItem: MenuItem = menu.getItem(i)
                    if (menuItem.getTitle().equals(indexPitanja)) {
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
            val odgovorTacan = parent?.getChildAt(pitanje.tacan) as TextView
            odgovorTacan.setTextColor(Color.parseColor("#000000"))
            parent.getChildAt(pitanje.tacan).setBackgroundColor(Color.parseColor("#3DDC84"))
            val odgovorPogresan = parent?.getChildAt(position) as TextView
            odgovorPogresan.setTextColor(Color.parseColor("#000000"))
            parent.getChildAt(position).setBackgroundColor(Color.parseColor("#DB4F3D"))
            if(indexPitanja != "") {
                for (i in 0 until menu.size()) {
                    val menuItem: MenuItem = menu.getItem(i)
                    if (menuItem.getTitle().equals(indexPitanja)) {
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
        KvizRepository.radjeniKviz?.id?.let { pitanjeKvizViewModel.postaviOdgovorKviz(it, pitanje.id, position, onSuccess = ::onSuccess, onError = ::onError) }
    }

    fun onSuccess(rez: Int) {
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                val rezic = rez + 100
                KvizRepository.radjeniKviz?.let { pitanjeKvizViewModel.getZavrsenKvizZaSveOdgovorene(it, rez ,onSuccess = ::onSuccess1, onError = ::onError) }
            }
        }
    }

    fun onSuccess1(rez: Boolean, rezultat: Int) {
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                if(rez){
                    KvizRepository.radjeniKviz?.let { kvizListViewModel.popuniApiZaZavrsenKviz(it, onSuccess = ::onSuccess2, onError = ::onError) }
                }
            }
        }
    }


    fun onSuccess2() {
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                val toast = Toast.makeText(AccountRepository.getContext(), "Kviz zavrsen", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    fun onError() {
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                val toast = Toast.makeText(AccountRepository.getContext(), "Neki error", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }


}