package com.polar.industries.teskotlin

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.polar.industries.teskotlin.adapters.AdapterMensajeria
import com.polar.industries.teskotlin.adapters.AdapterTalacheros
import com.polar.industries.teskotlin.helpers.FirebaseAuthHelper
import com.polar.industries.teskotlin.helpers.FirebaseFirestoreHelper
import com.polar.industries.teskotlin.interfaces.Contacto
import com.polar.industries.teskotlin.models.Chats
import com.polar.industries.teskotlin.models.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

class MensajeriaActivity : AppCompatActivity(), Contacto {

    private lateinit var recyclerViewMensajeria: RecyclerView
    private var listaContactos: ArrayList<User> = arrayListOf()
    private  var listaIDS: ArrayList<String> = arrayListOf()
    private lateinit var database: DatabaseReference
    private val firebaseFirestoreHelper: FirebaseFirestoreHelper = FirebaseFirestoreHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensajeria)
        supportActionBar!!.hide()

        database = Firebase.database.getReference("Chats")
        recyclerViewMensajeria = findViewById(R.id.recyclerViewMensajeria)


        getIdContactos()
    }

    private fun getIdContactos() {
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    listaIDS.clear()

                    for (userSnapShot in snapshot.children){
                        val usuarioActual = userSnapShot.getValue(Chats::class.java)

                        if(usuarioActual!!.envia!!.equals(FirebaseAuthHelper.mAuth.currentUser!!.uid) ){
                            if(listaIDS.indexOf(usuarioActual!!.recibe) == -1){
                                listaIDS.add(usuarioActual!!.recibe.toString())
                            }
                        } else if(usuarioActual!!.recibe!!.equals(FirebaseAuthHelper.mAuth.currentUser!!.uid) ){
                            if(listaIDS.indexOf(usuarioActual!!.envia) == -1){
                                listaIDS.add(usuarioActual!!.envia.toString())
                            }
                        }
                    }
                    val progressDialog = ProgressDialog.show(this@MensajeriaActivity, "", "Cargando...", true)
                    firebaseFirestoreHelper.readUsuariosMensajeria(progressDialog, this@MensajeriaActivity, listaIDS)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MensajeriaActivity, "Yo solo quiero ser feliz :c", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun getUsers(usuarios: ArrayList<User>) {
        if(usuarios.size != 0){
            listaContactos = usuarios

            recyclerViewMensajeria.layoutManager = LinearLayoutManager(this@MensajeriaActivity)
            recyclerViewMensajeria.adapter = AdapterMensajeria(this@MensajeriaActivity, listaContactos, this@MensajeriaActivity)
        }
    }
}