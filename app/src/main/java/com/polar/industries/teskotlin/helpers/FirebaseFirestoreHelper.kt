package com.polar.industries.teskotlin.helpers

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.polar.industries.teskotlin.LoginActivity
import com.polar.industries.teskotlin.MainActivity
import com.polar.industries.teskotlin.interfaces.Information
import com.polar.industries.teskotlin.interfaces.TalacheroInterface
import com.polar.industries.teskotlin.models.User
import java.util.*
import kotlin.collections.ArrayList

class FirebaseFirestoreHelper {
    //Se debe modificar
    fun addData(
        information: Information,
        dialog: ProgressDialog,
        context: Context,
        uid: String,
        email: String,
        password: String,
        param: Array<String?>?,
        tipo: String
    ) {
        val usuario: MutableMap<String, Any?> = HashMap()

        if (tipo == "TALACHERO") {
            usuario["tipo_user"] = "TALACHERO"
            usuario["nombre"] = param!![0]
            usuario["apellidos"] = param!![1]
            usuario["telefono"] = param!![2]
            usuario["ubicacion"] = param!![3]
            usuario["especialidad"] = param!![4]
            usuario["email"] = email
            usuario["password"] = password
            usuario["uri_image"] = ""
            usuario["activo"] = true
            Log.e("talachero", usuario.values.toTypedArray().size.toString() + "")
            registerDataUserToFirestore(
                UsuariosCollection,
                uid,
                information,
                dialog,
                usuario,
                context
            )
        } else if (tipo == "CLIENTE") {
            usuario["tipo_user"] = "CLIENTE"
            usuario["nombre"] = param!![0]
            usuario["apellidos"] = param!![1]
            usuario["telefono"] = param!![2]
            usuario["ubicacion"] = param!![3]
            usuario["email"] = email
            usuario["password"] = password
            usuario["uri_image"] = ""
            usuario["activo"] = true
            Log.e("cliente", usuario.values.toTypedArray().size.toString() + "")
            registerDataUserToFirestore(
                UsuariosCollection,
                uid,
                information,
                dialog,
                usuario,
                context
            )
        }else{
            Toast.makeText(context, "Ya valio pitufi verga", Toast.LENGTH_LONG).show()
        }
    }

    private fun registerDataUserToFirestore(
        collectionReference: CollectionReference,
        document: String,
        information: Information,
        dialog: ProgressDialog,
        data: Map<String, Any?>,
        context: Context
    ) {
        // Add a new document with a generated ID
        collectionReference.document(document).set(data)
            .addOnCompleteListener { task ->
                dialog.dismiss()
                if (task.isSuccessful) {
                    val alertDialogBuilder = AlertDialog.Builder(context)
                    alertDialogBuilder.setCancelable(false)
                    alertDialogBuilder.setTitle("Aviso")
                    alertDialogBuilder.setMessage(" Usuario registrado con exito.")
                    alertDialogBuilder.setPositiveButton(
                        "Aceptar"
                    ) { alertDialog, i ->
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                        (context as Activity).finish()
                        alertDialog.dismiss()
                    }
                    alertDialogBuilder.show()
                } else {
                    information.getMessage("Error del registros de los datos. Inténtelo de nuevo")
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                    (context as Activity).finish()
                }
            }
    }

