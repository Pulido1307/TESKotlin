package com.polar.industries.teskotlin

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.polar.industries.teskotlin.helpers.*
import com.polar.industries.teskotlin.interfaces.Information
import java.util.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity(), Information {
    private var materialButton_Registrarse_Login: MaterialButton? = null
    private var materialButton_IniciarSesion: MaterialButton? = null
    private var materialButton_OlvidarPass: MaterialButton? = null
    private var textInputLayout_Email: TextInputLayout? = null
    private var textInputLayout_Pass: TextInputLayout? = null
    private val firebaseAuthHelper: FirebaseAuthHelper = FirebaseAuthHelper()
    private val firestoreHelper: FirebaseFirestoreHelper = FirebaseFirestoreHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()
        firebaseAuthHelper.setContext(this@LoginActivity)
        firebaseAuthHelper.setOnInformationListener(this)
        materialButton_Registrarse_Login = findViewById(R.id.materialButton_Registrarse_Login)
        materialButton_IniciarSesion = findViewById(R.id.materialButton_IniciarSesion)
        materialButton_OlvidarPass = findViewById(R.id.materialButton_OlvidarPass)
        textInputLayout_Email = findViewById(R.id.textInputLayout_Email_Login)
        textInputLayout_Pass = findViewById(R.id.textInputLayout_Pass_Login)
        buttons()
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuthHelper.currentUser != null) {
            val dialog = ProgressDialog.show(this@LoginActivity, "", "Ingresando... ", true)
            dialog.show()
            firestoreHelper.getData(
                FirebaseAuthHelper!!.currentUser!!.getUid(),
                dialog,
                this@LoginActivity,
                this@LoginActivity
            )
        }
    }

    private fun buttons() {
        materialButton_Registrarse_Login!!.setOnClickListener {
            Toast.makeText(this@LoginActivity, "Registrar usuario", Toast.LENGTH_SHORT).show()
            showDialogDecision()
        }
        materialButton_IniciarSesion!!.setOnClickListener {
            val email = textInputLayout_Email!!.editText!!.text.toString()
            val pass = textInputLayout_Pass!!.editText!!.text.toString()
            if (validarCampos(email, pass)) {
                val dialog = ProgressDialog.show(this@LoginActivity, "", "Ingresando... ", true)
                dialog.show()
                firebaseAuthHelper.signInWithEmailAndPassword(email, pass, dialog)
            }
        }
        materialButton_OlvidarPass!!.setOnClickListener { showDialogRecoverPass() }
    }

    private fun showDialogDecision() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.dialog_decision, null)
        builder.setView(view)
        val dialogDEC = builder.create()
        //dialogDEC.setCancelable(false);
        dialogDEC.show()
        val materialButton_desc_cliente =
            dialogDEC.findViewById<MaterialButton>(R.id.materialButton_desc_cliente)
        val materialButton_desc_talachero =
            dialogDEC.findViewById<MaterialButton>(R.id.materialButton_desc_talachero)
        materialButton_desc_cliente.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignInActivity::class.java)
            intent.putExtra("ROL", "CLIENTE")
            startActivity(intent)
            //finish();
        }
        materialButton_desc_talachero.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignInActivity::class.java)
            intent.putExtra("ROL", "TALACHERO")
            startActivity(intent)
            //finish();
        }
    }

    override fun getMessage(message: String?) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
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
        return if (bEmail && bPass) {
            true
        } else {
            false
        }
    }

    private fun showDialogRecoverPass() {
        val builder = AlertDialog.Builder(this@LoginActivity)
        val inflater = layoutInflater
        val view: View = inflater.inflate(R.layout.dialog_recover_pass, null)
        builder.setView(view)
            .setTitle("Recuperar contraseña")
            .setMessage("Para recuperar tu contraseña ingresa el correo electrónico con el que te registraste y da clic en enviar.")
        val dialogRecoverPass = builder.create()
        dialogRecoverPass.setCancelable(false)
        dialogRecoverPass.show()
        val editText_email_recover =
            dialogRecoverPass.findViewById<TextInputLayout>(R.id.editText_email_recover)
        val button_Send_recover =
            dialogRecoverPass.findViewById<MaterialButton>(R.id.button_Send_recover)
        val button_Cancel_recover =
            dialogRecoverPass.findViewById<MaterialButton>(R.id.button_Cancel_recover)
        button_Send_recover.setOnClickListener {
            var flag_Email = false
            if (!editText_email_recover.editText!!.text.toString().isEmpty()) {
                if (StringHelper().isEmail(editText_email_recover.editText!!.text.toString())) {
                    flag_Email = true
                } else {
                    editText_email_recover.error = "Correo electrónico inválido"
                }
            } else {
                editText_email_recover.error = "Correo electrónico requerido"
            }
            if (flag_Email) {
                FirebaseQueryHelper().BuscarCredenciales(
                    editText_email_recover.editText!!.text.toString(),
                    this@LoginActivity
                )
                dialogRecoverPass.dismiss()
            }
        }
        button_Cancel_recover.setOnClickListener { dialogRecoverPass.dismiss() }
    }
}