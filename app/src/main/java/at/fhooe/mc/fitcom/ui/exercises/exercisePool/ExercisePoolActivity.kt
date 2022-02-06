package at.fhooe.mc.fitcom.ui.exercises.exercisePool

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import at.fhooe.mc.fitcom.databinding.ActivityExercisePoolBinding
import at.fhooe.mc.fitcom.ui.exercises.exercisePool.api.ApiInterface
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExercisePoolActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExercisePoolBinding
    private var mExerciseData: ArrayList<ExercisePoolData>? = null
    private var mImagesData: ArrayList<ExercisePoolImagesData>? = null
    private var mFinalizedData: ArrayList<ExercisePoolFinalizedData>? = null

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

        binding.activityExercisePoolChipArms.setOnClickListener {

        }

        binding.activityExercisePoolChipBack.setOnClickListener {

        }

        binding.activityExercisePoolChipChest.setOnClickListener {

        }

        binding.activityExercisePoolChipCore.setOnClickListener {

        }

        binding.activityExercisePoolChipLegs.setOnClickListener {

        }

        binding.activityExercisePoolChipShoulders.setOnClickListener {

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
//                    saveData(resultFinal.results)
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
//                                saveImages(resultFinal.results)
                                mImagesData = resultFinal.results
                                filterList()
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
        data: ArrayList<ExercisePoolFinalizedData>
    ) {
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

    private fun saveFinalizedData(data: ArrayList<ExercisePoolFinalizedData>) {
        val json = Gson().toJson(data)
        val sharedPref =
            getSharedPreferences("at.fhooe.mc.fitcom.exercisePoolActivity", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("finalizedData", json.toString())
            apply()
        }
    }

    private fun loadFinalizedData(): ArrayList<ExercisePoolFinalizedData>? {
        val sharedPref =
            getSharedPreferences("at.fhooe.mc.fitcom.exercisePoolActivity", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("finalizedData", null)
        if (json != null) {
            val exercisePoolData: List<ExercisePoolFinalizedData> =
                Gson().fromJson(json, Array<ExercisePoolFinalizedData>::class.java).toList()
            return exercisePoolData as ArrayList<ExercisePoolFinalizedData>
        }
        return null
    }

    private fun loadFromSharedPreferences(): Boolean {
        mFinalizedData = loadFinalizedData()
        if (mFinalizedData != null) {
            populateRecyclerView(mFinalizedData!!)
            return true
        }
        return false
    }

    private fun filterList() {
        mFinalizedData = ArrayList<ExercisePoolFinalizedData>()
        mExerciseData!!.forEach {
            val imageID = getCorrectId(it.id)
            if (imageID != null) {
                val imageURL = mImagesData!!.find { it.id == imageID }?.image
                if (imageURL != null) {
                    mFinalizedData?.add(
                        ExercisePoolFinalizedData(
                            it.id,
                            it.name,
                            it.category,
                            imageURL
                        )
                    )
                }
            }
        }
        saveFinalizedData(mFinalizedData!!)
        populateRecyclerView(mFinalizedData!!)
    }

    //needed because the API is shit
    private fun getCorrectId(exerciseId: Int): Int? {
        when (exerciseId) {
            91 -> return 3
            93 -> return 7
            128 -> return 11
            88 -> return 15
            129 -> return 19
            81 -> return 23
            74 -> return 27
            82 -> return 31
            83 -> return 35
            151 -> return 39
            150 -> return 43
            86 -> return 47
            138 -> return 51
            195 -> return 55
            84 -> return 60
            386 -> return 62
            116 -> return 66
            192 -> return 68
            127 -> return 69
            181 -> return 73
            106 -> return 77
            109 -> return 84
            110 -> return 91
            193 -> return 93
            311 -> return 95
            210 -> return 105
            217 -> return 107
            98 -> return 111
            97 -> return 113
            100 -> return 115
            163 -> return 117
            122 -> return 119
            113 -> return 121
            130 -> return 125
            207 -> return 127
            791 -> return 137
            125 -> return 139
            143 -> return 143
            161 -> return 145
            176 -> return 147
            191 -> return 149
            111 -> return 151
            792 -> return 165
            177 -> return 167
            119 -> return 171
            123 -> return 173
            152 -> return 175
            148 -> return 177
            154 -> return 179
            117 -> return 181
            118 -> return 183
            768 -> return 189
            213 -> return 209
        }
        return null
    }

    //In case that all exercises which are fetched should be displayed
    //even those without an image

    //    private fun saveData(data: ArrayList<ExercisePoolData>) {
//        val json = Gson().toJson(data)
//        val sharedPref =
//            getSharedPreferences("at.fhooe.mc.fitcom.exercisePoolActivity", MODE_PRIVATE)
//        with(sharedPref.edit()) {
//            putString("exercisesData", json.toString())
//            apply()
//        }
//    }
//
//    private fun saveImages(data: ArrayList<ExercisePoolImagesData>) {
//        val json = Gson().toJson(data)
//        val sharedPref =
//            getSharedPreferences("at.fhooe.mc.fitcom.exercisePoolActivity", MODE_PRIVATE)
//        with(sharedPref.edit()) {
//            putString("exerciseImages", json.toString())
//            apply()
//        }
//    }

    //    private fun loadData(): ArrayList<ExercisePoolData>? {
//        val sharedPref =
//            getSharedPreferences("at.fhooe.mc.fitcom.exercisePoolActivity", MODE_PRIVATE)
//        val gson = Gson()
//        val json = sharedPref.getString("exercisesData", null)
//        if (json != null) {
//            val exercisePoolData: List<ExercisePoolData> =
//                Gson().fromJson(json, Array<ExercisePoolData>::class.java).toList()
//            return exercisePoolData as ArrayList<ExercisePoolData>
//        }
//        return null
//    }
//
//    private fun loadImages(): ArrayList<ExercisePoolImagesData>? {
//        val sharedPref =
//            getSharedPreferences("at.fhooe.mc.fitcom.exercisePoolActivity", MODE_PRIVATE)
//        val gson = Gson()
//        val json = sharedPref.getString("exerciseImages", null)
//        if (json != null) {
//            val exercisePoolData: List<ExercisePoolImagesData> =
//                Gson().fromJson(json, Array<ExercisePoolImagesData>::class.java).toList()
//            return exercisePoolData as ArrayList<ExercisePoolImagesData>
//        }
//        return null
//    }
}