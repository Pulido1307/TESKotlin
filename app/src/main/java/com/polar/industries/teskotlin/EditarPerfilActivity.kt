package com.polar.industries.teskotlin

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.polar.industries.teskotlin.helpers.FirebaseFirestoreHelper
import com.polar.industries.teskotlin.helpers.FirebaseStorageHelper
import com.polar.industries.teskotlin.helpers.ImagesCompressHelper
import com.polar.industries.teskotlin.interfaces.Information
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_editar_perfil.*
import java.io.File
import java.io.IOException
import java.util.*

class EditarPerfilActivity : AppCompatActivity(), Information {
    private lateinit var imageViewUserActualizar: ImageView
    private val firebaseFirestoreHelper: FirebaseFirestoreHelper = FirebaseFirestoreHelper()
    private val firebaseStorageHelper: FirebaseStorageHelper = FirebaseStorageHelper()
    private var imagen: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)
        supportActionBar!!.hide()

        imageViewUserActualizar = findViewById(R.id.imageViewUserActualizar)
        setInformation()

        if (FirebaseFirestoreHelper.user!!.tipo_user.equals("CLIENTE")) {
            textInputLayoutEspecialidadActualizar.visibility = View.GONE
        } else {
            textInputLayoutEspecialidadActualizar.editText!!.setText(FirebaseFirestoreHelper.user!!.especialidad)
        }

        textInputLayoutEspecialidadActualizar.setOnClickListener {
            abrirMenuEspecialidades(textInputLayoutEspecialidadActualizar, textInputLayoutEspecialidadActualizar.editText!!.text.toString())
        }

        actionsButtons()
    }

    private fun actionsButtons() {
        buttonCambiarFotoActualizar.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this@EditarPerfilActivity)
            alertDialogBuilder.setTitle("Opciones:")
            alertDialogBuilder.setMessage("Elige una opción a realizar, sino da click fuera del recuadro.")
            alertDialogBuilder.setPositiveButton(
                "Modificar foto."
            ) { alertDialog, i ->
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(
                    Intent.createChooser(intent, "Selecciona una imagen"),
                    GALLERY_INTENT
                )
                alertDialog.cancel()
            }
            alertDialogBuilder.setNegativeButton(
                "Eliminar foto actual."
            ) { alertDialog, i ->
                if (!FirebaseFirestoreHelper.user!!.uriImage.equals("")) {
                    firebaseStorageHelper.deleteImage(FirebaseFirestoreHelper.user!!.id!!)
                } else {
                    getMessage("No tienes ninguna foto agregada en el sistema.")
                }
                alertDialog.cancel()
            }
            alertDialogBuilder.show()
        }

        buttonGuardarCambiosAct.setOnClickListener {
            actualizarInfo()
        }

        buttonCancelarActualizar.setOnClickListener {
            onBackPressed()
        }
    }

    private fun actualizarInfo() {
        var flag_nombre = false
        var flag_apellido = false
        var flag_telefono = false
        var flag_ubicacion = false
        var flag_especialidad = false

        if (textInputLayoutNombreActualizar.editText!!.text.toString().isNotEmpty()){
            flag_nombre = true
        } else {
            textInputLayoutNombreActualizar.error = "Campo requerido"
        }

        if(textInputLayoutApellidosActualizar.editText!!.text.toString().isNotEmpty()){
            flag_apellido = true
        } else {
            textInputLayoutApellidosActualizar.error = "Campo requerido"
        }

        if (textInputLayoutTelefonoActualizar.editText!!.text.toString().isNotEmpty()) {
            if (textInputLayoutTelefonoActualizar.editText!!.text.toString().length == 10) {
                flag_telefono = true
            } else {
                textInputLayoutTelefonoActualizar.error = "Número de teléfono no válido"
            }
        } else {
            textInputLayoutTelefonoActualizar.error = "Campo requerido"
        }

        if (textInputLayoutDireccionActualizar.editText!!.text.toString().isNotEmpty()) {
            flag_ubicacion = true
        } else {
            textInputLayoutDireccionActualizar.error = "Campo requerido"
        }

        if (FirebaseFirestoreHelper.user!!.tipo_user!!.equals("CLIENTE")) {
            flag_especialidad = true
        } else {
            if (textInputLayoutEspecialidadActualizar.editText!!.text.toString().isNotEmpty()) {
                flag_especialidad = true
            } else {
                textInputLayoutEspecialidadActualizar.error = "Campo requerido"
            }
        }

        if (flag_nombre && flag_apellido && flag_telefono && flag_ubicacion && flag_especialidad) {
            val dialog = ProgressDialog.show(this@EditarPerfilActivity, "", "Actualizando..", true)

            firebaseFirestoreHelper.updateDataUser(
                dialog,
                this@EditarPerfilActivity,
                textInputLayoutNombreActualizar.editText!!.text.toString(),
                textInputLayoutApellidosActualizar.editText!!.text.toString(),
                textInputLayoutTelefonoActualizar.editText!!.text.toString(),
                textInputLayoutDireccionActualizar.editText!!.text.toString(),
                textInputLayoutEspecialidadActualizar.editText!!.text.toString(),
                FirebaseFirestoreHelper.user!!.tipo_user!!,
                this@EditarPerfilActivity
            )

        }
    }

    override fun getMessage(message: String?) {
        Toast.makeText(this@EditarPerfilActivity, message, Toast.LENGTH_SHORT).show()
        if (message == "¡Datos actualizados!") {
            setInformation()
            onBackPressed()
        } else if (message == "Imagen eliminada") {
            setImage("")
            onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_INTENT) {
            try {
                val uri = Objects.requireNonNull(data)!!.data
                imagen = ImagesCompressHelper.from(applicationContext, uri!!)
                imagen = Compressor(applicationContext).compressToFile(imagen)
                setImage(imagen)
                firebaseStorageHelper.deleteImage(FirebaseFirestoreHelper.user!!.id)
                firebaseStorageHelper.addImage(
                    FirebaseFirestoreHelper.user!!.id,
                    Uri.fromFile(imagen)
                )
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
    }

    private fun setImage(image_url: String) {
        val rm = Glide.with(applicationContext)
        if (image_url == "") {
            val placeholder =
                BitmapFactory.decodeResource(applicationContext.resources, R.drawable.user)
            val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(
                applicationContext.resources, placeholder
            )
            circularBitmapDrawable.isCircular = true
            rm.load(circularBitmapDrawable)
                .fitCenter()
                .centerCrop()
                .apply(RequestOptions.circleCropTransform()) //.apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                .into(imageViewUserActualizar!!)
        } else {
            rm.load(FirebaseFirestoreHelper.user!!.uriImage)
                .fitCenter()
                .centerCrop()
                .apply(RequestOptions.circleCropTransform()) //.apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                .into(imageViewUserActualizar!!)
        }

    }

    private fun setImage(file: File?) {
        Glide.with(applicationContext)
            .load(file)
            .fitCenter()
            .centerCrop()
            .apply(RequestOptions.circleCropTransform())
            .into(imageViewUserActualizar!!)
    }

    companion object {
        private const val GALLERY_INTENT = 1
    }


    private fun setInformation() {
        textInputLayoutNombreActualizar.editText!!.setText(FirebaseFirestoreHelper.user!!.nombre)
        textInputLayoutApellidosActualizar.editText!!.setText(FirebaseFirestoreHelper.user!!.apellidos)
        textInputLayoutTelefonoActualizar.editText!!.setText(FirebaseFirestoreHelper.user!!.telefono)
        textInputLayoutDireccionActualizar.editText!!.setText(FirebaseFirestoreHelper.user!!.ubicacion)
        textInputLayoutEspecialidadActualizar.editText!!.setText(FirebaseFirestoreHelper.user!!.especialidad)
        setImage(FirebaseFirestoreHelper.user!!.uriImage!!)

    }

    private fun abrirMenuEspecialidades(textInputLayout_Especialidad: TextInputLayout, especialidades: String) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.dialog_especialidades, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.setCancelable(false)

        //Nota: Se deben renombrar las variables.
        val radioButton_albanileria_especialidad =
            dialog.findViewById<CheckBox>(R.id.radioButton_albanileria_especialidad)
        val radioButton_carpinteria_especialidad =
            dialog.findViewById<CheckBox>(R.id.radioButton_carpinteria_especialidad)
        val radioButton_cerrajeria_especialidad =
            dialog.findViewById<CheckBox>(R.id.radioButton_cerrajeria_especialidad)
        val radioButton_electricista_especialidad =
            dialog.findViewById<CheckBox>(R.id.radioButton_electricista_especialidad)
        val radioButton_fontaneria_especialidad =
            dialog.findViewById<CheckBox>(R.id.radioButton_fontaneria_especialidad)
        val radioButton_herreria_especialidad =
            dialog.findViewById<CheckBox>(R.id.radioButton_herreria_especialidad)
        val radioButton_mecanica_especialidad =
            dialog.findViewById<CheckBox>(R.id.radioButton_mecanica_especialidad)
        val radioButton_pintor_especialidad =
            dialog.findViewById<CheckBox>(R.id.radioButton_pintor_especialidad)
        val radioButton_plomeria_especialidad =
            dialog.findViewById<CheckBox>(R.id.radioButton_plomeria_especialidad)
        val radioButton_mudanza_especialidad =
            dialog.findViewById<CheckBox>(R.id.radioButton_mudanza_especialidad)
        val radioButton_otro_especialidad =
            dialog.findViewById<CheckBox>(R.id.radioButton_otro_especialidad)
        val textInputLayout_otro_especialidad =
            dialog.findViewById<TextInputLayout>(R.id.textInputLayout_otro_especialidad)
        val materialButton_registrar_especilidades =
            dialog.findViewById<MaterialButton>(R.id.materialButton_registrar_especilidades)
        val materialButton_salir_especialidades =
            dialog.findViewById<MaterialButton>(R.id.materialButton_salir_especialidades)
        materialButton_registrar_especilidades.setOnClickListener { view ->
            textInputLayout_Especialidad.editText!!.setText("")
            var seleccion = false
            var especialidadesSelecciondas = ""
            if (radioButton_albanileria_especialidad.isChecked) {
                seleccion = true
                especialidadesSelecciondas = especialidadesSelecciondas + "Albañilería, "
            }
            if (radioButton_carpinteria_especialidad.isChecked) {
                seleccion = true
                especialidadesSelecciondas = especialidadesSelecciondas + "Carpintería, "
            }
            if (radioButton_cerrajeria_especialidad.isChecked) {
                seleccion = true
                especialidadesSelecciondas = especialidadesSelecciondas + "Cerrajería, "
            }
            if (radioButton_electricista_especialidad.isChecked) {
                seleccion = true
                especialidadesSelecciondas = especialidadesSelecciondas + "Electricista, "
            }
            if (radioButton_fontaneria_especialidad.isChecked) {
                seleccion = true
                especialidadesSelecciondas = especialidadesSelecciondas + "Fontanería, "
            }
            if (radioButton_herreria_especialidad.isChecked) {
                seleccion = true
                especialidadesSelecciondas = especialidadesSelecciondas + "Herrería, "
            }
            if (radioButton_mecanica_especialidad.isChecked) {
                seleccion = true
                especialidadesSelecciondas = especialidadesSelecciondas + "Mecánica, "
            }
            if (radioButton_pintor_especialidad.isChecked) {
                seleccion = true
                especialidadesSelecciondas = especialidadesSelecciondas + "Pintor, "
            }
            if (radioButton_plomeria_especialidad.isChecked) {
                seleccion = true
                especialidadesSelecciondas = especialidadesSelecciondas + "Plomería, "
            }
            if (radioButton_mudanza_especialidad.isChecked) {
                seleccion = true
                especialidadesSelecciondas = especialidadesSelecciondas + "Mudanza, "
            }
            if (radioButton_otro_especialidad.isChecked) {
                seleccion = true
                val otro = textInputLayout_otro_especialidad.editText!!.text.toString()
                if (!otro.isEmpty()) {
                    especialidadesSelecciondas = especialidadesSelecciondas + otro
                } else {
                    seleccion = false
                }
            }
            if (seleccion) {
                textInputLayout_Especialidad.editText!!
                    .setText(
                        especialidadesSelecciondas.substring(
                            0,
                            especialidadesSelecciondas.length - 2
                        )
                    )
                dialog.dismiss()
            } else {
                Snackbar.make(
                    view,
                    "Debes seleccionar al menos una especialidad. Si marcaste Otro, debes escribir ese oficio.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        materialButton_salir_especialidades.setOnClickListener { dialog.dismiss() }

        //Marcar especialidades
        if (especialidades.contains("Albañilería")) {
            radioButton_albanileria_especialidad.isChecked = true
        }
        if (especialidades.contains("Carpintería")) {
            radioButton_carpinteria_especialidad.isChecked = true
        }
        if (especialidades.contains("Cerrajería")) {
            radioButton_cerrajeria_especialidad.isChecked = true
        }
        if (especialidades.contains("Electricista")) {
            radioButton_electricista_especialidad.isChecked = true
        }
        if (especialidades.contains("Fontanería")) {
            radioButton_fontaneria_especialidad.isChecked = true
        }
        if (especialidades.contains("Herrería")) {
            radioButton_herreria_especialidad.isChecked = true
        }
        if (especialidades.contains("Mécanica automotriz")) {
            radioButton_mecanica_especialidad.isChecked = true
        }
        if (especialidades.contains("Pintor")) {
            radioButton_pintor_especialidad.isChecked = true
        }
        if (especialidades.contains("Plomería")) {
            radioButton_plomeria_especialidad.isChecked = true
        }
        if (especialidades.contains("Servicio de mudanza")) {
            radioButton_mudanza_especialidad.isChecked = true
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseStorageHelper.setInformationListener(this)
    }


    override fun onBackPressed() {
        val intent: Intent = Intent(this@EditarPerfilActivity, OpcionesActivity::class.java)
        startActivity(intent)
        finish()
    }



}