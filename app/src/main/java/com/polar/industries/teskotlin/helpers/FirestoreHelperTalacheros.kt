package com.polar.industries.teskotlin.helpers

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.polar.industries.teskotlin.interfaces.TalacheroInterface
import com.polar.industries.teskotlin.models.Talacheros

class FirestoreHelperTalacheros {
    /**
     * obtener talachero de la colección
     */
    fun getTalacheros(talacheroInterface: TalacheroInterface) {
        _TALACHEROCOLLECTION.whereEqualTo("tipo_user", "TALACHERO").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val talacheros: MutableList<Talacheros> =
                        ArrayList<Talacheros>()
                    for (document in task.result) {
                        talacheros.add(
                            Talacheros(
                                document.getString("nombre"),
                                document.getString("ubicacion"),
                                5,
                                document.getString("uri_image"),
                                document.getString("especialidad")!!
                            )
                        )
                    }
                    //talacheroInterface.getTalacheros(talacheros)
                } else {
                    Log.d("ERROR", "ERROR al obtener documentos", task.exception)
                }
            }
    }

    companion object {
        var _DB = FirebaseFirestore.getInstance()
        var _TALACHEROCOLLECTION = _DB.collection("usuarios")
    }
}
