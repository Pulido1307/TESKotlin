package com.polar.industries.teskotlin

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.polar.industries.teskotlin.adapters.AdapterChats
import com.polar.industries.teskotlin.helpers.FirebaseAuthHelper
import com.polar.industries.teskotlin.helpers.FirebaseFirestoreHelper
import com.polar.industries.teskotlin.models.Chats
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {


    private lateinit var database: DatabaseReference

    private var nombreDestinatario: String? = null
    private lateinit var conversacionLista: ArrayList<Chats>

    private var idReceptor: String? = ""
    private var idUserEnvia: String? = ""
    private lateinit var imageButtonPropuestaContrato: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        supportActionBar!!.hide()

        idReceptor = intent.getStringExtra("id")
        idUserEnvia = FirebaseAuthHelper.mAuth.currentUser!!.uid

        imageButtonPropuestaContrato = findViewById(R.id.imageButtonPropuestaContrato)

        database = Firebase.database.getReference("Chats")
        recyclerViewChat.layoutManager = LinearLayoutManager(this@ChatActivity)
        recyclerViewChat.setHasFixedSize(true)

        conversacionLista = arrayListOf()

        textViewDestinatarioChat.text = "${intent.getStringExtra("nombre")} ${intent.getStringExtra("apellidos")}"

        if(FirebaseFirestoreHelper.user!!.tipo_user.equals("CLIENTE"))
            imageButtonPropuestaContrato.isVisible = false



        getMensajes()
        actionButtons()
    }

    private fun getMensajes() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    conversacionLista.clear()
                    for (conversacionSnapShot in snapshot.children){
                        val mensajeActual = conversacionSnapShot.getValue(Chats::class.java)
                        if(mensajeActual!!.envia.equals(idReceptor) && mensajeActual!!.recibe.equals(idUserEnvia)){
                            conversacionLista.add(mensajeActual!!)
                        } else if(mensajeActual!!.envia.equals(idUserEnvia) && mensajeActual!!.recibe.equals(idReceptor)){
                            conversacionLista.add(mensajeActual!!)
                        }
                    }
                    recyclerViewChat.adapter = AdapterChats( conversacionLista, this@ChatActivity)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, "Se cayó más feo que la maquina en la liguilla :c", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun actionButtons() {
        imageButtonEnviarMensaje.setOnClickListener {
            if(editTextMensajeChat.text.toString().isNotEmpty()){
                val msj: Chats = Chats(idUserEnvia, idReceptor, editTextMensajeChat.text.toString())
                database.push().setValue(msj)
                Toast.makeText(this@ChatActivity, "¡Mensaje enviado!", Toast.LENGTH_SHORT).show()
                editTextMensajeChat.setText("")
            }
        }

        imageButtonOnBackPress.setOnClickListener {
            onBackPressed()
        }

        imageButtonPropuestaContrato.setOnClickListener {
            val intent: Intent = Intent(this@ChatActivity, PropuestaContratoActivity::class.java).apply {
                putExtra("nombreC", intent.getStringExtra("nombre"))
                putExtra("apellidosC", intent.getStringExtra("apellidos"))
                putExtra("nombreT", FirebaseFirestoreHelper.user!!.nombre.toString())
                putExtra("apellidosT", FirebaseFirestoreHelper.user!!.apellidos.toString())
                putExtra("clienteUID", idReceptor)
            }

            startActivity(intent)
        }
    }


}