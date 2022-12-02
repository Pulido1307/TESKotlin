package com.polar.industries.teskotlin

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.polar.industries.teskotlin.models.Propuestas
import kotlinx.android.synthetic.main.activity_propuesta_contrato.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PropuestaContratoActivity : AppCompatActivity() {

    private lateinit var databasePropuesta: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_propuesta_contrato)

        textViewTalacheroPropuesta.text = "${intent.getStringExtra("nombreT")!!.trim()} ${intent.getStringExtra("apellidosT")!!.trim()}"
        textViewNClientePropuesta.text = "${intent.getStringExtra("nombreC")!!.trim()} ${intent.getStringExtra("apellidosC")!!.trim()}"

        databasePropuesta = Firebase.database.getReference("Contratos")

        actionsButtons()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun actionsButtons() {
        buttonEnviarPropuesta.setOnClickListener {
            var flagDescripcion: Boolean = false
            var flagMonto: Boolean = false

            var descripcionProblema: String = textInputLayoutDescripcionPropuesta.editText!!.text.toString()
            var monto: Double = textInputLayoutCosto.editText!!.text.toString() as Double

            if(descripcionProblema.isNotEmpty()){
                if (descripcionProblema.length > 31){
                    flagDescripcion = true
                } else{
                    textInputLayoutDescripcionPropuesta.error = "Ingresa una descripción más detallada"
                }
            }else{
                textInputLayoutDescripcionPropuesta.error = "Campo requerido"
            }

            if(textInputLayoutCosto.editText!!.text.toString().isNotEmpty()){
                if(monto > 99){
                    flagMonto = true
                }else{
                    textInputLayoutCosto.error = "Monto demasiado bajo"
                }
            }else{
                textInputLayoutCosto.error = "Campo requerido"
            }

            if (flagDescripcion && flagMonto){
                val date = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("MMM dd yyyy"))
                val propuesta: Propuestas  = Propuestas(intent.getStringExtra("clienteUID"), intent.getStringExtra("talacheroUID"), monto, descripcionProblema, date,
                "PENDIENTE", intent.getStringExtra("nombreC") + " " + intent.getStringExtra("apellidosC"), intent.getStringExtra("nombreT") + " " + intent.getStringExtra("apellidosT") )
                databasePropuesta.push().setValue(propuesta).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, "Propuesta enviada", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }
            }
        }

    }


    override fun onBackPressed() {
        finish()
    }
}