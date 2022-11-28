package com.polar.industries.teskotlin.helpers

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.polar.industries.teskotlin.LoginActivity
import com.polar.industries.teskotlin.interfaces.Information
import java.util.*

class FirebaseAuthHelper {
    private var information: Information? = null
    private var context: Context? = null
    private val stringHelper = StringHelper()
    private val firebaseFirestoreHelper = FirebaseFirestoreHelper()
    fun setContext(context: Context?) {
        this.context = context
    }

    //Registrarse
    fun createUserEmailAndPassword(email: String?, password: String?, dialog: ProgressDialog, args: Array<String?>?, tipo: String?) {
        if (stringHelper.isNotEmptyCredentials(email, password!!)) {
            mAuth.createUserWithEmailAndPassword(
                email!!,
                password
            )
                .addOnCompleteListener(
                    (context as Activity?)!!
                ) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth.currentUser
                        //Creación de usuario
                        firebaseFirestoreHelper.addData(
                            information!!, dialog,
                            context!!, user!!.uid, email, password, args, tipo!!
                        )
                    } else {
                        val error =
                            Objects.requireNonNull(task.exception)!!.message
                        if (error == "The email address is already in use by another account.") {
                            information!!.getMessage("Error en el registro, email registrado")
                        } else if (error == "The given password is invalid. [ Password should be at least 6 characters ]") {
                            information!!.getMessage("La contraseña debe de tener por lo menos 6 caracteres")
                        } else {
                            information!!.getMessage("Error al registrarse")
                        }
                        //createUser(null,"",null);
                        dialog.dismiss()
                    }
                }
        } else {
            dialog.dismiss()
            information!!.getMessage("Error en el email o en el password")
        }
    }

    //Iniciar sesión
    fun signInWithEmailAndPassword(email: String?, password: String?, dialog: ProgressDialog) {
        if (stringHelper.isNotEmptyCredentials(email, password!!)) {
            mAuth.signInWithEmailAndPassword(email!!, password)
                .addOnCompleteListener(context as Activity) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        firebaseFirestoreHelper.getData(
                            Objects.requireNonNull(user)!!.uid, dialog,
                            information!!,
                            context!!
                        )
                    } else {
                        val error =
                            Objects.requireNonNull(task.exception)!!.message
                        when (Objects.requireNonNull(error)) {
                            "There is no user record corresponding to this identifier. The user may have been deleted." -> information!!.getMessage(
                                "Esas credenciales no existen en la base de datos o el email es inválido"
                            )
                            "The password is invalid or the user does not have a password." -> information!!.getMessage(
                                "La contraseña es incorrecta"
                            )
                            "The user account has been disabled by an administrator." -> information!!.getMessage(
                                "Cuenta inhabilidada, contacta al administrador"
                            )
                            else -> information!!.getMessage("Verifica tu conexión a Internet")
                        }
                        Log.e("error", error!!)
                        dialog.dismiss()
                    }
                }
        } else {
            information!!.getMessage("Error en el email o en el password")
            dialog.dismiss()
        }
    }

    ///Cerrar sesión
    fun signout(dialog: ProgressDialog, context: Context) {
        mAuth.signOut()
        FirebaseFirestoreHelper.user = null
        Log.e("er", mAuth.currentUser.toString() + "")
        dialog.dismiss()
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
        (context as Activity).finish()
    }

    fun setOnInformationListener(information: Information?) {
        this.information = information
    }

    companion object {
        // Initialize Firebase Auth
        var mAuth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser?
            get() = mAuth.currentUser
    }
}
