package com.polar.industries.teskotlin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.polar.industries.teskotlin.R
import com.polar.industries.teskotlin.adapters.AdaptadorContratoRechazado
import com.polar.industries.teskotlin.helpers.FirebaseFirestoreHelper
import com.polar.industries.teskotlin.models.Propuestas

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContractsRejectedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContractsRejectedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var databaseContratos: DatabaseReference
    private lateinit var recyclerViewContractsRejected: RecyclerView
    private var listaPropuestas: ArrayList<Propuestas> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.fragment_contracts_rejected, container, false)

        databaseContratos = Firebase.database.getReference("Contratos")
        recyclerViewContractsRejected = root.findViewById(R.id.recyclerViewContractsRejected)
        recyclerViewContractsRejected.layoutManager = LinearLayoutManager(root.context)
        recyclerViewContractsRejected.setHasFixedSize(true)

       if(FirebaseFirestoreHelper.user!!.tipo_user!!.equals("CLIENTE")){
           databaseContratos.addValueEventListener(object : ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                   if(snapshot.exists()){
                       listaPropuestas.clear()
                       for (propuestasSnapShot in snapshot.children){
                           val propuestaActual = propuestasSnapShot.getValue(Propuestas::class.java)
                           if(propuestaActual!!.status.equals("RECHAZADO") && propuestaActual.clienteUID!!.equals(
                                   FirebaseFirestoreHelper.user!!.id)){
                               listaPropuestas.add(propuestaActual)
                           }
                       }

                       recyclerViewContractsRejected.adapter = AdaptadorContratoRechazado(root.context, listaPropuestas)
                   }
               }
               override fun onCancelled(error: DatabaseError) {
                   Toast.makeText(root.context, "Ya se cayó más feo que la maquina", Toast.LENGTH_SHORT).show()
               }

           })
       }else {
           databaseContratos.addValueEventListener(object : ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                   if(snapshot.exists()){
                       listaPropuestas.clear()
                       for (propuestasSnapShot in snapshot.children){
                           val propuestaActual = propuestasSnapShot.getValue(Propuestas::class.java)
                           if(propuestaActual!!.status.equals("RECHAZADO") && propuestaActual.talacheroUID!!.equals(
                                   FirebaseFirestoreHelper.user!!.id)){
                               listaPropuestas.add(propuestaActual)
                           }
                       }

                       recyclerViewContractsRejected.adapter = AdaptadorContratoRechazado(root.context, listaPropuestas)
                   }
               }

               override fun onCancelled(error: DatabaseError) {
                   Toast.makeText(root.context, "Ya se cayó más feo que la maquina", Toast.LENGTH_SHORT).show()
               }

           })
       }

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContractsRejectedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContractsRejectedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}