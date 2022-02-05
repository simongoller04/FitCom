package at.fhooe.mc.fitcom.ui.exercises.exercisePool.api

import at.fhooe.mc.fitcom.ui.exercises.exercisePool.ExercisePoolData
import at.fhooe.mc.fitcom.ui.exercises.exercisePool.ExercisePoolDataResult
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiInterface {
    @GET("exercise/?language=2&limit=231")
    fun getExercises(): Call<ExercisePoolDataResult>

    companion object {
        var BASE_URL = "https://wger.de/api/v2/"

        fun create(): ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }
}