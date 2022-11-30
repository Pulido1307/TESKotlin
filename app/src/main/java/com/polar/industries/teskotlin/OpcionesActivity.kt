package com.polar.industries.teskotlin

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.polar.industries.teskotlin.helpers.FirebaseAuthHelper
import com.polar.industries.teskotlin.helpers.FirebaseFirestoreHelper
import kotlinx.android.synthetic.main.activity_opciones.*

class OpcionesActivity : AppCompatActivity() {
    private val firebaseAuthHelper: FirebaseAuthHelper = FirebaseAuthHelper()
    private lateinit var imageViewUserOpc: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opciones)
        supportActionBar!!.hide()

        imageViewUserOpc = findViewById(R.id.imageViewUserOpc)


        getInformationUser()
        actionButtons()
    }

    private fun actionButtons() {

        cardViewEditarPerfil.setOnClickListener {
            val intent: Intent = Intent(this, EditarPerfilActivity::class.java)
            startActivity(intent)
        }

        cardViewCambiarContrasena.setOnClickListener {
            val intent: Intent = Intent(this, CambiarContrasenaActivity::class.java)
            startActivity(intent)
        }

        cardViewMensajeriaOpc.setOnClickListener {
            val intent: Intent = Intent(this@OpcionesActivity, MensajeriaActivity::class.java)
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
        setImage(FirebaseFirestoreHelper.user!!.uriImage!!)
    }

    private fun setImage(image_url: String) {
        val rm = Glide.with(applicationContext)
        if (image_url == "") {
            val placeholder =
                BitmapFactory.decodeResource(applicationContext.resources, R.drawable.usuario)
            val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(
                applicationContext.resources, placeholder
            )
            circularBitmapDrawable.isCircular = true
            rm.load(circularBitmapDrawable)
                .fitCenter()
                .centerCrop()
                .apply(RequestOptions.circleCropTransform()) //.apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                .into(imageViewUserOpc!!)
        } else {
            rm.load(FirebaseFirestoreHelper.user!!.uriImage)
                .fitCenter()
                .centerCrop()
                .apply(RequestOptions.circleCropTransform()) //.apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                .into(imageViewUserOpc!!)
        }
    }
}