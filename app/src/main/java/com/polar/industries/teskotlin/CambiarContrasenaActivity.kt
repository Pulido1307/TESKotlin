package com.polar.industries.teskotlin

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.polar.industries.teskotlin.helpers.FirebaseAuthHelper
import com.polar.industries.teskotlin.helpers.FirebaseFirestoreHelper
import kotlinx.android.synthetic.main.activity_cambiar_contrasena.*

class CambiarContrasenaActivity : AppCompatActivity() {
    private val firebaseAuthHelper: FirebaseAuthHelper = FirebaseAuthHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_contrasena)
        supportActionBar!!.hide()

        buttonNewPass.setOnClickListener {
            var flagPassActual = false
            var flagSizeNewPass = false
            var flagEqualsPass = false

            val passActual = textInputPassActual.editText!!.text.toString()
            val newPass = textInputNewPass.editText!!.text.toString()
            val confirmNewPass = textInputConfirmNewPass.editText!!.text.toString()

            if (passActual.length>=6){
                if (passActual.equals(FirebaseFirestoreHelper.user!!.password)){
                    flagPassActual = true
                } else{
                    textInputPassActual.error = "La contraseña no coincide con la contraseña actual"
                }
            }else{
                textInputPassActual.error = "La contraseña debe de tener un minimo de 6 caracteres"
            }

            if(newPass.length >= 6){
                if (!newPass.equals(FirebaseFirestoreHelper.user!!.password)){
                    flagSizeNewPass = true
                } else{
                    textInputNewPass.error = "La nueva contraseña debe ser diferente a la contraseña actual"
                }
            } else{
                textInputNewPass.error = "La contraseña debe de tener un minimo de 6 caracteres"
            }

            if(confirmNewPass.length >= 6){
                if (newPass.equals(confirmNewPass)){
                    flagEqualsPass = true
                } else{
                    textInputConfirmNewPass.error = "Las contraseñas no coinciden"
                }
            } else{
                textInputConfirmNewPass.error = "La contraseña debe de tener un minimo de 6 caracteres"
            }

            if (flagPassActual && flagSizeNewPass && flagEqualsPass){
                val dialog = ProgressDialog.show(this@CambiarContrasenaActivity, "", "Cambiando contraseña..", true)
                firebaseAuthHelper.actualizarPassword(newPass, this@CambiarContrasenaActivity, dialog)
            } else{
                Toast.makeText(this, "Revise los datos ingresados", Toast.LENGTH_SHORT).show()
            }
        }
    }


}