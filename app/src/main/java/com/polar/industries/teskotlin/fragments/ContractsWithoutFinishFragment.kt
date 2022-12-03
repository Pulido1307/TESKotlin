package com.polar.industries.teskotlin.fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.polar.industries.teskotlin.adapters.AdapterContratosSinFinalizar
import com.polar.industries.teskotlin.helpers.FirebaseAuthHelper
import com.polar.industries.teskotlin.helpers.FirebaseFirestoreHelper
import com.polar.industries.teskotlin.models.Propuestas

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContractsWithoutFinishFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContractsWithoutFinishFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var databaseContratos: DatabaseReference
    private lateinit var recyclerContractsWithoutFinish: RecyclerView
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
        val root: View = inflater.inflate(R.layout.fragment_contracts_without_finish, container, false)
        databaseContratos = Firebase.database.getReference("Contratos")

        recyclerContractsWithoutFinish = root.findViewById(R.id.recyclerContractsWithoutFinish)

        recyclerContractsWithoutFinish.layoutManager = LinearLayoutManager(root.context)
        recyclerContractsWithoutFinish.setHasFixedSize(true)


        if (FirebaseFirestoreHelper.user!!.tipo_user!!.equals("CLIENTE")){
            databaseContratos.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        listaPropuestas.clear()
                        for (propuestasSnapShot in snapshot.children){
                            val propuestaActual = propuestasSnapShot.getValue(Propuestas::class.java)
                            if((propuestaActual!!.status.equals("SIN FINALIZAR") || propuestaActual!!.status.equals("PENDIENTE")) && propuestaActual.clienteUID!!.equals(FirebaseFirestoreHelper.user!!.id)){
                                listaPropuestas.add(propuestaActual)
                            }
                        }

                        recyclerContractsWithoutFinish.adapter = AdapterContratosSinFinalizar(root.context, listaPropuestas, root.context as Activity)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(root.context, "Ya se cayó más feo que la maquina", Toast.LENGTH_SHORT).show()
                }

            })
        } else{
            databaseContratos.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        listaPropuestas.clear()
                        for (propuestasSnapShot in snapshot.children){
                            val propuestaActual = propuestasSnapShot.getValue(Propuestas::class.java)
                            if(propuestaActual!!.status.equals("SIN FINALIZAR") && propuestaActual.talacheroUID!!.equals(FirebaseFirestoreHelper.user!!.id)){
                                listaPropuestas.add(propuestaActual)
                            }
                        }

                        recyclerContractsWithoutFinish.adapter = AdapterContratosSinFinalizar(root.context, listaPropuestas, root.context as Activity)
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
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContractsWithoutFinishFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}