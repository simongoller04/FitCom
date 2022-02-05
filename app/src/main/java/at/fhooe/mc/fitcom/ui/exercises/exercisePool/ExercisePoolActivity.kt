package at.fhooe.mc.fitcom.ui.exercises.exercisePool

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import at.fhooe.mc.fitcom.databinding.ActivityExercisePoolBinding
import at.fhooe.mc.fitcom.ui.exercises.exercisePool.api.ApiInterface
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExercisePoolActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExercisePoolBinding
    private var mExerciseNames = ArrayList<String>()
    private var mExerciseIcons = ArrayList<Drawable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExercisePoolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Exercises"

        if (!loadData()) {
            binding.activityExercisePoolProgressBar.isVisible = true
            apiCall()
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

    private fun apiCall() {
        val apiInterface = ApiInterface.create().getExercises()
        apiInterface.enqueue(object : Callback<ExercisePoolDataResult> {
            override fun onResponse(
                call: Call<ExercisePoolDataResult>?,
                response: Response<ExercisePoolDataResult>?
            ) {
                if (response?.body() != null) {
                    val resultFinal = response!!.body() as ExercisePoolDataResult
                    saveData(resultFinal.results)
                    populateRecyclerView(resultFinal.results)
                    binding.activityExercisePoolProgressBar.isVisible = false
                }
            }

            override fun onFailure(call: Call<ExercisePoolDataResult>?, t: Throwable?) {
                Log.e("ExercisePoolActivity::apiCall", "Fetching Data failed $t")
            }
        })
    }

    private fun populateRecyclerView(data: ArrayList<ExercisePoolData>) {
        binding.activityExercisePoolRecyclerView.adapter =
            ExercisePoolAdapter(data)
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

    private fun loadData(): Boolean {
        val sharedPref =
            getSharedPreferences("at.fhooe.mc.fitcom.exercisePoolActivity", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("exercisesData", null)
        if (json != null) {
            val exercisePoolData: List<ExercisePoolData> =
                Gson().fromJson(json, Array<ExercisePoolData>::class.java).toList()
            populateRecyclerView(exercisePoolData as ArrayList<ExercisePoolData>)
            return true
        }
        return false
    }


}