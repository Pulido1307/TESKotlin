package com.polar.industries.teskotlin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.polar.industries.teskotlin.R
import com.polar.industries.teskotlin.models.Contrato

class AdapterListViewContratoFinalizado(
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
        var contratoHolder: ContratosPendientesHolder? = null
        if (view == null) {
            view = layoutInflater.inflate(idLayout, null)
            contratoHolder = ContratosPendientesHolder()
            contratoHolder.imageView_FotoPerfil_ItemCF =
                view.findViewById(R.id.imageView_FotoPerfil_ItemCF)
            contratoHolder.textView_Nombre_ItemCF = view.findViewById(R.id.textView_Nombre_ItemCF)
            contratoHolder.textView_DescrServicio_ItemCF =
                view.findViewById(R.id.textView_DescrServicio_ItemCF)
            contratoHolder.textView_Fecha_ItemCF = view.findViewById(R.id.textView_Fecha_ItemCF)
            contratoHolder.textView_Costo_ItemCF = view.findViewById(R.id.textView_Costo_ItemCF)
            contratoHolder.imageButton_Calif_ItemCF =
                view.findViewById(R.id.imageButton_Calif_ItemCF)
            view.tag = contratoHolder

            //contratotoHolder.imageView_FotoPerfil_ItemCP.setImageResource();

            contratoHolder.textView_Nombre_ItemCF!!.text = contrato.nombreCliente
            contratoHolder.textView_DescrServicio_ItemCF!!.text = contrato.descrServicio
            contratoHolder.textView_Fecha_ItemCF!!.text = contrato.fecha
            contratoHolder.textView_Costo_ItemCF!!.text = contrato.costo
            contratoHolder.imageButton_Calif_ItemCF!!.setOnClickListener{

            }
        } else {
            view.tag
        }
        return view
    }

    internal class ContratosPendientesHolder {
        var imageView_FotoPerfil_ItemCF: ImageView? = null
        var textView_Nombre_ItemCF: TextView? = null
        var textView_DescrServicio_ItemCF: TextView? = null
        var textView_Fecha_ItemCF: TextView? = null
        var textView_Costo_ItemCF: TextView? = null
        var imageButton_Calif_ItemCF: ImageButton? = null
    }
}