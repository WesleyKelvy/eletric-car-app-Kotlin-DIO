package com.example.eletriccarapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccarapp.R
import com.example.eletriccarapp.data.local.CarRepository
import com.example.eletriccarapp.domain.Carro
import com.example.eletriccarapp.ui.adapter.CarAdapter

class FavoriteFragment : Fragment() {
    lateinit var listaCarrosFavoritos: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_favorite, container, false
        )
    }

    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupList()
    }

    private fun setupView(view: View) {
        view.apply {
            listaCarrosFavoritos =
                findViewById(R.id.rv_lista_carros_favoritos)
        }
    }

    private fun getCarsOnLocalDatabase(): List<Carro> {
        val carRepository = CarRepository(requireContext())

        val carList = carRepository.getAll()
        return carList
    }

    private fun setupList() {
        val carList =
            getCarsOnLocalDatabase().toMutableList()

        val carAdapter = CarAdapter(carList, true)
        listaCarrosFavoritos.apply {
            isVisible = true
            adapter = carAdapter
        }

        carAdapter.carItemListener = { carro ->
            try {
                val deletedCar =
                    CarRepository(requireContext()).deleteItem(
                        carro.id
                    )
                if (deletedCar) {
                    carList.remove(carro)
                    updateList()
//                    Log.d("Success-->", "REMOVED!!!")
                }
            } catch (error: Exception) {
                Log.e("Error-->", error.toString())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateList()
        Log.d("ON RESUME-->", "HERE")
    }

    private fun updateList() {
        setupList()
    }
}