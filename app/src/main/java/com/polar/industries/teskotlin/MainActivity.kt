package com.polar.industries.teskotlin

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.polar.industries.teskotlin.adapters.AdapterListViewContratoPendiente
import com.polar.industries.teskotlin.datosPrueba.DatosPrueba
import com.polar.industries.teskotlin.helpers.FirebaseAuthHelper
import com.polar.industries.teskotlin.helpers.FirebaseFirestoreHelper
import com.polar.industries.teskotlin.helpers.FirebaseStorageHelper
import com.polar.industries.teskotlin.helpers.ImagesCompressHelper
import com.polar.industries.teskotlin.interfaces.Information
import com.polar.industries.teskotlin.models.Contrato
import id.zelory.compressor.Compressor
import java.io.File
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), Information {
    private var imagen: File? = null
    private var tipo: String? = null
    private var textView_Nombre: TextView? = null
    private var textView_Ubicacion: TextView? = null
    private var textView_Especialidad: TextView? = null
    private var textView_Correo: TextView? = null
    private var materialButton_BuscarT_VerC: MaterialButton? = null
    private var materialButton_AbrirMensajeria: MaterialButton? = null
    private var floatingActionButton_contratos_propuestos: FloatingActionButton? = null
    private val firebaseAuthHelper: FirebaseAuthHelper = FirebaseAuthHelper()
    private var imageButton_EditInfoPerfil: ImageButton? = null
    private var imageButton_EditFotoPerfil: ImageButton? = null
    private var imageView_FotoPerfil: ImageView? = null
    private val ERROR = "Campo requerido"
    private val firebaseFirestoreHelper: FirebaseFirestoreHelper = FirebaseFirestoreHelper()
    private val firebaseStorageHelper: FirebaseStorageHelper = FirebaseStorageHelper()

    //private FirebaseFirestoreHelper firestoreHelper = new FirebaseFirestoreHelper();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Talachitas Express Services"
        textView_Nombre = findViewById(R.id.textView_Nombre)
        textView_Ubicacion = findViewById(R.id.textView_Ubicacion)
        textView_Especialidad = findViewById(R.id.textView_Especialidad)
        textView_Correo = findViewById(R.id.textView_Correo)
        materialButton_BuscarT_VerC = findViewById(R.id.materialButton_BuscarT_VerC)
        materialButton_AbrirMensajeria = findViewById(R.id.materialButton_AbrirMensajeria)
        floatingActionButton_contratos_propuestos =
            findViewById(R.id.floatingActionButton_contratos_propuestos)
        imageButton_EditInfoPerfil = findViewById(R.id.imageButton_EditInfoPerfil)
        imageButton_EditFotoPerfil = findViewById(R.id.imageButton_EditFotoPerfil)
        imageView_FotoPerfil = findViewById(R.id.imageView_FotoPerfil)
        tipo = FirebaseFirestoreHelper.user!!.tipo_user
        Log.e("ROL", tipo!!)
        if (tipo == "CLIENTE") {
            textView_Especialidad!!.visibility = View.GONE
            materialButton_BuscarT_VerC!!.setText("BUSCAR TALACHERO")
        } else {
            floatingActionButton_contratos_propuestos!!.visibility = View.GONE
        }
        setInformation()
        buttons()
    }

    override fun onStart() {
        super.onStart()
        firebaseStorageHelper.setInformationListener(this)
    }

    private fun setInformation() {
        textView_Nombre!!.setText(FirebaseFirestoreHelper.user!!.nombre + " " + FirebaseFirestoreHelper.user!!.apellidos)
        textView_Especialidad!!.text =
            "Especialidad: " + FirebaseFirestoreHelper.user!!.especialidad
        textView_Ubicacion!!.text = "Ubicación: " + FirebaseFirestoreHelper.user!!.ubicacion
        textView_Correo!!.text = "Email: " + FirebaseFirestoreHelper.user!!.email
        setImage(FirebaseFirestoreHelper.user!!.uriImage!!)
    }

    private fun buttons() {
        materialButton_BuscarT_VerC!!.setOnClickListener {
            //Buscar Talachero
            if (tipo == "CLIENTE") {
                val intent = Intent(this@MainActivity, SpecialtyActivity::class.java)
                startActivity(intent)
            } else {
                //TALACHERO. Ver Contratos
                val intent = Intent(this@MainActivity, ContractHistoryActivity::class.java)
                startActivity(intent)
            }
        }
        materialButton_AbrirMensajeria!!.setOnClickListener {
            val intent = Intent(this@MainActivity, MensajeriaActivity::class.java)
            startActivity(intent)
        }
        floatingActionButton_contratos_propuestos!!.setOnClickListener { dialogContratosPendientes() }
        imageButton_EditInfoPerfil!!.setOnClickListener {
            Toast.makeText(this@MainActivity, "Actualizar información", Toast.LENGTH_SHORT).show()
            mostrarDialogEditarPerfil()
        }
        imageButton_EditFotoPerfil!!.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.overflow, menu)
        if (tipo.equals("talachero", ignoreCase = true)) {
            menu.removeItem(R.id.item_contratos_propuestos)
        } else {
            menu.removeItem(R.id.item_ganacias)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.item_cerrar_sesion -> {
                Toast.makeText(this@MainActivity, "Cerrar sesión" + "...", Toast.LENGTH_SHORT)
                    .show()
                val dialog = ProgressDialog.show(
                    this@MainActivity, "",
                    "Nos vemos pronto...", true
                )
                firebaseAuthHelper.signout(dialog, this@MainActivity)
            }
            R.id.item_contratos_propuestos -> dialogContratosPendientes()
            R.id.item_ganacias -> {
                val intent = Intent(this@MainActivity, GananciasActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dialogContratosPendientes() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.dialog_contratos_propuestos, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val listView_contratos_pendientes =
            dialog.findViewById<ListView>(R.id.listView_contratos_pendientes)
        val adapterListViewCP = AdapterListViewContratoPendiente(
            this@MainActivity,
            R.layout.item_contrato_pendiente,
            DatosPrueba.listContratosPendientes as List<Contrato>
        )
        listView_contratos_pendientes.adapter = adapterListViewCP
    }

    private fun mostrarDialogEditarPerfil() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.dialog_editar_informacion, null)
        builder.setView(view)
        val dialogEditarPerfil = builder.create()
        dialogEditarPerfil.setCancelable(false)
        dialogEditarPerfil.show()
        val textInputLayout_Nombre_editar =
            dialogEditarPerfil.findViewById<TextInputLayout>(R.id.textInputLayout_Nombre_editar)
        val textInputLayout_apellidos_editar =
            dialogEditarPerfil.findViewById<TextInputLayout>(R.id.textInputLayout_apellidos_editar)
        val textInputLayout_Telefono_editar =
            dialogEditarPerfil.findViewById<TextInputLayout>(R.id.textInputLayout_Telefono_editar)
        val textInputLayout_Ubicacion_editar =
            dialogEditarPerfil.findViewById<TextInputLayout>(R.id.textInputLayout_Ubicacion_editar)
        val textInputLayout_especialidad_editar =
            dialogEditarPerfil.findViewById<TextInputLayout>(R.id.textInputLayout_especialidad_editar)
        val linearLayout_Especialidad_editar =
            dialogEditarPerfil.findViewById<LinearLayout>(R.id.linearLayout_Especialidad_editar)
        val materialButton_salir_editar =
            dialogEditarPerfil.findViewById<MaterialButton>(R.id.materialButton_salir_editar)
        val materialButton_actualizar_editar =
            dialogEditarPerfil.findViewById<MaterialButton>(R.id.materialButton_actualizar_editar)
        if (FirebaseFirestoreHelper.user!!.tipo_user.equals("CLIENTE")) {
            linearLayout_Especialidad_editar.visibility = View.GONE
        } else {
            textInputLayout_especialidad_editar.editText!!.setText(FirebaseFirestoreHelper.user!!.especialidad)
        }
        textInputLayout_Nombre_editar.editText!!.setText(FirebaseFirestoreHelper.user!!.nombre)
        textInputLayout_apellidos_editar.editText!!.setText(FirebaseFirestoreHelper.user!!.apellidos)
        textInputLayout_Telefono_editar.editText!!.setText(FirebaseFirestoreHelper.user!!.telefono)
        textInputLayout_Ubicacion_editar.editText!!.setText(FirebaseFirestoreHelper.user!!.ubicacion)
        textInputLayout_especialidad_editar.editText!!.setOnClickListener {
            abrirMenuEspecialidades(
                textInputLayout_especialidad_editar, textInputLayout_especialidad_editar.editText!!
                    .text.toString()
            )
        }
        materialButton_salir_editar.setOnClickListener { dialogEditarPerfil.dismiss() }
        materialButton_actualizar_editar.setOnClickListener {
            var flag_nombre = false
            var flag_apellido = false
            var flag_telefono = false
            var flag_ubicacion = false
            var flag_especialidad = false
            if (!textInputLayout_Nombre_editar.editText!!.text.toString().isEmpty()) {
                flag_nombre = true
            } else {
                textInputLayout_Nombre_editar.error = ERROR
            }
            if (!textInputLayout_apellidos_editar.editText!!.text.toString().isEmpty()) {
                flag_apellido = true
            } else {
                textInputLayout_apellidos_editar.error = ERROR
            }
            if (!textInputLayout_Telefono_editar.editText!!.text.toString().isEmpty()) {
                if (textInputLayout_Telefono_editar.editText!!.text.toString().length == 10) {
                    flag_telefono = true
                } else {
                    textInputLayout_Telefono_editar.error = "Número de teléfono no válido"
                }
            } else {
                textInputLayout_Telefono_editar.error = ERROR
            }
            if (!textInputLayout_Ubicacion_editar.editText!!.text.toString().isEmpty()) {
                flag_ubicacion = true
            } else {
                textInputLayout_Ubicacion_editar.error = ERROR
            }

            if (FirebaseFirestoreHelper.user!!.tipo_user!!.equals("Cliente")) {
                flag_especialidad = true
            } else {
                if (!textInputLayout_especialidad_editar.editText!!.text.toString().isEmpty()) {
                    flag_especialidad = true
                } else {
                    textInputLayout_especialidad_editar.error = ERROR
                }
            }
            if (flag_nombre && flag_apellido && flag_telefono && flag_ubicacion && flag_especialidad) {
                val dialog = ProgressDialog.show(this@MainActivity, "", "Actualizando..", true)
                dialog.show()
                firebaseFirestoreHelper.updateDataUser(
                    dialog,
                    this@MainActivity,
                    textInputLayout_Nombre_editar.editText!!
                        .text.toString(),
                    textInputLayout_apellidos_editar.editText!!.text.toString(),
                    textInputLayout_Telefono_editar.editText!!
                        .text.toString(),
                    textInputLayout_Ubicacion_editar.editText!!.text.toString(),
                    textInputLayout_especialidad_editar.editText!!
                        .text.toString(),
                    FirebaseFirestoreHelper.user!!.tipo_user!!,
                    this@MainActivity
                )
                dialogEditarPerfil.dismiss()
            }
        }
    }

    private fun abrirMenuEspecialidades(
        textInputLayout_Especialidad: TextInputLayout,
        especialidades: String
    ) {
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
                .into(imageView_FotoPerfil!!)
        } else {
            rm.load(FirebaseFirestoreHelper.user!!.uriImage)
                .fitCenter()
                .centerCrop()
                .apply(RequestOptions.circleCropTransform()) //.apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                .into(imageView_FotoPerfil!!)
        }
    }

    private fun setImage(file: File?) {
        Glide.with(applicationContext)
            .load(file)
            .fitCenter()
            .centerCrop()
            .apply(RequestOptions.circleCropTransform())
            .into(imageView_FotoPerfil!!)
    }

    companion object {
        private const val GALLERY_INTENT = 1
    }

    override fun getMessage(message: String?) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        if (message == "¡Datos actualizados!") {
            setInformation()
        } else if (message == "Imagen eliminada") {
            setImage("")
        }
    }
}