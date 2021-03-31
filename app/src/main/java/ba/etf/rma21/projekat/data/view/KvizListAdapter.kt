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
import java.time.format.DateTimeFormatter

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

        holder.quizName.text = quizzes[position].naziv
        holder.quizDate.text = quizzes[position].datumRada.getDate().toString()
        holder.quizPoints.text = quizzes[position].osvojeniBodovi.toString()
        holder.quizSubjectName.text = quizzes[position].nazivPredmeta
        holder.quizTime.text = quizzes[position].trajanje.toString() + "min"
        val context: Context = holder.quizStatus.getContext()
        var id: Int = context.getResources()
            .getIdentifier("plava", "drawable", context.getPackageName())
        holder.quizStatus.setImageResource(id)
    }

    fun updateMovies(quizzes: List<Kviz>) {
        this.quizzes = quizzes
        notifyDataSetChanged()
    }

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quizName: TextView = itemView.findViewById(R.id.naziv)
        val quizPoints: TextView = itemView.findViewById(R.id.osvojeniBodovi)
        val quizDate: TextView = itemView.findViewById(R.id.datumRada)
        val quizSubjectName: TextView = itemView.findViewById(R.id.nazivPredmeta)
        val quizTime: TextView = itemView.findViewById(R.id.trajanje)
        val quizStatus: ImageView = itemView.findViewById(R.id.status)
    }
}