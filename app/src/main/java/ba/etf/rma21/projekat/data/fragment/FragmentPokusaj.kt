package ba.etf.rma21.projekat.data.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Kviz

class FragmentPokusaj : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_pokusaj, container, false)

        return view
    }
    companion object {
        fun newInstance(kviz:Kviz): FragmentPokusaj = FragmentPokusaj().apply {
            arguments = Bundle().apply {
                // ovdje cemo raditi sa kvizom
            }
        }
    }
}
