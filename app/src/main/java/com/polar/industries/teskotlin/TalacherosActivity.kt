package com.polar.industries.teskotlin

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.polar.industries.teskotlin.adapters.AdapterListViewTalachero
import com.polar.industries.teskotlin.helpers.FirestoreHelperTalacheros
import com.polar.industries.teskotlin.models.Talacheros

class TalacherosActivity : AppCompatActivity() {
    private var listView_Talacheros: ListView? = null
    private var listaLlenar: ArrayList<Talacheros>? = null
    private val firestoreHelperTalacheros: FirestoreHelperTalacheros = FirestoreHelperTalacheros()
    private val talacheros: ArrayList<Talacheros> = ArrayList<Talacheros>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talacheros)
        title = "Talacheros disponibles"
        var bundle: Bundle? = Bundle()
        bundle = intent.getBundleExtra("listatalacheros")
        val parametros = this.intent.extras
        listView_Talacheros = findViewById(R.id.listView_Talacheros)
        if (parametros != null) {
            if (parametros.getString("ESPECIALIDAD").equals("mécanica", ignoreCase = true)) {
                //firestoreHelperTalacheros.getTalacheros2(this, "Mecánica");
                listaLlenar =
                    bundle!!.getSerializable("talacherosMecanica") as ArrayList<Talacheros>?
            } else if (parametros.getString("ESPECIALIDAD")
                    .equals("fontanería", ignoreCase = true)
            ) {
                //firestoreHelperTalacheros.getTalacheros2(this,"Fontanería");
                listaLlenar =
                    bundle!!.getSerializable("talacherosFontaneria") as ArrayList<Talacheros>?
            } else if (parametros.getString("ESPECIALIDAD").equals("plomería", ignoreCase = true)) {
                //firestoreHelperTalacheros.getTalacheros2(this,"Plomería");
                listaLlenar =
                    bundle!!.getSerializable("talacherosPlomeria") as ArrayList<Talacheros>?
            } else if (parametros.getString("ESPECIALIDAD")
                    .equals("electricidad", ignoreCase = true)
            ) {
                //firestoreHelperTalacheros.getTalacheros2(this,"Electricista");
                listaLlenar =
                    bundle!!.getSerializable("talacherosElectriciste") as ArrayList<Talacheros>?
            }
        }
        listView_Talacheros = findViewById(R.id.listView_Talacheros)
        val adapterListViewTalachero =
            AdapterListViewTalachero(this@TalacherosActivity, R.layout.item_talachero, listaLlenar as List<Talacheros>)
        listView_Talacheros!!.setAdapter(adapterListViewTalachero)
    } /* @Override
    public void getTalacheros(List<Talachero> talacheros) {
        this.talacheros.clear();
        this.talacheros.addAll(talacheros);
        fill(talacheros);
    }

    private void fill(List<Talachero> listTalachero) {
        listView_Talacheros.setAdapter(new AdapterListViewTalachero(TalacherosActivity.this, R.layout.item_talachero, listTalachero));
    }*/
}