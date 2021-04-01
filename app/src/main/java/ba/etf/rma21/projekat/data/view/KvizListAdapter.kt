package ba.etf.rma21.projekat.data.view

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Kviz
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.Collator
import java.time.LocalTime
import java.util.*
import java.util.stream.Collector
import java.util.stream.Collectors


class KvizListAdapter(
    private var quizzes: List<Kviz>
): RecyclerView.Adapter<KvizListAdapter.QuizViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuizViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_kviz, parent, false)
        return QuizViewHolder(view)
    }

    override fun getItemCount(): Int = quizzes.size

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        //holder.newAction.setOnClickListener{onItemClicked()}
        holder.quizName.text = quizzes[position].naziv
        // nije radio kviz
        if(quizzes[position].datumRada == null){
            // nije aktivan kviz onda datum pocetka inace datum kraja
            if(uporediSaTrenutnimDatumom(quizzes[position].datumPocetka) == 1)
                holder.quizDate.text = dajDatum(quizzes[position].datumPocetka)
            else holder.quizDate.text = dajDatum(quizzes[position].datumKraj)
        }
        else holder.quizDate.text = quizzes[position].datumRada?.let { dajDatum(it) }
        holder.quizSubjectName.text = quizzes[position].nazivPredmeta
        holder.quizTime.text = quizzes[position].trajanje.toString() + " min"
        val context: Context = holder.quizStatus.getContext()
        val status: String = dajStatus(quizzes[position])
        if(status == "plava") holder.quizPoints.text = quizzes[position].osvojeniBodovi.toString()
        else holder.quizPoints.text = ""
        var id: Int = context.getResources()
            .getIdentifier(status, "drawable", context.getPackageName())
        holder.quizStatus.setImageResource(id)
    }

    private fun dajDatum(datumRada: Date): String{
        var dan: Int = datumRada.getDate()
        var mjesec: Int = datumRada.getMonth()
        var danString: String?
        var mjesecString: String?
        if(dan < 10){
            danString = "0" + dan.toString() + "."
        }
        else danString = dan.toString() + "."
        if(mjesec < 10){
            mjesecString = "0" + mjesec.toString() + "."
        }
        else mjesecString = mjesec.toString() + "."

        return danString + mjesecString + datumRada.getYear().toString()
    }

    private fun uporediSaTrenutnimDatumom(datum1: Date): Int{
        var godina = Calendar.getInstance().get(Calendar.YEAR)
        var mjesec = Calendar.getInstance().get(Calendar.MONTH) + 1
        var dan = Calendar.getInstance().get(Calendar.DATE)
        if(datum1.getYear() > godina) return 1
        else if(godina > datum1.getYear()) return 2;
        else if(datum1.getMonth() > mjesec) return 1;
        else if(mjesec > datum1.getMonth()) return 2;
        else if(datum1.getDate() > dan) return 1;
        else if(dan > datum1.getDate()) return 2;
        return 0;
    }


    private fun dajStatus(kviz: Kviz): String {
        // kviz nije uradjen
        if(kviz.datumRada == null){
            var datumPocetka = uporediSaTrenutnimDatumom(kviz.datumPocetka)
            var datumKraja = uporediSaTrenutnimDatumom(kviz.datumKraj)
            // kviz nije otvoren
            if(datumPocetka == 1){
                return "zuta"
            }
            // kviz aktivan
            else if(datumPocetka == 2 && datumKraja == 1){
                return "zelena"
            }
            // kviz nije uradjen i nije aktivan
            else if(datumPocetka == 2 && datumKraja == 2){
                return "crvena"
            }
        }
        return "plava"
    }


    fun updateQuizes(quizzes: List<Kviz>) {
        this.quizzes = quizzes
        // sortiranje kvizova
        this.quizzes = this.quizzes.stream().sorted { o1, o2 -> uporediDatume(o1, o2)}.collect(
            Collectors.toList())
        //
        notifyDataSetChanged()
    }

    private fun uporediDatume(o1: Kviz, o2: Kviz): Int {
        if(o1.datumPocetka.getYear() > o2.datumPocetka.getYear()) return 1
        else if(o2.datumPocetka.getYear() > o1.datumPocetka.getYear()) return -1;
        else if(o1.datumPocetka.getMonth() > o2.datumPocetka.getMonth()) return 1;
        else if(o2.datumPocetka.getMonth() > o1.datumPocetka.getMonth()) return -1;
        else if(o1.datumPocetka.getDate() > o2.datumPocetka.getDate()) return 1;
        else if(o2.datumPocetka.getDate() > o1.datumPocetka.getDate()) return -1;
        return 0;
    }

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quizName: TextView = itemView.findViewById(R.id.naziv)
        val quizPoints: TextView = itemView.findViewById(R.id.osvojeniBodovi)
        val quizDate: TextView = itemView.findViewById(R.id.datumRada)
        val quizSubjectName: TextView = itemView.findViewById(R.id.nazivPredmeta)
        val quizTime: TextView = itemView.findViewById(R.id.trajanje)
        val quizStatus: ImageView = itemView.findViewById(R.id.status)
        //val newAction: FloatingActionButton = itemView.findViewById(R.id.upisDugme)
    }
}