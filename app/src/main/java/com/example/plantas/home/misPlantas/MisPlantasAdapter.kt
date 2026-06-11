package com.example.plantas.home.misPlantas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plantas.core.database.PlantaFavorita
import com.example.plantas.databinding.ItemPlantaFavoritaBinding

class MisPlantasAdapter(
    private var listaPlantas: List<PlantaFavorita>,
    private val onPlantaClick: (PlantaFavorita) -> Unit // Para cuando el usuario toque una planta
) : RecyclerView.Adapter<MisPlantasAdapter.PlantaViewHolder>() {

    inner class PlantaViewHolder(private val binding: ItemPlantaFavoritaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(planta: PlantaFavorita) {
            binding.txtApodoFavorita.text = planta.apodo
            binding.txtNombreOriginalFavorita.text = planta.nombreOriginal

            Glide.with(binding.root.context)
                .load(planta.imagenLocalUri)
                .centerCrop()
                .into(binding.imgPlantaFavorita)

            binding.root.setOnClickListener {
                onPlantaClick(planta)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantaViewHolder {
        val binding = ItemPlantaFavoritaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlantaViewHolder(binding)
    }

    override fun getItemCount(): Int = listaPlantas.size

    override fun onBindViewHolder(holder: PlantaViewHolder, position: Int) {
        holder.bind(listaPlantas[position])
    }

    fun actualizarLista(nuevaLista: List<PlantaFavorita>) {
        listaPlantas = nuevaLista
        notifyDataSetChanged()
    }
}