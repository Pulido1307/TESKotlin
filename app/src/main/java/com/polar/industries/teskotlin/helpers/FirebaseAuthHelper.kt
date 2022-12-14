package com.polar.industries.teskotlin.helpers

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
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
                       /* var token: String = ""
                        FirebaseMessaging.getInstance().token.addOnCompleteListener(
                            OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.w("TOKEN", "Fetching FCM registration token failed", task.exception)
                                return@OnCompleteListener
                            }

                            // Get new FCM registration token
                            token = task.result
                        })*/

                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth.currentUser
                        //Creaci??n de usuario
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
                            information!!.getMessage("La contrase??a debe de tener por lo menos 6 caracteres")
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

    //Iniciar sesi??n
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
                                "Esas credenciales no existen en la base de datos o el email es inv??lido"
                            )
                            "The password is invalid or the user does not have a password." -> information!!.getMessage(
                                "La contrase??a es incorrecta"
                            )
                            "The user account has been disabled by an administrator." -> information!!.getMessage(
                                "Cuenta inhabilidada, contacta al administrador"
                            )
                            else -> information!!.getMessage("Verifica tu conexi??n a Internet")
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

    ///Cerrar sesi??n
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

    public fun actualizarPassword(password: String, activity: Activity, dialog: ProgressDialog, context: Context, information: Information){
        val userCambiarPass = Firebase.auth.currentUser
        userCambiarPass!!.updatePassword(password).addOnCompleteListener {
           if(it.isSuccessful){
               FirebaseFirestoreHelper.user!!.password = password
               Toast.makeText(activity.baseContext, "Contrase??a actualizada", Toast.LENGTH_SHORT).show()
               firebaseFirestoreHelper.updatePassword(password, context, dialog, information)
               activity.finish()
           } else{
               dialog.dismiss()
               Toast.makeText(activity.baseContext, "Error: ${it.exception}", Toast.LENGTH_LONG).show()
           }
        }.addOnFailureListener{
            dialog.dismiss()
            Toast.makeText(activity.baseContext, "Error: $it", Toast.LENGTH_LONG).show()
        }
    }


    companion object {
        // Initialize Firebase Auth
        var mAuth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser?
            get() = mAuth.currentUser
    }
}
