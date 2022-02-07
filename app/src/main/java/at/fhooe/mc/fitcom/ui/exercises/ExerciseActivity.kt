package at.fhooe.mc.fitcom.ui.exercises

import android.R.attr.data
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import at.fhooe.mc.fitcom.R
import at.fhooe.mc.fitcom.databinding.ActivityExerciseBinding
import at.fhooe.mc.fitcom.ui.exercises.exercisePool.ExercisePoolActivity
import at.fhooe.mc.fitcom.ui.exercises.exercisePool.ExercisePoolFinalizedData
import at.fhooe.mc.fitcom.ui.exercises.exercisePool.SingleCompleteExerciseData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson


class ExerciseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseBinding
    private lateinit var mName: String
    private var mAuth = FirebaseAuth.getInstance()
    private var mDbCollection =
        Firebase.firestore.collection("users").document(mAuth.uid.toString())
    private var mExerciseNames = ArrayList<String>()
    private var mWeights = ArrayList<Float>()
    private var mReps = ArrayList<Int>()
    private var mSets = ArrayList<Int>()
    private var mImages = ArrayList<String>()
    private lateinit var mAdapter: ExerciseAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var extras: Bundle? = intent.extras
        if (extras != null) {
            mName = extras.getString("workout").toString()
        }

        supportActionBar?.title = mName

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_)

        binding.activityExerciseRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this@ExerciseActivity,
                DividerItemDecoration.VERTICAL
            )
        )

        mAdapter = ExerciseAdapter(mExerciseNames, mWeights, mReps, mSets, mImages)
        binding.activityExerciseRecyclerView.adapter = mAdapter
        binding.activityExerciseRecyclerView.layoutManager =
            LinearLayoutManager(binding.root.context)

        if (mAuth.currentUser != null) {
            mDbCollection.collection("exercises").document(mName).get().addOnCompleteListener() {
                if (it.isSuccessful) {
                    if (it.result != null) {
                        if (it.result!!.exists()) {

                            mExerciseNames = it.result!!["exerciseNames"] as ArrayList<String>
                            mWeights = it.result!!["weights"] as ArrayList<Float>
                            mReps = it.result!!["reps"] as ArrayList<Int>
                            mSets = it.result!!["sets"] as ArrayList<Int>
                            mImages = it.result!!["images"] as ArrayList<String>

                            binding.activityExerciseTextView.isVisible = mExerciseNames.isEmpty()

                            mAdapter =
                                ExerciseAdapter(mExerciseNames, mWeights, mReps, mSets, mImages)

                            binding.activityExerciseRecyclerView.adapter = mAdapter
                        }
                    }

                } else {
                    Toast.makeText(binding.root.context, "Fetching Data failed!", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        binding.activityExerciseFabAddExercise.setOnClickListener {
            startActivityForResult(
                (Intent(binding.root.context, ExercisePoolActivity::class.java)),
                1
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 1) {
            if (resultCode === RESULT_OK) {
                val resultData =
                    data?.getSerializableExtra("SingleCompleteExerciseDataArray") as ArrayList<SingleCompleteExerciseData>
                resultData.forEach {
                    mExerciseNames.add(it.name)
                    mWeights.add(it.weight)
                    mReps.add(it.reps)
                    mSets.add(it.sets)
                    mImages.add(it.image)
                }
                binding.activityExerciseTextView.isVisible = mExerciseNames.isEmpty()
                binding.activityExerciseRecyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        val sharedPref =
            getSharedPreferences("at.fhooe.mc.fitcom.exerciseActivity", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("exerciseData", null)
        if (json != null) {
            val exerciseData: SingleCompleteExerciseData =
                Gson().fromJson(json, SingleCompleteExerciseData::class.java)
            for ((index, value) in mExerciseNames.withIndex()) {
                if (value == exerciseData.name) {
                    mReps[index] = exerciseData.reps
                    mSets[index] = exerciseData.sets
                    mWeights[index] = exerciseData.weight
                }
            }
            sharedPref.edit().remove("exerciseData").apply()
            (binding.activityExerciseRecyclerView.adapter as ExerciseAdapter).markAsChecked(exerciseData.name)
            binding.activityExerciseRecyclerView.adapter?.notifyDataSetChanged()
        }
    }

    override fun onPause() {
        super.onPause()
        val exerciseData = hashMapOf(
            "exerciseNames" to mExerciseNames,
            "weights" to mWeights,
            "reps" to mReps,
            "sets" to mSets,
            "images" to mImages
        )
        mDbCollection.collection("exercises").document(mName).set(exerciseData)
    }

}