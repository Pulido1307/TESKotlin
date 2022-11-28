package com.polar.industries.teskotlin

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.polar.industries.teskotlin.helpers.Constantes
import com.polar.industries.teskotlin.helpers.FirebaseAuthHelper
import com.polar.industries.teskotlin.interfaces.Information
import java.util.*
import java.util.regex.Pattern

class SignInActivity : AppCompatActivity(), Information {
    private var linearLayout_especialidad_sign: LinearLayout? = null
    private var textInputLayout_Nombre: TextInputLayout? = null
    private var textInputLayout_Apellidos: TextInputLayout? = null
    private var textInputLayout_Telefono: TextInputLayout? = null
    private var textInputLayout_Ubicacion: TextInputLayout? = null
    private var textInputLayout_Email: TextInputLayout? = null
    private var textInputLayout_Pass: TextInputLayout? = null
    private var textInputLayout_ConfirmPass: TextInputLayout? = null
    private var textInputLayout_Especialidad: TextInputLayout? = null
    private var materialButton_Resgistrarse: MaterialButton? = null
    private val firebaseAuthHelper: FirebaseAuthHelper = FirebaseAuthHelper()
    private var tipo: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        linearLayout_especialidad_sign = findViewById(R.id.linearLayout_especialidad_sign)
        textInputLayout_Nombre = findViewById(R.id.textInputLayout_Nombre)
        textInputLayout_Apellidos = findViewById(R.id.textInputLayout_Apellidos)
        textInputLayout_Telefono = findViewById(R.id.textInputLayout_Telefono)
        textInputLayout_Ubicacion = findViewById(R.id.textInputLayout_Ubicacion)
        textInputLayout_Email = findViewById(R.id.textInputLayout_Email)
        textInputLayout_Pass = findViewById(R.id.textInputLayout_Pass)
        textInputLayout_ConfirmPass = findViewById(R.id.textInputLayout_ConfirmPass)
        textInputLayout_Especialidad = findViewById(R.id.textInputLayout_Especialidad)
        materialButton_Resgistrarse = findViewById(R.id.materialButton_Registrarse)
        textInputLayout_Especialidad!!.isLongClickable = false
        firebaseAuthHelper.setContext(this@SignInActivity)
        firebaseAuthHelper.setOnInformationListener(this)
        val parametros = this.intent.extras
        if (parametros != null) {
            tipo = parametros.getString("ROL")
            if (tipo == "CLIENTE") {
                linearLayout_especialidad_sign!!.visibility = View.GONE
            }
        } else {
            Toast.makeText(
                this@SignInActivity,
                "Hubo un error al cargar la actividad",
                Toast.LENGTH_LONG
            ).show()
        }
        materialButton_Resgistrarse!!.setOnClickListener(View.OnClickListener { registrarse() })
        textInputLayout_Especialidad!!.getEditText()!!
            .setOnClickListener { //Toast.makeText(getApplicationContext(), "Especialidad", Toast.LENGTH_SHORT).show();
                abrirMenuEspecialidades()
            }
    }

    private fun abrirMenuEspecialidades() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.dialog_especialidades, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()

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
            textInputLayout_Especialidad!!.editText!!.setText("")
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
            var otr = false
            if (radioButton_otro_especialidad.isChecked) {
                seleccion = true
                val otro = textInputLayout_otro_especialidad.editText!!.text.toString()
                if (!otro.isEmpty()) {
                    especialidadesSelecciondas = especialidadesSelecciondas + otro
                    otr = true
                } else {
                    seleccion = false
                }
            }
            if (seleccion) {
                if (otr) {
                    textInputLayout_Especialidad!!.editText!!.setText(
                        especialidadesSelecciondas.substring(
                            0,
                            especialidadesSelecciondas.length
                        )
                    )
                } else {
                    textInputLayout_Especialidad!!.editText!!.setText(
                        especialidadesSelecciondas.substring(
                            0,
                            especialidadesSelecciondas.length - 2
                        )
                    )
                }
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
    }

    private fun registrarse() {
        val nombre = textInputLayout_Nombre!!.editText!!.text.toString()
        val apellidos = textInputLayout_Apellidos!!.editText!!.text.toString()
        val telefono = textInputLayout_Telefono!!.editText!!.text.toString()
        val ubicacion = textInputLayout_Ubicacion!!.editText!!.text.toString()
        val especialidad = textInputLayout_Especialidad!!.editText!!.text.toString()
        val email =
            textInputLayout_Email!!.editText!!.text.toString().lowercase(Locale.getDefault())
        val pass = textInputLayout_Pass!!.editText!!.text.toString()
        val confirmPass = textInputLayout_ConfirmPass!!.editText!!.text.toString()
        if (validarCampos(nombre, apellidos, telefono, ubicacion, especialidad, email, pass, confirmPass)) {
            val args = arrayOfNulls<String>(5)
            args[0] = nombre
            args[1] = apellidos
            args[2] = telefono
            args[3] = ubicacion
            args[4] = especialidad
            val dialog = ProgressDialog.show(this@SignInActivity, "", "Resgistrando... ", true)
            dialog.show()
            firebaseAuthHelper.createUserEmailAndPassword(email, pass, dialog, args, tipo)
        }
    }

    private fun validarCampos(nombre: String, apellidos: String, telefono: String, ubicacion: String, especialidad: String, email: String, pass: String, confirmPass: String): Boolean {
        textInputLayout_Nombre!!.error = null
        textInputLayout_Apellidos!!.error = null
        textInputLayout_Telefono!!.error = null
        textInputLayout_Ubicacion!!.error = null
        textInputLayout_Especialidad!!.error = null
        textInputLayout_Email!!.error = null
        textInputLayout_Pass!!.error = null
        textInputLayout_ConfirmPass!!.error = null
        var bNombre = false
        var bApellidos = false
        var bTelefono = false
        var bUbicacion = false
        var bEspecialidad = false
        var bEmail = false
        var bPass = false
        if (!nombre.isEmpty()) {
            bNombre = true
        } else {
            textInputLayout_Nombre!!.error = "Campo requerido"
        }
        if (!apellidos.isEmpty()) {
            bApellidos = true
        } else {
            textInputLayout_Apellidos!!.error = "Campo requerido"
        }
        if (!telefono.isEmpty()) {
            if (telefono.length == 10) {
                bTelefono = true
            } else {
                textInputLayout_Telefono!!.error = "Número de teléfono no válido"
            }
        } else {
            textInputLayout_Telefono!!.error = "Campo requerido"
        }
        if (!ubicacion.isEmpty()) {
            bUbicacion = true
        } else {
            textInputLayout_Ubicacion!!.error = "Campo requerido"
        }
        if (tipo == "TALACHERO") {
            if (!especialidad.isEmpty()) {
                bEspecialidad = true
            } else {
                textInputLayout_Especialidad!!.error = "Campo requerido"
            }
        } else {
            bEspecialidad = true
        }
        if (!email.isEmpty()) {
            if (Pattern.matches(Constantes.EXREGEMAIL, email)) {
                bEmail = true
            } else {
                textInputLayout_Email!!.error = "Correo no válido"
            }
        } else {
            textInputLayout_Email!!.error = "Campo requerido"
        }
        if (!pass.isEmpty()) {
            if (pass.length > 5) {
                if (!confirmPass.isEmpty()) {
                    if (confirmPass.length > 5) {
                        if (pass == confirmPass) {
                            bPass = true
                        } else {
                            textInputLayout_ConfirmPass!!.error = "Las contraseñas no son iguales"
                            textInputLayout_Pass!!.error = "Las contraseñas no son iguales"
                        }
                    } else {
                        textInputLayout_ConfirmPass!!.error =
                            "La contraseña debe tener mínimo 6 caracteres"
                    }
                } else {
                    textInputLayout_ConfirmPass!!.error = "Campo requerido"
                }
            } else {
                textInputLayout_Pass!!.error = "La contraseña debe tener mínimo 6 caracteres"
            }
        } else {
            textInputLayout_Pass!!.error = "Campo requerido"
        }
        return if (bNombre && bApellidos && bTelefono && bUbicacion && bEspecialidad && bEmail && bPass) {
            true
        } else {
            false
        }
    }

    override fun getMessage(message: String?) {
        Toast.makeText(this@SignInActivity, message, Toast.LENGTH_SHORT).show()
    }
}