package com.polar.industries.teskotlin.interfaces

import android.app.ProgressDialog
import com.polar.industries.teskotlin.models.User

interface TalacherosFiltrados {
    public fun getTalacherosFiltrados(talacheros:ArrayList<User>, progressDialog: ProgressDialog, especialidad:String)
}