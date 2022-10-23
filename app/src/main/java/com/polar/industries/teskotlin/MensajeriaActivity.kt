package com.polar.industries.teskotlin

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.polar.industries.teskotlin.adapters.AdapterListViewMensajeria
import com.polar.industries.teskotlin.datosPrueba.DatosPrueba
import com.polar.industries.teskotlin.models.Contacto

class MensajeriaActivity : AppCompatActivity() {
    private var listView_Mensajeria: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensajeria)
        title = "Mensajería"
        listView_Mensajeria = findViewById(R.id.listView_Mensajeria)
        val adapterListViewMensajeria = AdapterListViewMensajeria(
            this@MensajeriaActivity,
            R.layout.item_mensajeria,
            DatosPrueba.listContactos as List<Contacto>
        )
        listView_Mensajeria!!.adapter = adapterListViewMensajeria
    }
}