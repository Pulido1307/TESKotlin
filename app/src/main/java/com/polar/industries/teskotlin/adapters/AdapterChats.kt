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
import com.polar.industries.teskotlin.helpers.FirebaseAuthHelper
import com.polar.industries.teskotlin.helpers.FirebaseFirestoreHelper
import com.polar.industries.teskotlin.models.Chats
import java.text.SimpleDateFormat
import java.util.*

class AdapterChats(private val chatList: List<Chats>, private val context: Context): RecyclerView.Adapter<AdapterChats.ViewHolderAdapter>() {

    val MENSAJE_RIGHT:Int = 1
    val MENSAJE_LEFT: Int = 0
    var solo_right: Boolean = false
    var firebaseUser: FirebaseUser? = null
    public val DERECHA: Int = 1
    public val IZQUIERDA: Int = 0
    public var mensajePropio = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAdapter {
        if(viewType == MENSAJE_RIGHT){
            val view: View = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false)
            return AdapterChats.ViewHolderAdapter(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false)
            return AdapterChats.ViewHolderAdapter(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolderAdapter, position: Int) {
        val mensajeActual: Chats = chatList[position]

        holder.textViewMensajeItem.text = mensajeActual.mensaje
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (chatList[position].envia.equals(FirebaseAuthHelper.mAuth.currentUser!!.uid)){
            mensajePropio = true
            return DERECHA
        } else{
            mensajePropio = false
            return IZQUIERDA
        }
    }


    class ViewHolderAdapter(itemView: View): RecyclerView.ViewHolder(itemView){
        val textViewMensajeItem: TextView = itemView.findViewById(R.id.textViewMensajeItem)
    }
}