package com.example.eletriccarapp.data.local

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import com.example.eletriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_BATERIA
import com.example.eletriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_CAR_ID
import com.example.eletriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_POTENCIA
import com.example.eletriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_PRECO
import com.example.eletriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_RECARGA
import com.example.eletriccarapp.data.local.CarrosContract.CarEntry.COLUMN_NAME_URL_PHOTO
import com.example.eletriccarapp.data.local.CarrosContract.CarEntry.TABLE_NAME
import com.example.eletriccarapp.domain.Carro

class CarRepository(private val context: Context) {
    companion object {
        const val ID_WHEN_NO_CARRO = 0
    }

    private fun saveOnDatabase(carro: Carro): Boolean {
        var isSaved = false

        try {
            val dbHelper = CarsDbHelper(context)
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put(COLUMN_NAME_CAR_ID, carro.id)
                put(COLUMN_NAME_PRECO, carro.preco)
                put(COLUMN_NAME_BATERIA, carro.bateria)
                put(COLUMN_NAME_RECARGA, carro.recarga)
                put(COLUMN_NAME_URL_PHOTO, carro.urlPhoto)
                put(COLUMN_NAME_POTENCIA, carro.potencia)
            }

            val insert =
                db?.insert(TABLE_NAME, null, values)

            if (insert != null) {
                isSaved = true
            }

        } catch (ex: Exception) {
            ex.message?.let {
                Log.e("erro ao inserir ->", it)
            }
        }

        return isSaved
    }

    private fun findCarById(id: Int): Carro {
        val dbHelper = CarsDbHelper(context)
        val db = dbHelper.writableDatabase
        // Lista das colunas a serem exibidas no resultado da Query
        val columns = arrayOf(
            BaseColumns._ID,
            COLUMN_NAME_CAR_ID,
            COLUMN_NAME_PRECO,
            COLUMN_NAME_BATERIA,
            COLUMN_NAME_POTENCIA,
            COLUMN_NAME_RECARGA,
            COLUMN_NAME_URL_PHOTO
        )

        val filter = "$COLUMN_NAME_CAR_ID = ?"
        val filterValues = arrayOf(id.toString())

        val cursor = db.query(
            TABLE_NAME,
            columns,
            filter,
            filterValues,
            null,
            null,
            null
        )

        var itemId: Long = 0
        var preco = ""
        var bateria = ""
        var recarga = ""
        var potencia = ""
        var url_photo = ""

        with(cursor) {
            while (moveToNext()) {
                itemId = getLong(
                    getColumnIndexOrThrow(BaseColumns._ID)
                )
//                Log.d("ID ->", itemId.toString())

                preco = getString(
                    getColumnIndexOrThrow(COLUMN_NAME_PRECO)
                )
//                Log.d("preco carro ->", preco)

                bateria = getString(
                    getColumnIndexOrThrow(
                        COLUMN_NAME_BATERIA
                    )
                )
//                Log.d("bateria carro ->", bateria)

                recarga = getString(
                    getColumnIndexOrThrow(
                        COLUMN_NAME_RECARGA
                    )
                )
//                Log.d("preco carro ->", recarga)

                potencia = getString(
                    getColumnIndexOrThrow(
                        COLUMN_NAME_POTENCIA
                    )
                )
//                Log.d("potencia carro ->", potencia)

                url_photo = getString(
                    getColumnIndexOrThrow(
                        COLUMN_NAME_URL_PHOTO
                    )
                )
//                Log.d("URL carro ->", url_photo)
            }
        }
        cursor.close()

        return Carro(
            id = itemId.toInt(),
            bateria = bateria,
            potencia = potencia,
            recarga = recarga,
            urlPhoto = url_photo,
            preco = preco,
            isFavorite = true
        )
    }

    fun saveIfNotExist(carro: Carro) {
        val car = findCarById(carro.id)
        if (car.id == ID_WHEN_NO_CARRO) {
            saveOnDatabase(carro)
        }
    }

    fun getAll(): MutableList<Carro> {
        val dbHelper = CarsDbHelper(context)
        val db = dbHelper.writableDatabase
        // Lista das colunas a serem exibidas no resultado da Query
        val columns = arrayOf(
            BaseColumns._ID,
            COLUMN_NAME_CAR_ID,
            COLUMN_NAME_PRECO,
            COLUMN_NAME_BATERIA,
            COLUMN_NAME_POTENCIA,
            COLUMN_NAME_RECARGA,
            COLUMN_NAME_URL_PHOTO
        )

        val cursor = db.query(
            TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            null
        )

        val carros = mutableListOf<Carro>()

        with(cursor) {
            while (moveToNext()) {
                val itemId = getLong(
                    getColumnIndexOrThrow(BaseColumns._ID)
                )
//                Log.d("ID ->", itemId.toString())

                val preco = getString(
                    getColumnIndexOrThrow(COLUMN_NAME_PRECO)
                )
//                Log.d("preco carro ->", preco)

                val bateria = getString(
                    getColumnIndexOrThrow(
                        COLUMN_NAME_BATERIA
                    )
                )
//                Log.d("bateria carro ->", bateria)

                val recarga = getString(
                    getColumnIndexOrThrow(
                        COLUMN_NAME_RECARGA
                    )
                )
//                Log.d("preco carro ->", recarga)

                val potencia = getString(
                    getColumnIndexOrThrow(
                        COLUMN_NAME_POTENCIA
                    )
                )
//                Log.d("potencia carro ->", potencia)

                val url_photo = getString(
                    getColumnIndexOrThrow(
                        COLUMN_NAME_URL_PHOTO
                    )
                )
//                Log.d("URL carro ->", url_photo)

                carros.add(
                    Carro(
                        id = itemId.toInt(),
                        bateria = bateria,
                        potencia = potencia,
                        recarga = recarga,
                        urlPhoto = url_photo,
                        preco = preco,
                        isFavorite = true
                    )
                )
            }
        }
        cursor.close()
        return carros
    }

    fun deleteItem(id: Int): Boolean {
        val dbHelper = CarsDbHelper(context)
        val db = dbHelper.writableDatabase

        val filter = "${COLUMN_NAME_CAR_ID} = ?"
        val filterValues = arrayOf(id.toString())

        val deletedRows = db.delete(
            TABLE_NAME, filter, filterValues
        )
        return deletedRows > 0
    }


}