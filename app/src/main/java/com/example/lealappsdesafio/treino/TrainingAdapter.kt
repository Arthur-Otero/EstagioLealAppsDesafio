package com.example.lealappsdesafio.treino

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.lealappsdesafio.R
import com.example.lealappsdesafio.model.Training
import java.lang.reflect.Method

class TrainingAdapter(val trainings: MutableList<Training>) :
    RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.training_card, parent, false)
        return TrainingViewHolder(view)
    }

    override fun getItemCount(): Int = trainings.size

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        holder.menu.setOnClickListener {
            showMenu(it, R.menu.menu_training_card)
        }
    }

    private fun showMenu(view: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(view.context!!, view)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuIntem: MenuItem ->
            return@setOnMenuItemClickListener false
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
    }
}