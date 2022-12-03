package com.polar.industries.teskotlin.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.polar.industries.teskotlin.R
import com.polar.industries.teskotlin.helpers.FirebaseFirestoreHelper
import com.polar.industries.teskotlin.models.Contrato
import com.polar.industries.teskotlin.models.Propuestas

class AdapterContratosSinFinalizar(private val context: Context, private val listaContratos: ArrayList<Propuestas>, private val activity: Activity): RecyclerView.Adapter<AdapterContratosSinFinalizar.HolderSinFinalizar>(){
    private  var databaseContratos: DatabaseReference = Firebase.database.getReference("Contratos")

    inner class HolderSinFinalizar(itemView: View): RecyclerView.ViewHolder(itemView){
        val textView_Nombre_ItemCSF: TextView = itemView.findViewById(R.id.textView_Nombre_ItemCSF)
        val textView_DescrServicio_ItemCSF: TextView = itemView.findViewById(R.id.textView_DescrServicio_ItemCSF)
        val textView_Fecha_ItemCSF: TextView = itemView.findViewById(R.id.textView_Fecha_ItemCSF)
        val textView_Costo_ItemCSF: TextView = itemView.findViewById(R.id.textView_Costo_ItemCSF)

        val imageButton_Finalizar_ItemCSF: ImageButton = itemView.findViewById(R.id.imageButton_Finalizar_ItemCSF)
        val imageButton_Rechazar_ItemCSF: ImageButton = itemView.findViewById(R.id.imageButton_Rechazar_ItemCSF)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderSinFinalizar {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_contract_without_finish, parent, false)
        return HolderSinFinalizar(itemView)
    }

    override fun onBindViewHolder(holder: HolderSinFinalizar, position: Int) {
        val contratoActual: Propuestas = listaContratos[position]
        if(FirebaseFirestoreHelper.user!!.tipo_user.equals("CLIENTE")){
            if(contratoActual.status.equals("SIN FINALIZAR")){
                holder.imageButton_Finalizar_ItemCSF.visibility = View.INVISIBLE
                holder.imageButton_Rechazar_ItemCSF.visibility = View.INVISIBLE
            }

            holder.textView_Nombre_ItemCSF.text = contratoActual.nombreTalachero
            holder.imageButton_Finalizar_ItemCSF.setOnClickListener {
                Toast.makeText(context, "Acepto el contrato ", Toast.LENGTH_SHORT).show()
                contratoActual.status = "SIN FINALIZAR"
                val childUpdates = hashMapOf<String, Any>(
                    "clienteUID" to contratoActual.clienteUID!!,
                    "descripcionArreglo" to contratoActual.descripcionArreglo!!,
                    "fecha" to contratoActual.fecha!!,
                    "monto" to contratoActual.monto!!,
                    "nombreCliente" to contratoActual.nombreCliente!!,
                    "nombreTalachero" to contratoActual.nombreTalachero!!,
                    "status" to contratoActual.status!!,
                    "talacheroUID" to contratoActual.talacheroUID!!,
                    "uidContrato" to contratoActual.uidContrato!!
                )
                databaseContratos.child(contratoActual.uidContrato).updateChildren(childUpdates)
            }
            holder.imageButton_Rechazar_ItemCSF.setOnClickListener {
                Toast.makeText(context, "Rechazando el contrato", Toast.LENGTH_SHORT).show()
                contratoActual.status = "RECHAZADO"
                val childUpdates = hashMapOf<String, Any>(
                    "clienteUID" to contratoActual.clienteUID!!,
                    "descripcionArreglo" to contratoActual.descripcionArreglo!!,
                    "fecha" to contratoActual.fecha!!,
                    "monto" to contratoActual.monto!!,
                    "nombreCliente" to contratoActual.nombreCliente!!,
                    "nombreTalachero" to contratoActual.nombreTalachero!!,
                    "status" to contratoActual.status!!,
                    "talacheroUID" to contratoActual.talacheroUID!!,
                    "uidContrato" to contratoActual.uidContrato!!
                )
                databaseContratos.child(contratoActual.uidContrato).updateChildren(childUpdates)
            }
        }else{
            holder.textView_Nombre_ItemCSF.text = contratoActual.nombreTalachero
            holder.imageButton_Finalizar_ItemCSF.setOnClickListener {
                Toast.makeText(context, "Terminando el contrato", Toast.LENGTH_SHORT).show()
                contratoActual.status = "FINALIZADO"
                val childUpdates = hashMapOf<String, Any>(
                    "clienteUID" to contratoActual.clienteUID!!,
                    "descripcionArreglo" to contratoActual.descripcionArreglo!!,
                    "fecha" to contratoActual.fecha!!,
                    "monto" to contratoActual.monto!!,
                    "nombreCliente" to contratoActual.nombreCliente!!,
                    "nombreTalachero" to contratoActual.nombreTalachero!!,
                    "status" to contratoActual.status!!,
                    "talacheroUID" to contratoActual.talacheroUID!!,
                    "uidContrato" to contratoActual.uidContrato!!
                )
                databaseContratos.child(contratoActual.uidContrato).updateChildren(childUpdates)
            }
            holder.imageButton_Rechazar_ItemCSF.setOnClickListener {
                Toast.makeText(context, "Cancelando el contrato", Toast.LENGTH_SHORT).show()
                contratoActual.status = "RECHAZADO"
                val childUpdates = hashMapOf<String, Any>(
                    "clienteUID" to contratoActual.clienteUID!!,
                    "descripcionArreglo" to contratoActual.descripcionArreglo!!,
                    "fecha" to contratoActual.fecha!!,
                    "monto" to contratoActual.monto!!,
                    "nombreCliente" to contratoActual.nombreCliente!!,
                    "nombreTalachero" to contratoActual.nombreTalachero!!,
                    "status" to contratoActual.status!!,
                    "talacheroUID" to contratoActual.talacheroUID!!,
                    "uidContrato" to contratoActual.uidContrato!!
                )
                databaseContratos.child(contratoActual.uidContrato).updateChildren(childUpdates)
            }
        }

        holder.textView_DescrServicio_ItemCSF.text = contratoActual.descripcionArreglo
        holder.textView_Fecha_ItemCSF.text = contratoActual.fecha
        holder.textView_Costo_ItemCSF.text = "$ ${contratoActual.monto.toString()}"


    }

    override fun getItemCount(): Int {
        return listaContratos.size
    }
}