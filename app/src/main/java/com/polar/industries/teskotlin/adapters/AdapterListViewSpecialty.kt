package com.polar.industries.teskotlin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.polar.industries.teskotlin.R
import com.polar.industries.teskotlin.models.Specialty

class AdapterListViewSpecialty(
    private val context: Context,
    private val idLayout: Int,
    specialties: List<Specialty>
) :
    BaseAdapter() {
    private val layoutInflater: LayoutInflater
    private var specialties: List<Specialty>

    init {
        this.specialties = specialties
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    fun getSpecialties(): List<Specialty> {
        return specialties
    }

    fun setSpecialties(specialties: List<Specialty>) {
        this.specialties = specialties
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return specialties.size
    }

    override fun getItem(i: Int): Any {
        return specialties[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(position: Int, view: View, viewGroup: ViewGroup): View {
        var view: View? = view
        val specialty: Specialty = specialties[position]
        var specialtyHolder: SpecialtyHolder? = null
        if (view == null) {
            view = layoutInflater.inflate(idLayout, null)
            specialtyHolder = SpecialtyHolder()
            specialtyHolder.imageView_Especialidad = view.findViewById(R.id.imageView_Especialidad)
            specialtyHolder.textView_NombreEspecialidad =
                view.findViewById(R.id.textView_NombreEspecialidad)
            specialtyHolder.textView_CantTalacherosEsp =
                view.findViewById(R.id.textView_CantTalacherosEsp)
            view.tag = specialtyHolder
            specialtyHolder.imageView_Especialidad!!.setImageResource(specialty.image!!)
            specialtyHolder.textView_NombreEspecialidad!!.text = specialty.nombre
            specialtyHolder.textView_CantTalacherosEsp!!.text = "${specialty.cantTalacheros}   Talacheros disponibles"
        } else {
            view.tag
        }
        return view!!
    }

    internal class SpecialtyHolder {
        var textView_NombreEspecialidad: TextView? = null
        var textView_CantTalacherosEsp: TextView? = null
        var imageView_Especialidad: ImageView? = null
    }
}
