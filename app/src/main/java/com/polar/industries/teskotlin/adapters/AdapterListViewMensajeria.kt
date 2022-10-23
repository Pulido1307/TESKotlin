package com.polar.industries.teskotlin.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.polar.industries.teskotlin.ChatActivity
import com.polar.industries.teskotlin.R
import com.polar.industries.teskotlin.models.Contacto

class AdapterListViewMensajeria(
    private val context: Context,
    private val idLayout: Int,
    contactos: List<Contacto>
) :
    BaseAdapter() {
    private val layoutInflater: LayoutInflater
    private var contactos: List<Contacto>

    init {
        this.contactos = contactos
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    fun getContactos(): List<Contacto> {
        return contactos
    }

    fun setContactos(contactos: List<Contacto>) {
        this.contactos = contactos
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return contactos.size
    }

    override fun getItem(i: Int): Any {
        return contactos[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View, viewGroup: ViewGroup): View {
        var view = view
        val contacto: Contacto = contactos[i]
        var contactoHolder: ContactoHolder? = null
        if (view == null) {
            view = layoutInflater.inflate(idLayout, null)
            contactoHolder = ContactoHolder()
            contactoHolder.imageView_FotoPerfil_ItemMensajeria =
                view.findViewById(R.id.imageView_FotoPerfil_ItemMensajeria)
            contactoHolder.textView_Nombre_ItemMensajeria =
                view.findViewById(R.id.textView_Nombre_ItemMensajeria)
            contactoHolder.imageButton_Mensaje_ItemMensajeria =
                view.findViewById(R.id.imageButton_Mensaje_ItemMensajeria)
            contactoHolder.carViewContacto = view.findViewById(R.id.carViewContacto)
            view.tag = contactoHolder

            //contratotoHolder.imageView_FotoPerfil_ItemMensajeria.setImageResource();
            contactoHolder.textView_Nombre_ItemMensajeria!!.text = contacto.nombre
            contactoHolder.imageButton_Mensaje_ItemMensajeria!!.setOnClickListener {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("NombreTalachero", contacto.nombre)
                context.startActivity(intent)
            }
            contactoHolder.carViewContacto!!.setOnClickListener {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("NombreTalachero", contacto.nombre)
                context.startActivity(intent)
            }
        } else {
            view.tag
        }
        return view
    }

    internal class ContactoHolder {
        var imageView_FotoPerfil_ItemMensajeria: ImageView? = null
        var textView_Nombre_ItemMensajeria: TextView? = null
        var imageButton_Mensaje_ItemMensajeria: ImageButton? = null
        var carViewContacto: CardView? = null
    }
}
