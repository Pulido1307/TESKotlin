package com.polar.industries.teskotlin.helpers

import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.net.PasswordAuthentication
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session


class FirebaseQueryHelper {
    fun BuscarCredenciales(email: String?, context: Context?) {
        val dialog = ProgressDialog.show(
            context, "",
            "Verificando en el sistema", true
        )
        dialog.show()
        UsuariosCollection.whereEqualTo("email", email).get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                dialog.dismiss()
                if (queryDocumentSnapshots.documents.size > 0) {
                    val mapData = queryDocumentSnapshots.documents[0].data
                    val datos = arrayOf(
                        mapData!!["nombre"].toString() + " " + mapData["apellidos"],
                        mapData["password"].toString()
                    )
                    sendEmailWithGmail(
                        "talachitasexpressservices@gmail.com",
                        "TES20210909",
                        mapData["email"].toString(),
                        context,
                        datos
                    )
                } else {
                    Toast.makeText(context, "No existe registro con esos datos", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    fun sendEmailWithGmail(
        from: String?,
        passwordfrom: String?,
        to: String?,
        context: Context?,
        datos: Array<String>?
    ) {
        val props = Properties()
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.socketFactory.port"] = "465"
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = "465"
        val session: Session = Session.getDefaultInstance(props, object : Authenticator() {
            protected val passwordAuthentication: PasswordAuthentication
                protected get() = PasswordAuthentication(from, passwordfrom)
        })
        val task = SenderAsyncTask(session, from, to, context, datos)
        task.execute()
    }

    companion object {
        private val db = FirebaseFirestore.getInstance()
        private val UsuariosCollection = db.collection("usuarios")
    }
}
