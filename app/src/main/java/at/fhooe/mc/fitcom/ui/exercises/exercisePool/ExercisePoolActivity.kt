package at.fhooe.mc.fitcom.ui.exercises.exercisePool

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import at.fhooe.mc.fitcom.R
import at.fhooe.mc.fitcom.databinding.ActivityExercisePoolBinding
import at.fhooe.mc.fitcom.ui.exercises.exercisePool.api.ApiInterface
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExercisePoolActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExercisePoolBinding
    private lateinit var mExerciseData: ArrayList<ExercisePoolData>
    private lateinit var mImagesData: ArrayList<ExercisePoolImagesData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExercisePoolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Exercises"

        if (!loadFromSharedPreferences()) {
            binding.activityExercisePoolProgressBar.isVisible = true
            fetchExerciseData()
        } else {
            loadFromSharedPreferences()
        }

        binding.activityExercisePoolChipArm.setOnClickListener {

        }

        binding.activityExercisePoolChipBack.setOnClickListener {

        }

        binding.activityExercisePoolChipChest.setOnClickListener {

        }

        binding.activityExercisePoolChipCore.setOnClickListener {

        }

        binding.activityExercisePoolChipLegs.setOnClickListener {

        }

        binding.activityExercisePoolChipShoulder.setOnClickListener {

        }
    }

    //TODO not really efficient
    private fun fetchExerciseData() {
        val apiInterface = ApiInterface.create().getExercises()
        apiInterface.enqueue(object : Callback<ExercisePoolDataResult> {
            override fun onResponse(
                call: Call<ExercisePoolDataResult>?,
                response: Response<ExercisePoolDataResult>?
            ) {
                if (response?.body() != null) {
                    val resultFinal = response!!.body() as ExercisePoolDataResult
                    saveData(resultFinal.results)
                    mExerciseData = resultFinal.results

                    //fetch images
                    val apiInterfaceImages = ApiInterface.create().getImages()
                    apiInterfaceImages.enqueue(object : Callback<ExercisePoolImagesDataResult> {
                        override fun onResponse(
                            call: Call<ExercisePoolImagesDataResult>?,
                            response: Response<ExercisePoolImagesDataResult>?
                        ) {
                            if (response?.body() != null) {
                                val resultFinal = response!!.body() as ExercisePoolImagesDataResult
                                saveImages(resultFinal.results)
                                mImagesData = resultFinal.results
                                populateRecyclerView(mExerciseData, mImagesData)
                                binding.activityExercisePoolProgressBar.isVisible = false
                            }
                        }
                        override fun onFailure(
                            call: Call<ExercisePoolImagesDataResult>?,
                            t: Throwable?
                        ) {
                            Log.e("ExercisePoolActivity::fetchImages", "Fetching Images failed $t")
                        }
                    })
                }
            }

            override fun onFailure(call: Call<ExercisePoolDataResult>?, t: Throwable?) {
                Log.e("ExercisePoolActivity::fetchData", "Fetching Data failed $t")
            }
        })
    }

    private fun populateRecyclerView(
        data: ArrayList<ExercisePoolData>,
        images: ArrayList<ExercisePoolImagesData>
    ) {
        binding.activityExercisePoolRecyclerView.adapter =
            ExercisePoolAdapter(data, images)
        binding.activityExercisePoolRecyclerView.layoutManager =
            LinearLayoutManager(this@ExercisePoolActivity)
        binding.activityExercisePoolRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this@ExercisePoolActivity,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun saveData(data: ArrayList<ExercisePoolData>) {
        val json = Gson().toJson(data)
        val sharedPref =
            getSharedPreferences("at.fhooe.mc.fitcom.exercisePoolActivity", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("exercisesData", json.toString())
            apply()
        }
    }

    private fun saveImages(data: ArrayList<ExercisePoolImagesData>) {
        val json = Gson().toJson(data)
        val sharedPref =
            getSharedPreferences("at.fhooe.mc.fitcom.exercisePoolActivity", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("exerciseImages", json.toString())
            apply()
        }
    }

    private fun loadFromSharedPreferences(): Boolean {
        val exerciseData = loadData()
        val imagesData = loadImages()
        if (exerciseData != null && imagesData != null) {
            populateRecyclerView(exerciseData, imagesData)
            return true
        }
        return false
    }

    private fun loadData(): ArrayList<ExercisePoolData>? {
        val sharedPref =
            getSharedPreferences("at.fhooe.mc.fitcom.exercisePoolActivity", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("exercisesData", null)
        if (json != null) {
            val exercisePoolData: List<ExercisePoolData> =
                Gson().fromJson(json, Array<ExercisePoolData>::class.java).toList()
            return exercisePoolData as ArrayList<ExercisePoolData>
        }
        return null
    }

    private fun loadImages(): ArrayList<ExercisePoolImagesData>? {
        val sharedPref =
            getSharedPreferences("at.fhooe.mc.fitcom.exercisePoolActivity", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("exerciseImages", null)
        if (json != null) {
            val exercisePoolData: List<ExercisePoolImagesData> =
                Gson().fromJson(json, Array<ExercisePoolImagesData>::class.java).toList()
            return exercisePoolData as ArrayList<ExercisePoolImagesData>
        }
        return null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {

            R.id.navigation_settings -> {
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}