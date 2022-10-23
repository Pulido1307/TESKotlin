package com.polar.industries.teskotlin.helpers

import android.app.AlertDialog
import android.content.Context

class AlertDialogPersonalized {
    fun alertDialogInformacion(message: String?, context: Context?) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Aviso")
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton(
            "Aceptar"
        ) { alertDialog, i -> alertDialog.cancel() }
        alertDialogBuilder.show()
    }
}
