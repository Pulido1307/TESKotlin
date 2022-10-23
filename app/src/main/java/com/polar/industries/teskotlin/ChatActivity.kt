package com.polar.industries.teskotlin

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.polar.industries.teskotlin.adapters.AdapterChats
import com.polar.industries.teskotlin.helpers.FirebaseFirestoreHelper
import com.polar.industries.teskotlin.models.Chats
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {

    private var tipoUsuario: String? = null
    private var textView_NombreDestinatario_Chat: TextView? = null
    private var editTextTextMultiLine_Mensaje: EditText? = null
    private var imageView_ButtonEnviar: ImageView? = null
    private var adapterChats: AdapterChats? = null
    private var arrayChats: MutableList<Chats>? = null
    private var recyclerView_Chats: RecyclerView? = null
    private var imageButton_propuesta_contrato_chat: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        supportActionBar!!.hide()
        textView_NombreDestinatario_Chat = findViewById(R.id.textView_NombreDestinatario_Chat)
        editTextTextMultiLine_Mensaje = findViewById(R.id.editTextTextMultiLine_Mensaje)
        imageView_ButtonEnviar = findViewById(R.id.imageView_ButtonEnviar)
        imageButton_propuesta_contrato_chat = findViewById(R.id.imageButton_propuesta_contrato_chat)

        val parametros = this.intent.extras
        textView_NombreDestinatario_Chat!!.text = parametros!!.getString("NombreTalachero")
        recyclerView_Chats = findViewById(R.id.recyclerView_Chats)
        recyclerView_Chats!!.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        recyclerView_Chats!!.setLayoutManager(linearLayoutManager)
        arrayChats = ArrayList<Chats>()
        adapterChats = AdapterChats(arrayChats as ArrayList<Chats>, this@ChatActivity)
        recyclerView_Chats!!.setAdapter(adapterChats)
        imageView_ButtonEnviar!!.setOnClickListener {
            val c = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val timeFormat = SimpleDateFormat("HH:mm")
            val texto = editTextTextMultiLine_Mensaje!!.text
            if (!texto.isEmpty()) {
                val chats = Chats()
                chats.mensaje = texto.toString()
                chats.visto = "SI"
                chats.fecha = dateFormat.format(c.time).toString()
                chats.hora = timeFormat.format(c.time).toString()
                arrayChats!!.add(chats)
                adapterChats!!.notifyDataSetChanged()
                editTextTextMultiLine_Mensaje!!.setText("")
            }
        }
        tipoUsuario = FirebaseFirestoreHelper.user!!.tipo_user
        if (tipoUsuario.equals("cliente", ignoreCase = true)) {
            imageButton_propuesta_contrato_chat!!.visibility= View.INVISIBLE
        }
    }
}