package com.example.maxfit.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.maxfit.Activity.DietaDetalleActivity
import com.example.maxfit.R
import com.example.maxfit.dietaT.Dieta

class DietaAdapter : RecyclerView.Adapter<DietaAdapter.DietaViewHolder>() {

    private val dietaList = mutableListOf<Dieta>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dieta, parent, false)
        return DietaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DietaViewHolder, position: Int) {
        val dieta = dietaList[position]
        holder.bind(dieta, dietaList, position)
    }

    override fun getItemCount(): Int {
        return dietaList.size
    }

    fun setDieta(lista: List<Dieta>) {
        dietaList.clear()
        dietaList.addAll(lista)
        notifyDataSetChanged()
    }

    class DietaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDia: TextView = itemView.findViewById(R.id.tvDia)
        private val tvReceta: TextView = itemView.findViewById(R.id.tvReceta)
        private val tvMomento: TextView = itemView.findViewById(R.id.tvMomento)
        private val tvKcal: TextView = itemView.findViewById(R.id.tvKcal)

        fun bind(dieta: Dieta, dietaList: List<Dieta>, position: Int) {
            if (position == 0 || dietaList[position - 1].dia != dieta.dia) {
                tvDia.text = dieta.dia
                tvDia.visibility = View.VISIBLE
            } else {
                tvDia.visibility = View.GONE
            }
            tvReceta.text = dieta.nombre
            tvMomento.text = dieta.momento_dia
            tvKcal.text = dieta.kcalTotal

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DietaDetalleActivity::class.java).apply {
                    putExtra("RECETA", dieta.nombre)
                    putExtra("PASOS", dieta.pasos)
                    putExtra("ALIMENTOS", dieta.alimentos)
                }
                itemView.context.startActivity(intent)
            }
        }
    }
}
