package com.example.plantas.home.explorar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plantas.R
import com.example.plantas.core.model.Planta
import com.example.plantas.databinding.ItemPlantaBinding

class PlantasAdapter(
    private var listaPlantas: List<Planta>,
    private val onPlantaClick: (Planta) -> Unit
) : RecyclerView.Adapter<PlantasAdapter.PlantaViewHolder>() {

    inner class PlantaViewHolder(private val binding: ItemPlantaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(planta: Planta) {
            // Ajustado a los IDs del nuevo item_planta.xml
            binding.txtPlantName.text = planta.commonName ?: "Sin nombre común"
            binding.txtScientificName.text = planta.scientificName?.firstOrNull() ?: "Sin nombre científico"

            // Mostramos el tipo de riego en el Tag de abajo
            binding.tagWatering.text = planta.watering ?: "Regular"

            // Usamos 'regular_url' o 'original_url' que sí vienen en el JSON de Beeceptor
            val urlImagen = planta.defaultImage?.regularUrl ?: planta.defaultImage?.originalUrl

            Glide.with(binding.root.context)
                .load(urlImagen)
                .centerCrop()
                // Colocamos tu flor de respaldo mientras carga o por si la URL falla
                .placeholder(R.drawable.icono_flor)
                .error(R.drawable.icono_flor)
                .into(binding.imgPlant) // ID exacto del nuevo XML

            // Listener para cuando el usuario seleccione esta planta
            binding.root.setOnClickListener { onPlantaClick(planta) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantaViewHolder {
        val binding = ItemPlantaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlantaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantaViewHolder, position: Int) {
        holder.bind(listaPlantas[position])
    }

    override fun getItemCount(): Int = listaPlantas.size

    // Función para actualizar la lista de forma reactiva
    fun updateList(nuevaLista: List<Planta>) {
        this.listaPlantas = nuevaLista
        notifyDataSetChanged()
    }
}