package com.polar.industries.teskotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ContractsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contracts)
        supportActionBar!!.hide()
    }
}