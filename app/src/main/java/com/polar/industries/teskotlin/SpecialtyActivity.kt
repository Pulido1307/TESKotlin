package com.polar.industries.teskotlin

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.polar.industries.teskotlin.adapters.AdapterListViewSpecialty
import com.polar.industries.teskotlin.helpers.FirestoreHelperTalacheros
import com.polar.industries.teskotlin.interfaces.TalacheroInterface
import com.polar.industries.teskotlin.models.Specialty
import com.polar.industries.teskotlin.models.Talacheros

class SpecialtyActivity : AppCompatActivity(), TalacheroInterface {
    private var listView_Especialidades: ListView? = null
    private val listaEspecialidades: MutableList<Specialty> = ArrayList<Specialty>()
    private val firestoreHelperTalacheros: FirestoreHelperTalacheros = FirestoreHelperTalacheros()
    private val talacheros: ArrayList<Talacheros> = ArrayList<Talacheros>()
    private val talacherosMecanica: ArrayList<Talacheros> = ArrayList<Talacheros>()
    private val talacherosFontaneria: ArrayList<Talacheros> = ArrayList<Talacheros>()
    private val talacherosPlomeria: ArrayList<Talacheros> = ArrayList<Talacheros>()
    private val talacherosElectriciste: ArrayList<Talacheros> = ArrayList<Talacheros>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specialty)
        title = "Especialidades de TES"
        firestoreHelperTalacheros.getTalacheros(this)
        listView_Especialidades = findViewById(R.id.listView_Especialidades)
        listView_Especialidades!!.setOnItemClickListener(OnItemClickListener { adapterView, view, position, l ->
            val intent = Intent(this@SpecialtyActivity, TalacherosActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable("talacherosMecanica", talacherosMecanica)
            bundle.putSerializable("talacherosFontaneria", talacherosFontaneria)
            bundle.putSerializable("talacherosPlomeria", talacherosPlomeria)
            bundle.putSerializable("talacherosElectriciste", talacherosElectriciste)
            intent.putExtra("listatalacheros", bundle)
            intent.putExtra("ESPECIALIDAD", listaEspecialidades[position].nombre)
            startActivity(intent)
        })
    }

    override fun getTalacheros(talacheros: List<Talacheros>) {
        this.talacheros.clear()
        this.talacheros.addAll(talacheros)
        especialidades(talacheros)
    }

    fun especialidades(talacheros: List<Talacheros>) {
        for (i in talacheros.indices) {
            if (talacheros[i].especialidad.contains("Mecánica")) talacherosMecanica.add(
                talacheros[i]
            )
            if (talacheros[i].especialidad.contains("Fontanería")) talacherosFontaneria.add(
                talacheros[i]
            )
            if (talacheros[i].especialidad.contains("Plomería")) talacherosPlomeria.add(
                talacheros[i]
            )
            if (talacheros[i].especialidad.contains("Electricista")
            ) talacherosElectriciste.add(
                talacheros[i]
            )
        }
        val s = Specialty("Mécanica", talacherosMecanica.size, R.drawable.mecanica)
        val s1 = Specialty("Fontanería", talacherosFontaneria.size, R.drawable.fontaneria)
        val s2 = Specialty("Plomería", talacherosPlomeria.size, R.drawable.plomeria)
        val s3 = Specialty("Electricidad", talacherosElectriciste.size, R.drawable.electricidad)
        listaEspecialidades.add(s)
        listaEspecialidades.add(s1)
        listaEspecialidades.add(s2)
        listaEspecialidades.add(s3)
        fill(listaEspecialidades)
    }

    fun fill(listaEspecialidades: List<Specialty>?) {
        //AdapterListViewSpecialty adapterListViewSpecialty = new AdapterListViewSpecialty(SpecialtyActivity.this, R.layout.item_especialidad, listaEspecialidades);
        listView_Especialidades!!.adapter = AdapterListViewSpecialty(
            this@SpecialtyActivity,
            R.layout.item_especialidad,
            listaEspecialidades as List<Specialty>
        )
    }
}