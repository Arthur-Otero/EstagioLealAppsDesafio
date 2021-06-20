package com.example.lealappsdesafio.treino

import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.lealappsdesafio.R
import com.example.lealappsdesafio.exercicio.ExerciseActivity
import com.example.lealappsdesafio.model.Data
import com.example.lealappsdesafio.model.Exercise
import com.example.lealappsdesafio.model.Training
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.util.*


class TrainingAdapter(val callback : (Int, Char)->Unit) : RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder>() {
    private var trainings = mutableListOf<Training>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.training_card, parent, false)
        return TrainingViewHolder(view)
    }

    override fun getItemCount(): Int = trainings.size

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        holder.name.text = trainings[position].name.toString()
        holder.description.text = trainings[position].description

        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale("pt-BR"))
        val dateString: String = formatter.format(Date(trainings[position].date!!.seconds * 1000L))
        formatter.timeZone = TimeZone.getDefault()

        holder.date.text = dateString

        holder.menu.setOnClickListener {
            showMenu(it, R.menu.menu_training_card,position)
        }
        holder.itemView.setOnClickListener {
            Data.trainings = trainings
            Data.position = position
            val intent = Intent(it.context,ExerciseActivity::class.java)
            it.context.startActivity(intent)
        }
    }

    private fun showMenu(view: View, @MenuRes menuRes: Int, position: Int) {
        val popup = PopupMenu(view.context!!, view)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            when(it.itemId){//delete
                R.id.edit->{
                    callback(position,'e')
                }
                R.id.delete->{
                    callback(position,'d')
                }
            }
            false
        }

        try {
            val fields = popup.javaClass.declaredFields
            for (field in fields) {
                if ("mPopup" == field.name) {
                    field.isAccessible = true
                    val menuPopupHelper = field[popup]
                    val classPopupHelper =
                        Class.forName(menuPopupHelper.javaClass.name)
                    val setForceIcons: Method = classPopupHelper.getMethod(
                        "setForceShowIcon",
                        Boolean::class.javaPrimitiveType
                    )
                    setForceIcons.invoke(menuPopupHelper, true)
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        popup.show()
    }

    inner class TrainingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val menu by lazy { view.findViewById<Button>(R.id.menuCard) }
        val name by lazy { view.findViewById<TextView>(R.id.nameCard) }
        val date by lazy { view.findViewById<TextView>(R.id.dateCard) }
        val description by lazy { view.findViewById<TextView>(R.id.descriptionCard) }
    }

    fun addTraining(training: List<Training>){
        trainings.addAll(training)
        notifyDataSetChanged()
    }
}