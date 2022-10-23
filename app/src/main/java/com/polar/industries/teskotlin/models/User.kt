package com.polar.industries.teskotlin.models

class User {
    var id: String? = null
    var tipo_user: String? = null
    var nombre: String? = null
    var apellidos: String? = null
    var telefono: String? = null
    var ubicacion: String? = null
    var email: String? = null
    var password: String? = null
    var especialidad: String? = null
    var isActivo = false
    var uriImage: String? = null

    constructor() {}
    constructor(
        id: String?,
        tipo_user: String?,
        nombre: String?,
        apellidos: String?,
        telefono: String?,
        ubicacion: String?,
        email: String?,
        password: String?,
        activo: Boolean,
        especialidad: String?,
        uri_image: String?
    ) {
        this.id = id
        this.tipo_user = tipo_user
        this.nombre = nombre
        this.apellidos = apellidos
        this.telefono = telefono
        this.ubicacion = ubicacion
        this.email = email
        this.password = password
        isActivo = activo
        this.especialidad = especialidad
        uriImage = uri_image
    }

    constructor(
        id: String?,
        tipo_user: String?,
        nombre: String?,
        apellidos: String?,
        telefono: String?,
        ubicacion: String?,
        email: String?,
        password: String?,
        activo: Boolean,
        uri_image: String?
    ) {
        this.id = id
        this.tipo_user = tipo_user
        this.nombre = nombre
        this.apellidos = apellidos
        this.telefono = telefono
        this.ubicacion = ubicacion
        this.email = email
        this.password = password
        isActivo = activo
        uriImage = uri_image
    }
}
