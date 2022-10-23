package com.polar.industries.teskotlin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseUser
import com.polar.industries.teskotlin.R
import com.polar.industries.teskotlin.models.Chats
import java.text.SimpleDateFormat
import java.util.*

class AdapterChats(private val chatList: List<Chats>, private val context: Context): RecyclerView.Adapter<AdapterChats.ViewHolderAdapter>() {

    val MENSAJE_RIGHT:Int = 1
    val MENSAJE_LEFT: Int = 0
    var solo_right: Boolean = false
    var firebaseUser: FirebaseUser? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAdapter {
        if(viewType == MENSAJE_RIGHT){
            val view: View = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false)
            return AdapterChats.ViewHolderAdapter(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false)
            return AdapterChats.ViewHolderAdapter(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolderAdapter, position: Int) {
        val chats: Chats = chatList.get(position)

        holder.textView_Message.setText(chats.mensaje)
        if (solo_right) {
            if (chats.visto.equals("SI")) {
                holder.imageView_Entregado.visibility = View.GONE
                holder.imageView_Visto.visibility = View.VISIBLE
            } else {
                holder.imageView_Entregado.visibility = View.VISIBLE
                holder.imageView_Visto.visibility = View.GONE
            }
        } //Fin solo_right

        val c = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        if (chats.fecha.equals(dateFormat.format(c.time))) {
            holder.textView_Fecha.text = "Hoy " + chats.hora
        } else {
            holder.textView_Fecha.setText(chats.fecha + " " + chats.hora)
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }


    class ViewHolderAdapter(itemView: View): RecyclerView.ViewHolder(itemView){
        val textView_Message: TextView = itemView.findViewById(R.id.textView_Message)
        val textView_Fecha: TextView = itemView.findViewById(R.id.textView_Fecha)
        val imageView_Entregado: ImageView = itemView.findViewById(R.id.imageView_Entregado)
        val imageView_Visto: ImageView = itemView.findViewById(R.id.imageView_Visto)
    }
}