package com.example.eletriccarapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccarapp.R
import com.example.eletriccarapp.data.CarsAPI
import com.example.eletriccarapp.data.local.CarRepository
import com.example.eletriccarapp.domain.Carro
import com.example.eletriccarapp.ui.adapter.CarAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CarFragment : Fragment() {

    //    var carrosArray: ArrayList<Carro> = ArrayList()
    private lateinit var fabCalcular: FloatingActionButton
    private lateinit var listaCarros: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var noInternetImage: ImageView
    lateinit var noInternetText: TextView

    private lateinit var carsApi: CarsAPI

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.car_fragment, container, false
        )
    }

    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupView(view)
        setuplisteners()
        setupRetroFit()
    }

    override fun onResume() {
        super.onResume()
        if (checkForInternet(context)) {
            getAllCars()
        } else {
            emptyState()
        }
    }

    private fun setupRetroFit() {
        val builder = Retrofit.Builder()
            .baseUrl("https://igorbag.github.io/cars-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        carsApi = builder.create(CarsAPI::class.java)
    }

    private fun getAllCars() {
        carsApi.getAllCars()
            .enqueue(object : Callback<List<Carro>> {
                override fun onResponse(
                    call: Call<List<Carro>>,
                    response: Response<List<Carro>>
                ) {
                    if (response.isSuccessful) {
                        progressBar.isVisible = false
                        noInternetText.isVisible = false
                        noInternetImage.isVisible = false

                        response.body()?.let {
                            setupList(it)
                        }

                    } else {
                        Toast.makeText(
                            context,
                            R.string.response_error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<List<Carro>>, t: Throwable
                ) {
                }

            })
    }

    private fun emptyState() {
        progressBar.isVisible = false
        fabCalcular.isVisible = false
        listaCarros.isVisible = false
        noInternetText.isVisible = false
        noInternetImage.isVisible = false
    }

    private fun setupView(view: View) {
        view.apply {
            listaCarros = findViewById(R.id.rv_lista_carros)
            fabCalcular = findViewById(R.id.fab_calcular)
            progressBar = findViewById(R.id.pb_loader)
            noInternetImage =
                findViewById(R.id.iv_empty_state)
            noInternetText =
                findViewById(R.id.tv_empty_state)
        }
    }

    private fun setupList(list: List<Carro>) {
        val carAdapter = CarAdapter(list)
        listaCarros.adapter = carAdapter
        listaCarros.isVisible = true
        fabCalcular.isVisible = true

        carAdapter.carItemListener = { carro ->
            CarRepository(requireContext()).saveIfNotExist(
                carro
            )
        }
    }

    private fun setuplisteners() {
        fabCalcular.setOnClickListener {
            val intent = Intent(
                context,
                ActivityCalcularAutonomia::class.java
            )
            startActivity(intent)
        }
    }

//    private fun callService() {
//        val urlBase = "https://igorbag.github.io/cars-api/cars.json"
//        MyTask().execute(urlBase)
//        progressBar.isVisible = true
//    }

    @SuppressLint("ObsoleteSdkInt")
    private fun checkForInternet(context: Context?): Boolean {
        val connectivityManeger =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManeger.activeNetwork
                ?: return false

            val activeNetwork =
                connectivityManeger.getNetworkCapabilities(
                    network
                ) ?: return false

            return when {
                activeNetwork.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                ) -> true

                activeNetwork.hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                ) -> true

                else -> false
            }
        } else {
            val networkIndo =
                connectivityManeger.activeNetworkInfo
                    ?: return false
            return networkIndo.isConnected
        }
    }


}


//    @SuppressLint("StaticFieldLeak")
//    inner class MyTask : AsyncTask<String, String, String>() {
//        @Deprecated("Deprecated in Java")
//        override fun doInBackground(vararg url: String?): String? {
//            var urlConnection: HttpURLConnection? = null
//
//            try {
//                val urlBase = URL(url[0])
//
//                urlConnection = urlBase.openConnection() as HttpURLConnection
//                urlConnection.connectTimeout = 60000
//                urlConnection.readTimeout = 60000
//                urlConnection.setRequestProperty(
//                    "Accept", "application/JSON"
//                )
//
//                val responseCode = urlConnection.responseCode
//
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    val response = urlConnection.inputStream.bufferedReader()
//                        .use { it.readText() }
//                    publishProgress(response)
//                } else {
//                    Log.d("erro:", "serviço fora do ar...")
//                }
//
//            } catch (ex: Exception) {
//                Log.e("Erro", "Erro ao realizar processamento....")
//
//            } finally {
//                urlConnection?.disconnect()
//            }
//            return " "
//        }
//
//        @Deprecated("Deprecated in Java")
//        override fun onProgressUpdate(vararg values: String?) {
//            try {
//                val jsonArray = JSONTokener(values[0]).nextValue() as JSONArray
//
//                for (i in 0 until jsonArray.length()) {
//                    val id = jsonArray.getJSONObject(i).getString("id")
//                    val price = jsonArray.getJSONObject(i).getString("preco")
//                    val battery =
//                        jsonArray.getJSONObject(i).getString("bateria")
//                    val horsepower =
//                        jsonArray.getJSONObject(i).getString("potencia")
//                    val chargintTime =
//                        jsonArray.getJSONObject(i).getString("recarga")
//                    val urlPhoto =
//                        jsonArray.getJSONObject(i).getString("urlPhoto")
////                    Log.d("hp", horsepower)
//
//                    val model = Carro(
//                        id = id.toInt(),
//                        price = price,
//                        battery = battery,
//                        horsepower = horsepower,
//                        charging = chargintTime,
//                        urlPhoto = urlPhoto
//                    )
//                    carrosArray.add(model)
//                }
////                setupList()
//                progressBar.isVisible = false
//                noInternetText.isVisible = false
//                noInternetImage.isVisible = false
//
//            } catch (ex: Exception) {
//                Log.e(
//                    "Erro progress",
//                    "Erro ao realizar onProgressUpdate: ${ex.message}"
//                )
//            }
//        }
//
//        fun streamToString(inputStream: InputStream): String {
//            val bufferReader = BufferedReader(InputStreamReader(inputStream))
//
//            var line: String
//            var result = ""
//
//            try {
//                do {
//                    line = bufferReader.readLine()
//                    line?.let {
//                        result += line
//                    }
//                } while (true)
//            } catch (ex: Exception) {
//                Log.e("Erro", "Erro ao parcelar stream")
//            }
//
//            return result
//        }
//
//        override fun onPreExecute() {
//            super.onPreExecute()
//            Log.d("My task:", "Iniciando")
//        }
//
//    }
