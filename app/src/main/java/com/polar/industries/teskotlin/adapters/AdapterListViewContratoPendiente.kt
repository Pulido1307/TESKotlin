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
import com.google.android.material.button.MaterialButton
import com.polar.industries.teskotlin.ChatActivity
import com.polar.industries.teskotlin.R
import com.polar.industries.teskotlin.models.Contrato

class AdapterListViewContratoPendiente(
    private val context: Context,
    private val idLayout: Int,
    contratoes: List<Contrato>
) :
    BaseAdapter() {
    private val layoutInflater: LayoutInflater
    private var contratoes: List<Contrato>

    init {
        this.contratoes = contratoes
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    var contratosPendientes: List<Any>
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
        var contratoHolder: ContratosPendientesHolder? = null
        if (view == null) {
            view = layoutInflater.inflate(idLayout, null)
            contratoHolder = ContratosPendientesHolder()
            contratoHolder.imageView_FotoPerfil_ItemCP =
                view.findViewById(R.id.imageView_FotoPerfil_ItemCP)
            contratoHolder.textView_Nombre_ItemCP = view.findViewById(R.id.textView_Nombre_ItemCP)
            contratoHolder.textView_DescrServicioCP =
                view.findViewById(R.id.textView_DescrServicioCP)
            contratoHolder.textView_Fecha_ItemCP = view.findViewById(R.id.textView_Fecha_ItemCP)
            contratoHolder.textView_Costo_ItemCP = view.findViewById(R.id.textView_Costo_ItemCP)
            contratoHolder.imageButton_Mensaje_ItemCP =
                view.findViewById(R.id.imageButton_Mensaje_ItemCP)
            contratoHolder.materialButton_Aceptar_ItemCP =
                view.findViewById(R.id.materialButton_Aceptar_ItemCP)
            contratoHolder.materialButton_Rechazar_ItemCP =
                view.findViewById(R.id.materialButton_Rechazar_ItemCP)
            view.tag = contratoHolder

            //contratotoHolder.imageView_FotoPerfil_ItemCP.setImageResource();
            contratoHolder.textView_Nombre_ItemCP!!.text = contrato.nombreCliente
            contratoHolder.textView_DescrServicioCP!!.text = contrato.descrServicio
            contratoHolder.textView_Fecha_ItemCP!!.text = contrato.fecha
            contratoHolder.textView_Costo_ItemCP!!.text = contrato.costo
            contratoHolder.imageButton_Mensaje_ItemCP!!.setOnClickListener {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("NombreTalachero", contratoes[position].nombreCliente)
                context.startActivity(intent)
            }
            contratoHolder.materialButton_Aceptar_ItemCP!!.setOnClickListener { }
            contratoHolder.materialButton_Rechazar_ItemCP!!.setOnClickListener { }
        } else {
            view.tag
        }
        return view
    }

    internal class ContratosPendientesHolder {
        var imageView_FotoPerfil_ItemCP: ImageView? = null
        var textView_Nombre_ItemCP: TextView? = null
        var textView_DescrServicioCP: TextView? = null
        var textView_Fecha_ItemCP: TextView? = null
        var textView_Costo_ItemCP: TextView? = null
        var imageButton_Mensaje_ItemCP: ImageButton? = null
        var materialButton_Aceptar_ItemCP: MaterialButton? = null
        var materialButton_Rechazar_ItemCP: MaterialButton? = null
    }
}
