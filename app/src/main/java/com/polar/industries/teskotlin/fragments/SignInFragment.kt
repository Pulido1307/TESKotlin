package com.polar.industries.teskotlin.fragments

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputLayout
import com.polar.industries.teskotlin.R
import com.polar.industries.teskotlin.helpers.Constantes
import com.polar.industries.teskotlin.helpers.FirebaseAuthHelper
import com.polar.industries.teskotlin.interfaces.Information
import java.util.*
import java.util.regex.Pattern

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SignInFragment : Fragment(), Information {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var root: View
    private val firebaseAuthHelper: FirebaseAuthHelper = FirebaseAuthHelper()
    private lateinit var textField_Name_sign_in: TextInputLayout
    private lateinit var textField_LastName_sign_in: TextInputLayout
    private lateinit var textField_EmailSI_sign_in: TextInputLayout
    private lateinit var textField_Phone_sign_in: TextInputLayout
    private lateinit var textField_Address_sign_in: TextInputLayout
    private lateinit var textField_PasswordSI_sign_in: TextInputLayout
    private lateinit var textField_ConfirmPass_sign_in: TextInputLayout
    private lateinit var textField_Speciality_sign_in: TextInputLayout

    private var tipo: String = "TALACHERO"

    private lateinit var switchDecision_sign_in: SwitchMaterial

    private lateinit var button_SingIn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root =  inflater.inflate(R.layout.fragment_sign_in, container, false)

        textField_Name_sign_in = root.findViewById(R.id.textField_Name_sign_in)
        textField_LastName_sign_in = root.findViewById(R.id.textField_LastName_sign_in)
        textField_EmailSI_sign_in = root.findViewById(R.id.textField_EmailSI_sign_in)
        textField_Phone_sign_in = root.findViewById(R.id.textField_Phone_sign_in)
        textField_Address_sign_in = root.findViewById(R.id.textField_Address_sign_in)
        textField_PasswordSI_sign_in = root.findViewById(R.id.textField_PasswordSI_sign_in)
        textField_ConfirmPass_sign_in = root.findViewById(R.id.textField_ConfirmPass_sign_in)
        textField_Speciality_sign_in = root.findViewById(R.id.textField_Speciality_sign_in)

        switchDecision_sign_in = root.findViewById(R.id.switchDecision_sign_in)

        button_SingIn = root.findViewById(R.id.button_SingIn)
        firebaseAuthHelper.setContext(root.context)
        firebaseAuthHelper.setOnInformationListener(this)

        textField_Speciality_sign_in.isLongClickable = false

        customTextFieldSpeciality()

        buttons()

        return root
    }

    private fun buttons() {
        button_SingIn.setOnClickListener {
            checkIn()
        }
    }

    private fun checkIn() {
        val nombre = textField_Name_sign_in!!.editText!!.text.toString()
        val apellidos = textField_LastName_sign_in!!.editText!!.text.toString()
        val telefono = textField_Phone_sign_in!!.editText!!.text.toString()
        val ubicacion = textField_Address_sign_in!!.editText!!.text.toString()
        val especialidad = textField_Speciality_sign_in!!.editText!!.text.toString()
        val email =
            textField_EmailSI_sign_in!!.editText!!.text.toString().lowercase(Locale.getDefault())
        val pass = textField_PasswordSI_sign_in!!.editText!!.text.toString()
        val confirmPass = textField_ConfirmPass_sign_in!!.editText!!.text.toString()


        if (validarCampos(nombre, apellidos, telefono, ubicacion, especialidad, email, pass, confirmPass)) {
            val args = arrayOfNulls<String>(5)
            args[0] = nombre
            args[1] = apellidos
            args[2] = telefono
            args[3] = ubicacion
            args[4] = especialidad
            val dialog = ProgressDialog.show(root.context, "", "Registrando... ", true)
            dialog.show()
            firebaseAuthHelper.createUserEmailAndPassword(email, pass, dialog, args, tipo)
        }

    }

    private fun validarCampos(nombre: String, apellidos: String, telefono: String, ubicacion: String, especialidad: String, email: String, pass: String, confirmPass: String): Boolean {
        textField_Name_sign_in!!.error = null
        textField_LastName_sign_in!!.error = null
        textField_Phone_sign_in!!.error = null
        textField_Address_sign_in!!.error = null
        textField_Speciality_sign_in!!.error = null
        textField_EmailSI_sign_in!!.error = null
        textField_PasswordSI_sign_in!!.error = null
        textField_ConfirmPass_sign_in!!.error = null
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
            textField_Name_sign_in!!.error = "Campo requerido"
        }
        if (!apellidos.isEmpty()) {
            bApellidos = true
        } else {
            textField_LastName_sign_in!!.error = "Campo requerido"
        }
        if (!telefono.isEmpty()) {
            if (telefono.length == 10) {
                bTelefono = true
            } else {
                textField_Phone_sign_in!!.error = "Número de teléfono no válido"
            }
        } else {
            textField_Phone_sign_in!!.error = "Campo requerido"
        }
        if (!ubicacion.isEmpty()) {
            bUbicacion = true
        } else {
            textField_Address_sign_in!!.error = "Campo requerido"
        }

        if (!email.isEmpty()) {
            if (Pattern.matches(Constantes.EXREGEMAIL, email)) {
                bEmail = true
            } else {
                textField_EmailSI_sign_in!!.error = "Correo no válido"
            }
        } else {
            textField_EmailSI_sign_in!!.error = "Campo requerido"
        }
        if (!pass.isEmpty()) {
            if (pass.length > 5) {
                if (!confirmPass.isEmpty()) {
                    if (confirmPass.length > 5) {
                        if (pass == confirmPass) {
                            bPass = true
                        } else {
                            textField_ConfirmPass_sign_in!!.error = "Las contraseñas no son iguales"
                            textField_PasswordSI_sign_in!!.error = "Las contraseñas no son iguales"
                        }
                    } else {
                        textField_ConfirmPass_sign_in!!.error =
                            "La contraseña debe tener mínimo 6 caracteres"
                    }
                } else {
                    textField_ConfirmPass_sign_in!!.error = "Campo requerido"
                }
            } else {
                textField_PasswordSI_sign_in!!.error = "La contraseña debe tener mínimo 6 caracteres"
            }
        } else {
            textField_PasswordSI_sign_in!!.error = "Campo requerido"
        }

        if(tipo == "TALACHERO"){
            if (especialidad.isNotEmpty()) {
                bEspecialidad = true

            } else {
                textField_Speciality_sign_in!!.error = "Campo requerido"
            }
        }else{
            bEspecialidad = true
        }


        return bNombre && bApellidos && bTelefono && bUbicacion && bEspecialidad && bEmail && bPass
    }


    private fun customTextFieldSpeciality() {
        switchDecision_sign_in.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){
                textField_Speciality_sign_in.visibility = View.GONE
                tipo = "CLIENTE"
            }
            else {
                textField_Speciality_sign_in.visibility = View.VISIBLE
                tipo = "TALACHERO"
            }
        }

        textField_Speciality_sign_in!!.getEditText()!!
            .setOnClickListener{
                abrirMenuEspecialidades()
            }
    }

    private fun abrirMenuEspecialidades() {
        val builder = AlertDialog.Builder(root.context)
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
        /*val radioButton_otro_especialidad =
            dialog.findViewById<CheckBox>(R.id.radioButton_otro_especialidad)*/
        val textInputLayout_otro_especialidad =
            dialog.findViewById<TextInputLayout>(R.id.textInputLayout_otro_especialidad)
        val materialButton_registrar_especilidades =
            dialog.findViewById<MaterialButton>(R.id.materialButton_registrar_especilidades)
        val materialButton_salir_especialidades =
            dialog.findViewById<MaterialButton>(R.id.materialButton_salir_especialidades)
        materialButton_registrar_especilidades.setOnClickListener { view ->
            textField_Speciality_sign_in!!.editText!!.setText("")
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
            /*if (radioButton_otro_especialidad.isChecked) {
                seleccion = true
                val otro = textInputLayout_otro_especialidad.editText!!.text.toString()
                if (!otro.isEmpty()) {
                    especialidadesSelecciondas = especialidadesSelecciondas + otro
                    otr = true
                } else {
                    seleccion = false
                }
            }*/
            if (seleccion) {
                if (otr) {
                    textField_Speciality_sign_in!!.editText!!.setText(
                        especialidadesSelecciondas.substring(
                            0,
                            especialidadesSelecciondas.length
                        )
                    )
                } else {
                    textField_Speciality_sign_in!!.editText!!.setText(
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

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun getMessage(message: String?) {
        Toast.makeText(root.context, message, Toast.LENGTH_SHORT).show()
    }


}