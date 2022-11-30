package com.polar.industries.teskotlin

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.polar.industries.teskotlin.helpers.FirebaseAuthHelper
import com.polar.industries.teskotlin.helpers.FirebaseFirestoreHelper
import kotlinx.android.synthetic.main.activity_opciones.*

class OpcionesActivity : AppCompatActivity() {
    private val firebaseAuthHelper: FirebaseAuthHelper = FirebaseAuthHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opciones)
        supportActionBar!!.hide()

        getInformationUser()
        actionButtons()
    }

    private fun actionButtons() {

        cardViewEditarPerfil.setOnClickListener {
            val intent: Intent = Intent(this, EditarPerfilActivity::class.java)
            startActivity(intent)
        }

        cardViewMensajeriaOpc.setOnClickListener {
            val intent: Intent = Intent(this@OpcionesActivity, MensajeriaActivity::class.java)
            startActivity(intent)
        }

        cardViewCambiarContrasena.setOnClickListener {
            val intent: Intent = Intent(this, CambiarContrasenaActivity::class.java)
            startActivity(intent)
        }

        carViewContratos.setOnClickListener {
            val intent: Intent = Intent(this, ContractsActivity::class.java)
            startActivity(intent)
        }//pendiente

        buttonCerrarSesion.setOnClickListener {
            val progressDialog = ProgressDialog.show(this@OpcionesActivity, "", "Cerrando sesión...", true)
            firebaseAuthHelper.signout(progressDialog, this@OpcionesActivity)
       }
    }

    private fun getInformationUser() {
        textViewNombreUserMenu.text =
            "${FirebaseFirestoreHelper.user!!.nombre} ${FirebaseFirestoreHelper.user!!.apellidos}"
        textViewCorreoUserMenu.text = "${FirebaseFirestoreHelper.user!!.email}"
        textViewDireccionUserMenu.text = "${FirebaseFirestoreHelper.user!!.ubicacion}"
    }
}