package com.polar.industries.teskotlin.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.polar.industries.teskotlin.ChatActivity
import com.polar.industries.teskotlin.R
import com.polar.industries.teskotlin.models.Talacheros

class AdapterListViewTalachero(
    private val context: Context,
    private val idLayout: Int,
    talacheroes: List<Talacheros>
) :
    BaseAdapter() {
    private val layoutInflater: LayoutInflater
    private var talacheroes: List<Talacheros>

    init {
        this.talacheroes = talacheroes
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    var talacheros: List<Any>
        get() = talacheroes
        set(talacheroes) {
            this.talacheroes = talacheroes as List<Talacheros>
            notifyDataSetChanged()
        }

    override fun getCount(): Int {
        return talacheroes.size
    }

    override fun getItem(i: Int): Any {
        return talacheroes[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(position: Int, view: View, viewGroup: ViewGroup): View {
        var view = view
        val talachero: Talacheros = talacheroes[position]
        var talacheroHolder: TalacheroHolder? = null
        if (view == null) {
            view = layoutInflater.inflate(idLayout, null)
            talacheroHolder = TalacheroHolder()
            talacheroHolder.textView_NombreT_Item = view.findViewById(R.id.textView_NombreT_Item)
            talacheroHolder.textView_DireccionT_Item =
                view.findViewById(R.id.textView_DireccionT_Item)
            talacheroHolder.textView_CalificacionT_Item =
                view.findViewById(R.id.textView_CalificacionT_Item)
            talacheroHolder.imageView_FotoPerfilT_Item =
                view.findViewById(R.id.imageView_FotoPerfilT_Item)
            talacheroHolder.imageButton_Mensaje_Item =
                view.findViewById(R.id.imageButton_Mensaje_Item)
            view.tag = talacheroHolder
            talacheroHolder.textView_NombreT_Item!!.text = talacheroes[position].nombre
            talacheroHolder.textView_DireccionT_Item!!.text = "Dirección:  ${talacheroes[position].direccion}"
            talacheroHolder.textView_CalificacionT_Item!!.setText("Calificación:  ${talacheroes[position].calificacion}")
            Glide.with(talacheroHolder.imageView_FotoPerfilT_Item!!.getContext())
                .load(talacheroes[position].image)
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .circleCrop()
                .into(talacheroHolder.imageView_FotoPerfilT_Item!!)
            //talacheroHolder.imageView_FotoPerfilT_Item.setImageResource();
            talacheroHolder.imageButton_Mensaje_Item!!.setOnClickListener{
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("NombreTalachero", talacheroes[position].nombre)
                context.startActivity(intent)
            }
        } else {
            view.tag
        }
        return view
    }

    internal class TalacheroHolder {
        var textView_NombreT_Item: TextView? = null
        var textView_DireccionT_Item: TextView? = null
        var textView_CalificacionT_Item: TextView? = null
        var imageView_FotoPerfilT_Item: ImageView? = null
        var imageButton_Mensaje_Item: ImageView? = null
    }
}
