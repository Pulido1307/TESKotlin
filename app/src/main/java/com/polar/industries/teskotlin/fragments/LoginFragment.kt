package com.polar.industries.teskotlin.fragments

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.polar.industries.teskotlin.R
import com.polar.industries.teskotlin.helpers.Constantes
import com.polar.industries.teskotlin.helpers.FirebaseAuthHelper
import com.polar.industries.teskotlin.interfaces.Information
import java.util.regex.Pattern


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment(), Information {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var button_Login: Button
    private var textInputLayout_Email: TextInputLayout? = null
    private var textInputLayout_Pass: TextInputLayout? = null
    private val firebaseAuthHelper: FirebaseAuthHelper = FirebaseAuthHelper()
    private lateinit var root: View

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
        root = inflater.inflate(R.layout.fragment_login, container, false)


        button_Login = root.findViewById(R.id.materialButton_IniciarSesion)
        textInputLayout_Email = root.findViewById(R.id.textInputLayout_Email_Login)
        textInputLayout_Pass = root.findViewById(R.id.textInputLayout_Pass_Login)
        firebaseAuthHelper.setContext(root.context)
        firebaseAuthHelper.setOnInformationListener(this)

        button_Login.setOnClickListener {
            val email = textInputLayout_Email!!.editText!!.text.toString()
            val pass = textInputLayout_Pass!!.editText!!.text.toString()
            if (validarCampos(email, pass)) {
                val dialog = ProgressDialog.show(root.context!!, "", "Ingresando... ", true)
                dialog.show()
                firebaseAuthHelper.signInWithEmailAndPassword(email, pass, dialog)
            }
        }

        return root
    }

    private fun validarCampos(email: String, pass: String): Boolean {
        textInputLayout_Email!!.error = null
        textInputLayout_Pass!!.error = null
        var bEmail = false
        var bPass = false
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
                bPass = true
            } else {
                textInputLayout_Pass!!.error = "La contraseña debe tener mínimo 6 caracteres"
            }
        } else {
            textInputLayout_Pass!!.error = "Campo requerido"
        }
        return bEmail && bPass
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
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