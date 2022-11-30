package com.polar.industries.teskotlin.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.polar.industries.teskotlin.ChatActivity
import com.polar.industries.teskotlin.R
import com.polar.industries.teskotlin.models.User

class AdapterTalacheros(private val context: Context, private val listTalacheros: ArrayList<User>, private val activity: Activity): RecyclerView.Adapter<AdapterTalacheros.HolderTalachero>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderTalachero {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.card_categoria, parent, false)
        return HolderTalachero(itemView)
    }

    override fun onBindViewHolder(holder: HolderTalachero, position: Int) {
        val talacheroActual: User = listTalacheros[position]

        holder.textViewNombreCardCategoria.text = "${talacheroActual.nombre} ${talacheroActual.apellidos}"
        holder.textViewTelefonoCardCategoria.text = "${talacheroActual.telefono}"

        holder.cardViewTalachero.setOnClickListener {
            moveToChat(position)
        }
    }

    private fun moveToChat(position: Int) {
        val intent: Intent = Intent(context, ChatActivity::class.java).apply {
            putExtra("id", listTalacheros[position].id)
            putExtra("nombre", listTalacheros[position].nombre)
            putExtra("apellidos", listTalacheros[position].apellidos)
            putExtra("uri_image", listTalacheros[position].uriImage)
        }
        activity.startActivity(intent)
    }

    override fun getItemCount(): Int {
       return listTalacheros.size
    }


    inner class HolderTalachero(view: View): RecyclerView.ViewHolder(view){
        val cardViewTalachero: CardView = view.findViewById(R.id.cardViewTalachero)
        val imageViewTalachero: ImageView = view.findViewById(R.id.imageViewTalachero)
        val textViewNombreCardCategoria: TextView = view.findViewById(R.id.textViewNombreCardCategoria)
        val textViewTelefonoCardCategoria: TextView = view.findViewById(R.id.textViewTelefonoCardCategoria)
    }
}