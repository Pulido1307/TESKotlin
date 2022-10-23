package com.polar.industries.teskotlin

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.polar.industries.teskotlin.adapters.AdapterListViewContratoFinalizado
import com.polar.industries.teskotlin.adapters.AdapterListViewContratoSinFinalizar
import com.polar.industries.teskotlin.adapters.AdapterListViewContratosRechazados
import com.polar.industries.teskotlin.datosPrueba.DatosPrueba
import com.polar.industries.teskotlin.models.Contrato

class ContractHistoryActivity : AppCompatActivity() {
    private var listView_ContratosRechazados: ListView? = null
    private var listView_ContratosSinFinalizar: ListView? = null
    private var listView_ContratosFinalizados: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contract_history)
        title = "Historial de contratos"
        listView_ContratosRechazados = findViewById(R.id.listView_ContratosrRechazados)
        listView_ContratosSinFinalizar = findViewById(R.id.listView_ContratosSinFinalizar)
        listView_ContratosFinalizados = findViewById(R.id.listView_ContratosFinalizados)
        val adapterListViewCR = AdapterListViewContratosRechazados(
            this@ContractHistoryActivity,
            R.layout.item_contrato_rechazados,
            DatosPrueba.listContratosPendientes as List<Contrato>
        )
        listView_ContratosRechazados!!.setAdapter(adapterListViewCR)
        val adapterListViewCSF = AdapterListViewContratoSinFinalizar(
            this@ContractHistoryActivity,
            R.layout.item_contrato_sin_finalizar,
            DatosPrueba.listContratosSinFinalizar as List<Contrato>
        )
        listView_ContratosSinFinalizar!!.setAdapter(adapterListViewCSF)
        val adapterListViewCF = AdapterListViewContratoFinalizado(
            this@ContractHistoryActivity,
            R.layout.item_contratos_finalizados,
            DatosPrueba.listContratosFinalizados as List<Contrato>
        )
        listView_ContratosFinalizados!!.adapter  = adapterListViewCF
    }
}