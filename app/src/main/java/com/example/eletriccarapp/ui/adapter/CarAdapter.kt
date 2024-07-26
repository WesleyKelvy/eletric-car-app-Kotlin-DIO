package com.example.eletriccarapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccarapp.R
import com.example.eletriccarapp.domain.Carro

class CarAdapter(
    private var carros: List<Carro>,
    private val isFavoriteScreen: Boolean = false
) : RecyclerView.Adapter<CarAdapter.ViewHolder>() {

    var carItemListener: (Carro) -> Unit = {}

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.carro_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = carros.size

    //Pega o conteudo da view e troca pela informação de item de uma lista
    override fun onBindViewHolder(
        holder: ViewHolder, position: Int
    ) {
        holder.price.text = carros[position].preco
        holder.batery.text = carros[position].bateria
        holder.charging.text = carros[position].recarga
        holder.horsepower.text = carros[position].potencia

        if (isFavoriteScreen) {
            holder.favorito.setImageResource((R.drawable.ic_star_selected))
        }

        holder.favorito.setOnClickListener {
            val carro = carros[position]
            carItemListener(carro)
            setupFavorite(carro, holder)
        }
    }

    private fun setupFavorite(
        carro: Carro, holder: ViewHolder
    ) {
        carro.isFavorite = !carro.isFavorite

        if (carro.isFavorite) {
            holder.favorito.setImageResource(R.drawable.ic_star_selected)
        } else {
            holder.favorito.setImageResource(R.drawable.ic_star)
        }
    }

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val price: TextView =
            view.findViewById(R.id.tv_preco_value)
        val batery: TextView =
            view.findViewById(R.id.tv_bateria_value)
        val horsepower: TextView =
            view.findViewById(R.id.tv_potencia_value)
        val charging: TextView =
            view.findViewById(R.id.tv_recarga_value)
        val favorito: ImageView =
            view.findViewById(R.id.iv_favorite)
    }

}
