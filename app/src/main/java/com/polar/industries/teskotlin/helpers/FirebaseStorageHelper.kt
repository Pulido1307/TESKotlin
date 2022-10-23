package com.polar.industries.teskotlin.helpers

import android.net.Uri
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.polar.industries.teskotlin.interfaces.Information


class FirebaseStorageHelper {
    private val firebaseFirestoreHelper = FirebaseFirestoreHelper()
    private val mStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private val usuariosFiles: StorageReference = mStorage.getReference().child("usuarios")
    private var information: Information? = null
    fun addImage(uid: String?, uri: Uri?) {
        usuariosFiles.child(uid!!).putFile(uri!!)
            .addOnFailureListener(OnFailureListener { information!!.getMessage("Error de actualización de imagen, verifica tu conexión a Internet") })
            .addOnSuccessListener(
                OnSuccessListener<Any?> {
                    information!!.getMessage("Imagen actualizada")
                    usuariosFiles.child(uid).getDownloadUrl()
                        .addOnSuccessListener(OnSuccessListener<Any> { uri ->
                            firebaseFirestoreHelper.updateImage(
                                uri.toString(),
                                information!!
                            )
                        })
                })
    }

    fun deleteImage(uid: String?) {
        usuariosFiles.child(uid!!).delete().addOnSuccessListener(OnSuccessListener<Void?> {
            information!!.getMessage("Imagen eliminada")
            firebaseFirestoreHelper.updateImage("", information!!)
        }).addOnFailureListener(OnFailureListener {
            //status.status("Error de actualización de imagen, verifica tu conexión a Internet");
        })
    }

    fun setInformationListener(information: Information?) {
        this.information = information
    }
}