    fun getData(
        document: String?,
        dialog: ProgressDialog,
        information: Information,
        context: Context
    ) {
        dialog.show()
        UsuariosCollection.document(document!!).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (Objects.requireNonNull(document).exists()) {
                    val data = document.data
                    if (data!!["tipo_user"].toString() == "CLIENTE") {
                        //Cliente
                        user = User(
                            document.id,
                            data["tipo_user"].toString(),
                            data["nombre"].toString(),
                            data["apellidos"].toString(),
                            data["telefono"].toString(),
                            data["ubicacion"].toString(),
                            data["email"].toString(),
                            data["password"].toString(),
                            data["activo"] as Boolean,
                            data["uri_image"].toString()
                        )
                    } else {
                        //Talachero
                        user = User(
                            document.id,
                            data["tipo_user"].toString(),
                            data["nombre"].toString(),
                            data["apellidos"].toString(),
                            data["telefono"].toString(),
                            data["ubicacion"].toString(),
                            data["email"].toString(),
                            data["password"].toString(),
                            data["activo"] as Boolean,
                            data["especialidad"].toString(),
                            data["uri_image"].toString()
                        )
                    }
                    if (Objects.requireNonNull(document["activo"]) as Boolean) {
                        information.getMessage(
                            "Bienvenido " + user!!.nombre
                                .toString() + " " + user!!.apellidos
                        )
                        //Se debe redigir...
                        val intent = Intent(context, MainActivity::class.java)
                        intent.putExtra(
                            "ROL",
                            user!!.tipo_user
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                        (context as Activity).finish()
                    } else {
                        val alertDialogBuilder = AlertDialog.Builder(context)
                        alertDialogBuilder.setCancelable(false)
                        alertDialogBuilder.setTitle("Aviso")
                        alertDialogBuilder.setMessage("Su cuenta ha sido bloqueada, comuniquese con los administradores...")
                        alertDialogBuilder.setPositiveButton(
                            "Aceptar"
                        ) { alertDialog, i ->
                            FirebaseAuthHelper.mAuth.signOut()
                            user = null
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                            (context as Activity).finish()
                            alertDialog.cancel()
                        }
                        alertDialogBuilder.show()
                    }
                } else {
                    information.getMessage("No existe esa cuenta")
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                    (context as Activity).finish()
                }
            } else {
                information.getMessage("Error, verifique su conexión a Internet, si los problemas continuan contacte al administrado")
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
                (context as Activity).finish()
            }
            dialog.dismiss()
        }
    }

    fun updateDataUser(
        dialog: ProgressDialog,
        context: Context?,
        nombre: String,
        apellido: String,
        telefono: String,
        ubicacion: String,
        especialidad: String,
        tipo: String,
        information: Information
    ) {
        val data: MutableMap<String, Any> = HashMap()
        data["nombre"] = nombre.trim { it <= ' ' }
        data["apellidos"] = apellido.trim { it <= ' ' }
        data["telefono"] = telefono.trim { it <= ' ' }
        data["ubicacion"] = ubicacion.trim { it <= ' ' }
        if (tipo.equals("talachero", ignoreCase = true)) {
            data["especialidad"] = especialidad.trim { it <= ' ' }
        }
        UsuariosCollection.document(user?.id!!).update(data)
            .addOnSuccessListener {
                user!!.nombre = nombre.trim { it <= ' ' }
                user!!.apellidos = apellido.trim { it <= ' ' }
                user!!.telefono = telefono.trim { it <= ' ' }
                user!!.ubicacion = ubicacion.trim { it <= ' ' }
                user!!.especialidad = especialidad.trim { it <= ' ' }
                information.getMessage("¡Datos actualizados!")
                dialog.dismiss()
            }
            .addOnFailureListener {
                information.getMessage("Datos no actualizados, verifica tu conexión a Internet")
                AlertDialogPersonalized().alertDialogInformacion(
                    "Datos no actualizados, verifica tu conexión a Internet",
                    context
                )
                dialog.dismiss()
            }
    }

    fun updateImage(uri_image: String, information: Information) {
        val usuarioMap: MutableMap<String, Any> = HashMap()
        usuarioMap["uri_image"] = uri_image
        UsuariosCollection.document(user!!.id!!).update(usuarioMap)
            .addOnSuccessListener {
                user!!.uriImage = uri_image
                information.getMessage("Datos actualizados")
            }
            .addOnFailureListener { information.getMessage("Imagen no actualizada, verifica tu conexión a Internet") }
    }

    public fun getTalachero(talacheroInterface: TalacheroInterface, progressDialog: Dialog){
        UsuariosCollection.whereEqualTo("tipo_user", "TALACHERO").get().addOnCompleteListener { it->
            if(it.isSuccessful){
                val listaTalacheros: ArrayList<User> = arrayListOf()

                for (document in it.result){
                    listaTalacheros.add(
                        User(
                            document.id,
                            document.getString("tipo_user"),
                            document.getString("nombre"),
                            document.getString("apellidos"),
                            document.getString("telefono"),
                            document.getString("ubicacion"),
                            document.getString("email"),
                            document.getString("password"),
                            true,
                            document.getString("especialidad"),
                            document.getString("uri_image")
                        )
                    )
                }

                talacheroInterface.getTalacheros(listaTalacheros)
            }
        }
    }

    companion object {
        var user: User? = null
        var db = FirebaseFirestore.getInstance()
        val UsuariosCollection = db.collection("usuarios")
    }
}
