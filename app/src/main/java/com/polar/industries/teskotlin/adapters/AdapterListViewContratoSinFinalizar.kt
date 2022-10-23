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
import com.polar.industries.teskotlin.ChatActivity
import com.polar.industries.teskotlin.R
import com.polar.industries.teskotlin.models.Contrato

class AdapterListViewContratoSinFinalizar(
    private val context: Context,
    private val idLayout: Int,
    contratoes: List<Contrato>
) : BaseAdapter() {
    private val layoutInflater: LayoutInflater
    private var contratoes: List<Contrato>

    init {
        this.contratoes = contratoes
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    var contratosFinalizados: List<Any>
        get() = contratoes
        set(contratoes) {
            this.contratoes = contratoes as List<Contrato>
            notifyDataSetChanged()
        }

    override fun getCount(): Int {
        return contratoes.size
    }

    override fun getItem(i: Int): Any {
        return contratoes[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(position: Int, view: View, viewGroup: ViewGroup): View {
        var view = view
        val contrato: Contrato = contratoes[position]
        var contratoHolder: ContratosSinFinalizarHolder? = null
        if (view == null) {
            view = layoutInflater.inflate(idLayout, null)
            contratoHolder = ContratosSinFinalizarHolder()
            contratoHolder.imageView_FotoPerfil_ItemCSF =
                view.findViewById(R.id.imageView_FotoPerfil_ItemCSF)
            contratoHolder.textView_Nombre_ItemCSF = view.findViewById(R.id.textView_Nombre_ItemCSF)
            contratoHolder.textView_DescrServicio_ItemCSF =
                view.findViewById(R.id.textView_DescrServicio_ItemCSF)
            contratoHolder.textView_Fecha_ItemCSF = view.findViewById(R.id.textView_Fecha_ItemCSF)
            contratoHolder.textView_Costo_ItemCSF = view.findViewById(R.id.textView_Costo_ItemCSF)
            contratoHolder.imageButton_Mensaje_ItemCSF =
                view.findViewById(R.id.imageButton_Mensaje_ItemCSF)
            view.tag = contratoHolder

            //contratotoHolder.imageView_FotoPerfil_ItemCSF.setImageResource();
            contratoHolder.textView_Nombre_ItemCSF!!.text = contrato.nombreCliente
            contratoHolder.textView_DescrServicio_ItemCSF!!.text = contrato.descrServicio
            contratoHolder.textView_Fecha_ItemCSF!!.text = contrato.fecha
            contratoHolder.textView_Costo_ItemCSF!!.text = contrato.costo
            contratoHolder.imageButton_Mensaje_ItemCSF!!.setOnClickListener {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("NombreTalachero", contratoes[position].nombreCliente)
                context.startActivity(intent)
            }
        } else {
            view.tag
        }
        return view
    }

    internal class ContratosSinFinalizarHolder {
        var imageView_FotoPerfil_ItemCSF: ImageView? = null
        var textView_Nombre_ItemCSF: TextView? = null
        var textView_DescrServicio_ItemCSF: TextView? = null
        var textView_Fecha_ItemCSF: TextView? = null
        var textView_Costo_ItemCSF: TextView? = null
        var imageButton_Mensaje_ItemCSF: ImageButton? = null
    }
}
