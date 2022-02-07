package at.fhooe.mc.fitcom.ui.exercises.exercisePool

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Filter
import androidx.appcompat.widget.SearchView
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
import java.util.*
import kotlin.collections.ArrayList


class ExercisePoolActivity : AppCompatActivity(), ExercisePoolAdapter.CallBackInterface {

    private lateinit var binding: ActivityExercisePoolBinding
    private var mExerciseData: ArrayList<ExercisePoolData>? = null
    private var mImagesData: ArrayList<ExercisePoolImagesData>? = null
    private var mFinalizedData: ArrayList<ExercisePoolFinalizedData>? = null
    private lateinit var mTempArrayList: ArrayList<ExercisePoolFinalizedData>
    private var mFilteredData: ArrayList<ExercisePoolFinalizedData> = ArrayList()
    private var mExerciseCompleteData: ArrayList<SingleCompleteExerciseData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExercisePoolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "Exercises"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_)

        if (!loadFromSharedPreferences()) {
            binding.activityExercisePoolProgressBar.isVisible = true
            fetchExerciseData()
        } else {
            loadFromSharedPreferences()
        }

        mTempArrayList = arrayListOf<ExercisePoolFinalizedData>()

        binding.activityExerciseFabExercisePool.isVisible = false

        binding.activityExercisePoolRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this@ExercisePoolActivity,
                DividerItemDecoration.VERTICAL
            )
        )

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

        binding.activityExerciseFabExercisePool.setOnClickListener{
            val intent = Intent();
            intent.putExtra("SingleCompleteExerciseDataArray", mExerciseCompleteData)
            setResult(RESULT_OK, intent);
            finish()
        }
    }

    override fun passResultCallback(exerciseData: SingleCompleteExerciseData) {
        binding.activityExerciseFabExercisePool.isVisible = true
        mExerciseCompleteData.add(exerciseData)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.top_nav_menu_search, menu)
        val item = menu?.findItem(R.id.search_action)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                populateRecyclerView(mTempArrayList)
                mTempArrayList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if(searchText.isNotEmpty()){
                    mFinalizedData?.forEach {

                        if(it.name.lowercase(Locale.getDefault()).contains(searchText)){
                            mTempArrayList.add(it)
                        }
                    }
                    binding.activityExercisePoolRecyclerView.adapter!!.notifyDataSetChanged()
                } else {

                    mTempArrayList.clear()
                    mTempArrayList.addAll(mFinalizedData!!)
                    binding.activityExercisePoolRecyclerView.adapter!!.notifyDataSetChanged()
                }

                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return true
    }

    fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                for (data: ExercisePoolFinalizedData in mFinalizedData!!) {

                    if (charString == data.category.toString()) {
                        mFilteredData?.add(data)
                    }
                }
                return FilterResults().apply { values = mFilteredData }
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {

            }
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
            ExercisePoolAdapter(data, this)
        binding.activityExercisePoolRecyclerView.layoutManager =
            LinearLayoutManager(this@ExercisePoolActivity)
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

        mTempArrayList.addAll(mFinalizedData!!)

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