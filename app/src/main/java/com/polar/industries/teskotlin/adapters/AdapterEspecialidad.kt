package com.polar.industries.teskotlin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.polar.industries.teskotlin.R
import com.polar.industries.teskotlin.models.Especialidad

class AdapterEspecialidad(private val context: Context, private val especialidadList: ArrayList<Especialidad>): RecyclerView.Adapter<AdapterEspecialidad.EspecialidaHolder>() {

    inner class EspecialidaHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardEspecialidad: CardView = itemView.findViewById(R.id.cardEspecialidad)
        val textViewCardEspecialidad: TextView = itemView.findViewById(R.id.textViewCardEspecialidad)
        val imageViewEspecialidadIcon: ImageView = itemView.findViewById(R.id.imageViewEspecialidadIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EspecialidaHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.card_especialidades, parent, false)
        return EspecialidaHolder(itemView)
    }

    override fun onBindViewHolder(holder: EspecialidaHolder, position: Int) {
        val especialidadActual: Especialidad = especialidadList[position]
        holder.textViewCardEspecialidad.text = especialidadActual.nombreEspecialidad

        if(especialidadActual.nombreEspecialidad!!.equals("Albañilería")){
            val imageResource = context.resources.getIdentifier("drawable/albanil", null, context.packageName)
            holder.imageViewEspecialidadIcon.setImageDrawable(ContextCompat.getDrawable(context, imageResource))
        } else if(especialidadActual.nombreEspecialidad!!.equals("Carpintería")){
            val imageResource = context.resources.getIdentifier("drawable/carpinteria", null, context.packageName)
            holder.imageViewEspecialidadIcon.setImageDrawable(ContextCompat.getDrawable(context, imageResource))
        } else if(especialidadActual.nombreEspecialidad!!.equals("Cerrajería")){
            val imageResource = context.resources.getIdentifier("drawable/cerrajeria", null, context.packageName)
            holder.imageViewEspecialidadIcon.setImageDrawable(ContextCompat.getDrawable(context, imageResource))
        } else if(especialidadActual.nombreEspecialidad!!.equals("Electrisista")){
            val imageResource = context.resources.getIdentifier("drawable/electrisista", null, context.packageName)
            holder.imageViewEspecialidadIcon.setImageDrawable(ContextCompat.getDrawable(context, imageResource))
        } else if(especialidadActual.nombreEspecialidad!!.equals("Fontanería")){
            val imageResource = context.resources.getIdentifier("drawable/plomerias", null, context.packageName)
            holder.imageViewEspecialidadIcon.setImageDrawable(ContextCompat.getDrawable(context, imageResource))
        } else if(especialidadActual.nombreEspecialidad!!.equals("Herrería")){
            val imageResource = context.resources.getIdentifier("drawable/herreria", null, context.packageName)
            holder.imageViewEspecialidadIcon.setImageDrawable(ContextCompat.getDrawable(context, imageResource))
        } else if(especialidadActual.nombreEspecialidad!!.equals("Mécanica Automotriz")){
            val imageResource = context.resources.getIdentifier("drawable/mecanico", null, context.packageName)
            holder.imageViewEspecialidadIcon.setImageDrawable(ContextCompat.getDrawable(context, imageResource))
        } else if(especialidadActual.nombreEspecialidad!!.equals("Pintor")){
            val imageResource = context.resources.getIdentifier("drawable/pintor", null, context.packageName)
            holder.imageViewEspecialidadIcon.setImageDrawable(ContextCompat.getDrawable(context, imageResource))
        }else if(especialidadActual.nombreEspecialidad!!.equals("Servicio de Mudanzas")){
            val imageResource = context.resources.getIdentifier("drawable/mudanza", null, context.packageName)
            holder.imageViewEspecialidadIcon.setImageDrawable(ContextCompat.getDrawable(context, imageResource))
        }
    }

    override fun getItemCount(): Int {
        return especialidadList.size
    }
}