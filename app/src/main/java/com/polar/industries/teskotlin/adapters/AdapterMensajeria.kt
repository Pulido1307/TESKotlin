package com.polar.industries.teskotlin.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
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

class AdapterMensajeria(private val context: Context, private val listaMensajes: ArrayList<User>, private val activity: Activity ): RecyclerView.Adapter<AdapterMensajeria.HolderMensajeria>() {
    inner class HolderMensajeria(view: View): RecyclerView.ViewHolder(view){
        val imageViewUserMensajeria: ImageView = view.findViewById(R.id.imageViewUserMensajeria)
        val textViewUsuarioMensajeria: TextView = view.findViewById(R.id.textViewUsuarioMensajeria)
        val cardViewContactoMensajeria: CardView = view.findViewById(R.id.cardViewContactoMensajeria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMensajeria {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.card_mensajeria, parent, false)
        return HolderMensajeria(itemView)
    }

    override fun onBindViewHolder(holder: HolderMensajeria, position: Int) {
        val mensajeActual: User = listaMensajes[position]

        holder.textViewUsuarioMensajeria.text = "${mensajeActual.nombre} ${mensajeActual.apellidos}"
        holder.cardViewContactoMensajeria.setOnClickListener {
            moveToChat(position)
        }

    }

    private fun moveToChat(position: Int) {
        val intent: Intent = Intent(context, ChatActivity::class.java).apply {
            putExtra("id", listaMensajes[position].id)
            putExtra("nombre", listaMensajes[position].nombre)
            putExtra("apellidos", listaMensajes[position].apellidos)
            putExtra("uri_image", listaMensajes[position].uriImage)
        }
        activity.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return listaMensajes.size
    }
}