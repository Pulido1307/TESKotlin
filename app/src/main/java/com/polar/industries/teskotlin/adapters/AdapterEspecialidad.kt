package com.polar.industries.teskotlin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.polar.industries.teskotlin.R
import com.polar.industries.teskotlin.models.Especialidad

class AdapterEspecialidad(private val context: Context, private val especialidadList: ArrayList<Especialidad>): RecyclerView.Adapter<AdapterEspecialidad.EspecialidaHolder>() {

    inner class EspecialidaHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardEspecialidad: CardView = itemView.findViewById(R.id.cardEspecialidad)
        val textViewCardEspecialidad: TextView = itemView.findViewById(R.id.textViewCardEspecialidad)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EspecialidaHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.card_especialidades, parent, false)
        return EspecialidaHolder(itemView)
    }

    override fun onBindViewHolder(holder: EspecialidaHolder, position: Int) {
        val especialidadActual: Especialidad = especialidadList[position]
        holder.textViewCardEspecialidad.text = especialidadActual.nombreEspecialidad
    }

    override fun getItemCount(): Int {
        return especialidadList.size
    }
}