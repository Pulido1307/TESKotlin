package com.polar.industries.teskotlin

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.polar.industries.teskotlin.adapters.AdapterListViewMensajeria
import com.polar.industries.teskotlin.datosPrueba.DatosPrueba
import com.polar.industries.teskotlin.models.Contacto

class MensajeriaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensajeria)
        supportActionBar!!.hide()

    }
}