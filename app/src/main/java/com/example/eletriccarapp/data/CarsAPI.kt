package com.example.eletriccarapp.data

import com.example.eletriccarapp.domain.Carro
import retrofit2.Call
import retrofit2.http.GET

interface CarsAPI {

    @GET("cars.json")
    fun getAllCars(): Call<List<Carro>>
}