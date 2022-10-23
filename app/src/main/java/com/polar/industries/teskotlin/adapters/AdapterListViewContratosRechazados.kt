package com.polar.industries.teskotlin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.polar.industries.teskotlin.R
import com.polar.industries.teskotlin.models.Contrato

class AdapterListViewContratosRechazados(
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
        var view: View? = view
        val contrato: Contrato = contratoes[position]
        var contratosRechazadosHolder: ContratosRechazadosHolder? = null
        if (view == null) {
            view = layoutInflater.inflate(idLayout, null)
            contratosRechazadosHolder = ContratosRechazadosHolder()
            contratosRechazadosHolder.imageView_FotoPerfil_ItemCR =
                view.findViewById(R.id.imageView_FotoPerfil_ItemCR)
            contratosRechazadosHolder.textView_Nombre_ItemCR =
                view.findViewById(R.id.textView_Nombre_ItemCR)
            contratosRechazadosHolder.textView_DescrServicio_ItemCR =
                view.findViewById(R.id.textView_DescrServicio_ItemCR)
            contratosRechazadosHolder.textView_Fecha_ItemCR =
                view.findViewById(R.id.textView_Fecha_ItemCR)
            contratosRechazadosHolder.textView_Costo_ItemCR =
                view.findViewById(R.id.textView_Costo_ItemCR)
            view.tag = contratosRechazadosHolder

            //contratotoRechazadosHolder.imageView_FotoPerfil_ItemCR.setImageResource();
            contratosRechazadosHolder.textView_Nombre_ItemCR!!.text = contrato.nombreCliente
            contratosRechazadosHolder.textView_DescrServicio_ItemCR!!.text = contrato.descrServicio
            contratosRechazadosHolder.textView_Fecha_ItemCR!!.text = contrato.fecha
            contratosRechazadosHolder.textView_Costo_ItemCR!!.text = contrato.costo
        } else {
            view.tag
        }
        return view!!
    }

    internal class ContratosRechazadosHolder {
        var imageView_FotoPerfil_ItemCR: ImageView? = null
        var textView_Nombre_ItemCR: TextView? = null
        var textView_DescrServicio_ItemCR: TextView? = null
        var textView_Fecha_ItemCR: TextView? = null
        var textView_Costo_ItemCR: TextView? = null
    }
}
