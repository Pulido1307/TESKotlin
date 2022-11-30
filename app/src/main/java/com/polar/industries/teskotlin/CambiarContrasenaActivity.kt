package com.polar.industries.teskotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class CambiarContrasenaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_contrasena)
        supportActionBar!!.hide()
    }
}