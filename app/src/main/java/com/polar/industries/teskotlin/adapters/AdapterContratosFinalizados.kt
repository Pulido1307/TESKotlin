package com.polar.industries.teskotlin.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.polar.industries.teskotlin.R
import com.polar.industries.teskotlin.helpers.FirebaseFirestoreHelper
import com.polar.industries.teskotlin.models.Propuestas

class AdapterContratosFinalizados(private val context: Context, private val listaContratos: ArrayList<Propuestas>, activity: Activity): RecyclerView.Adapter<AdapterContratosFinalizados.HolderContratoFinalizado>(){

    inner class HolderContratoFinalizado(itemView:View): RecyclerView.ViewHolder(itemView){
        val textView_Nombre_ItemCF: TextView = itemView.findViewById(R.id.textView_Nombre_ItemCF)
        val textView_DescrServicio_ItemCF: TextView = itemView.findViewById(R.id.textView_DescrServicio_ItemCF)
        val textView_Fecha_ItemCF: TextView = itemView.findViewById(R.id.textView_Fecha_ItemCF)
        val textView_Costo_ItemCF: TextView = itemView.findViewById(R.id.textView_Costo_ItemCF)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderContratoFinalizado {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_contract_finished, parent, false)
        return HolderContratoFinalizado(itemView)
    }

    override fun onBindViewHolder(holder: HolderContratoFinalizado, position: Int) {
        val contratoActual: Propuestas = listaContratos[position]
        if(FirebaseFirestoreHelper.user!!.tipo_user.equals("CLIENTE")){
            holder.textView_Nombre_ItemCF.text = "${contratoActual.nombreTalachero}"
        }else{
            holder.textView_Nombre_ItemCF.text = "${contratoActual.nombreCliente}"
        }
        holder.textView_DescrServicio_ItemCF.text = contratoActual.descripcionArreglo
        holder.textView_Fecha_ItemCF.text = contratoActual.fecha
        holder.textView_Costo_ItemCF.text = "$ ${contratoActual.monto}"
    }

    override fun getItemCount(): Int {
        return listaContratos.size
    }
}