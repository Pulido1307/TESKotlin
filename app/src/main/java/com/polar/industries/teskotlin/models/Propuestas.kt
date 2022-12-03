package com.polar.industries.teskotlin.models

data class Propuestas(val uidContrato: String = "", val clienteUID: String? = "", val talacheroUID: String? = "", val monto: Double ? = 0.0, val descripcionArreglo: String? = "",
                      val fecha: String? = "", var status: String? = "", val nombreCliente: String? = "", val nombreTalachero: String? = "")
