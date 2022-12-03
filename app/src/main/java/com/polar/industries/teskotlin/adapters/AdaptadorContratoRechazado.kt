package com.polar.industries.teskotlin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.polar.industries.teskotlin.R
import com.polar.industries.teskotlin.helpers.FirebaseFirestoreHelper
import com.polar.industries.teskotlin.models.Propuestas

class AdaptadorContratoRechazado(private val context: Context, private val listaContratos: ArrayList<Propuestas>): RecyclerView.Adapter<AdaptadorContratoRechazado.HolderContratoRechazado>(){

    inner class HolderContratoRechazado(itemView: View): RecyclerView.ViewHolder(itemView){
        val textView_Nombre_ItemCR: TextView = itemView.findViewById(R.id.textView_Nombre_ItemCR)
        val textView_DescrServicio_ItemCR: TextView = itemView.findViewById(R.id.textView_DescrServicio_ItemCR)
        val textView_Fecha_ItemCR: TextView = itemView.findViewById(R.id.textView_Fecha_ItemCR)
        val textView_Costo_ItemCR: TextView = itemView.findViewById(R.id.textView_Costo_ItemCR)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderContratoRechazado {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_contract_rejected, parent, false)
        return HolderContratoRechazado(itemView)
    }

    override fun onBindViewHolder(holder: HolderContratoRechazado, position: Int) {
        val contratoActual: Propuestas = listaContratos[position]
        if(FirebaseFirestoreHelper.user!!.tipo_user.equals("CLIENTE")){
            holder.textView_Nombre_ItemCR.text = contratoActual.nombreTalachero
        } else{
            holder.textView_Nombre_ItemCR.text = contratoActual.nombreCliente
        }

        holder.textView_DescrServicio_ItemCR.text = contratoActual.descripcionArreglo
        holder.textView_Fecha_ItemCR.text = contratoActual.fecha
        holder.textView_Costo_ItemCR.text = "$ ${contratoActual.monto}"
    }

    override fun getItemCount(): Int {
        return listaContratos.size
    }
}